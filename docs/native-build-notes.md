# Native executables and installers (GraalVM Native Image)

ArabicBASIC builds a standalone native executable with **GraalVM Native Image** and wraps it in an installer for non-Java users:

| Platform | Artifact | Built from |
|----------|----------|------------|
| macOS | `ArabicBASIC-<version>-macos-universal.pkg` (arm64 + x64 merged with `lipo`) | `macos-latest` + `macos-15-intel` |
| Windows | `ArabicBASIC-<version>-windows-x64-setup.exe` (Inno Setup) | `windows-latest` |
| Linux | not yet (see below) | |

**Truffle is not required.** Native Image compiles ordinary Java ahead of time. Truffle is a separate framework for building a high-performance language runtime and is unrelated to producing a native binary (see the Truffle track in `docs/todo.md`).

**Native Image cannot cross-compile.** It builds only for the OS and CPU it runs on, so each target gets its own CI runner. Windows on ARM is not supported by GraalVM.

## Building locally

Use a **GraalVM CE 25** JDK (the project's `.sdkmanrc` pins Temurin for normal work, so switch only for native builds):

```bash
sdk install java 25.5.5.0+3-graalce   # answer "n" to "set as default"; check `sdk current java` afterwards
export JAVA_HOME="$HOME/.sdkman/candidates/java/25.5.5.0+3-graalce"
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw -Pnative -DskipTests package
target/arabicbasic --version
```

The `native` profile in `pom.xml` uses `native-maven-plugin` (`compile-no-fork` bound to `package`), produces `target/arabicbasic[.exe]` in about 30 seconds, and the result is about 21 MB.

macOS installer from the local binary (ad-hoc signed, one architecture):

```bash
scripts/package-macos-pkg.sh 0.7.3 target/installer/ArabicBASIC-0.7.3-macos.pkg target/arabicbasic
```

## Native-image configuration

| Concern | How it is handled |
|---------|-------------------|
| Picocli reflection / `Messages` bundle | `picocli-codegen` annotation processor writes `META-INF/native-image/picocli-generated/...` at compile time |
| `InterpreterMessages` and `Messages` bundles | `-H:IncludeResourceBundles=...` in `src/main/resources/META-INF/native-image/native-image.properties` |
| `--version` | `arabicbasic-version.properties` (Maven-filtered with the project version, registered in `reachability-metadata.json`) read by `ManifestVersionProvider`. Native binaries have no JAR manifest. |
| Arabic locale data (`ar-u-nu-arab`, Arabic-Indic digits) | `-H:IncludeLocales=ar,en` in the `native` profile |
| UTF-8 | `-Dfile.encoding=UTF-8` in the `native` profile |
| JLine terminal | JLine 4.1.0 ships its own native-image metadata; `--enable-native-access=ALL-UNNAMED` silences the FFM restricted-method warning; `-H:+SharedArenaSupport` is required because JLine's FFM signal handler closes a shared `Arena` during `readLine` (without it the REPL crashes with `UnsupportedFeatureError: Support for Arena.ofShared is not active`) |
| ANTLR runtime, ICU4J | needed no extra metadata; ICU4J is only reached from `--unicode-map` |

Verified locally on macOS arm64: `--version`, the `*_Ar.bas` examples, runtime and parse error messages for `--locale ar`, `en` and `en-US`, `--unicode-map`, and the non-TTY REPL all produce output identical to the JAR.

If a future change adds reflection or resources, run the tracing agent to find what is missing:

```bash
java -agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image/agent -jar target/ArabicBASIC-*-jar-with-dependencies.jar examples/LOOP_Ar.bas
```

## Not yet verified (needs a human at a real terminal)

- **Interactive REPL (JLine) in the native binary**, especially RTL input and the cursor behaviour. Test with `target/arabicbasic --repl` in Terminal.app and in Windows Terminal / `cmd.exe`.
- **Windows console encoding.** Java encodes stdout with the console code page, so Arabic output may appear as `?` unless the console is UTF-8 (`chcp 65001`). The JAR has the same behaviour. CI therefore checks only exit codes and line counts on Windows.
- **Installers end to end** on real machines (CI installs the Windows setup silently and inspects the macOS `.pkg` payload, but does not click through the GUI).

## The workflow

`.github/workflows/release.yml` runs on a **pushed `v<semver>` tag whose commit is on `main`**, on **a published GitHub Release**, and on **manual dispatch** (`Actions > Release > Run workflow`).

1. `verify`: `mvn test`, and the release tag must equal `v<pom version>` (or a `-prerelease` suffix of it).
2. `native`: 3-runner matrix with `graalvm/setup-graalvm` (**GraalVM Community Edition**, free of redistribution restrictions), `mvn -Pnative package`, then a smoke test (the native output is compared with the JAR on macOS).
3. `package-macos`: `lipo` merge, `.pkg`, optional signing and notarization.
4. `package-windows`: Inno Setup installer, optional Authenticode signing, silent-install check.
5. `publish`: `sha256sums.txt`, then `gh release upload --clobber` to the release.

Manual runs with an empty `tag` only produce workflow artifacts. To attach to an existing release, give its `tag` and enable `upload`. To build a tagged release, check that tag out by passing it as `tag`.

Typical flow after `./mvnw -Prelease process-resources` and `git push --follow-tags`: the tag push starts the workflow, which creates the GitHub Release for the tag and attaches the installers a few minutes later. Pushing a tag whose commit is not on `main` fails the `verify` job.

## Signing

Both signing paths are optional. Without secrets, the workflow still produces installers, just unsigned.

### macOS (Apple Developer account, $99/year)

Installers need **two** certificates from the Apple Developer portal: *Developer ID Application* (signs the binary) and *Developer ID Installer* (signs the `.pkg`). Export each as a `.p12`, then add these **repository secrets**:

| Secret | Value |
|--------|-------|
| `APPLE_APP_CERT_P12_BASE64` | `base64 -i app.p12` |
| `APPLE_INSTALLER_CERT_P12_BASE64` | `base64 -i installer.p12` |
| `APPLE_CERT_PASSWORD` | password used when exporting both `.p12` files |
| `APPLE_APP_SIGN_IDENTITY` | e.g. `Developer ID Application: Your Name (TEAMID)` |
| `APPLE_INSTALLER_SIGN_IDENTITY` | e.g. `Developer ID Installer: Your Name (TEAMID)` |
| `APPLE_ID` | Apple ID email |
| `APPLE_TEAM_ID` | 10-character team ID |
| `APPLE_APP_PASSWORD` | app-specific password from appleid.apple.com |

With all of them set, `scripts/package-macos-pkg.sh` signs with the hardened runtime, signs the `.pkg`, submits it to `notarytool --wait`, and staples the ticket. Without them the binary is ad-hoc signed (required on Apple Silicon after `lipo`), and users must right-click > Open the downloaded `.pkg` once. If notarization rejects the binary, check whether it needs a hardened-runtime entitlement.

### Windows

Windows does **not** require signing: an unsigned installer runs, but SmartScreen shows "Windows protected your PC" (More info > Run anyway), and Smart App Control (off on most PCs) blocks unsigned programs entirely. SmartScreen reputation also builds slowly, even with a certificate.

**Current decision:** ship the Windows installer **unsigned, with SHA-256 checksums** (`sha256sums.txt` on every release, verified by users with `Get-FileHash`; see the README). This is free, and acceptable for an early project whose users can click "More info > Run anyway". **Backup plan:** switch to **Azure Artifact Signing** once there is funding.

Options considered:

- **SignPath Foundation**: free for open source, but they decline very young projects.
- **Azure Artifact Signing** (formerly Trusted Signing): about $10/month; individuals qualify only in the US and Canada. **Chosen backup.**
- **Certum Open Source certificate**: roughly EUR 50-70 per year.

The workflow already has a `signtool` path that activates when the secrets `WINDOWS_CERT_PFX_BASE64` and `WINDOWS_CERT_PASSWORD` exist. Azure Artifact Signing **cannot** use it: the key stays in Azure and there is no exportable `.pfx`. The same is true of most certificate authorities now, which issue keys on hardware tokens or cloud HSMs.

### Switching to Azure Artifact Signing

1. In Azure, create an **Artifact Signing account** and a **certificate profile**, and complete identity validation. Note the account's region **endpoint** (for example `https://eus.codesigning.azure.net/`).
2. Authenticate the workflow to Azure. OIDC (workload identity federation) avoids storing a client secret; see the action's [`docs/OIDC.md`](https://github.com/Azure/artifact-signing-action/blob/main/docs/OIDC.md). Grant the app registration the certificate-profile signer role on the signing account (the role was called *Trusted Signing Certificate Profile Signer* before the rename; confirm the current name in the Azure portal). Add the Azure identifiers the login step needs (typically `AZURE_TENANT_ID` and `AZURE_CLIENT_ID` as repository secrets, plus `AZURE_CLIENT_SECRET` only if not using OIDC).
3. In the `package-windows` job of `release.yml`, replace the two `signtool` sign steps ("Sign arabicbasic.exe" and "Sign installer") and the "Locate signtool" step with `azure/login` followed by `Azure/artifact-signing-action@v2`. Sign the binary **before** "Build installer", and the installer **after** it:
   ```yaml
   - uses: Azure/artifact-signing-action@v2
     with:
       endpoint: https://<region>.codesigning.azure.net/
       signing-account-name: <account>
       certificate-profile-name: <profile>
       files: ${{ github.workspace }}\native\arabicbasic.exe
       file-digest: SHA256
       timestamp-rfc3161: http://timestamp.acs.microsoft.com
       timestamp-digest: SHA256
   ```
   Then repeat it with `files: ${{ github.workspace }}\dist\ArabicBASIC-${{ needs.verify.outputs.version }}-windows-x64-setup.exe`. Check the action's README for the current inputs and the authentication step it expects; this was written from its `action.yml` and has not been run.
4. Set `AppPublisher` in `packaging/windows/arabicbasic.iss` (currently `Lisantra`) to match the certificate subject, so the publisher name SmartScreen shows is consistent.
5. Update the README Windows bullet and drop the "intentionally unsigned" wording. Keep `sha256sums.txt`.

Expect SmartScreen reputation to build slowly even after signing starts.

## Linux (follow-on)

Not built yet because of distro and package-manager diversity. Reasonable first steps, in order of effort:

1. A `ubuntu-latest` native build published as a `.tar.gz` (a runner matrix entry plus an upload).
2. A `.deb` (and maybe `.rpm`) wrapping the same binary.
3. A Homebrew tap, which also covers macOS, and optionally winget for Windows.
4. Flatpak or Snap only if demand justifies the sandboxing work.

Build on the oldest practical glibc (for example `ubuntu-22.04`) so the binary runs on more distros.

## Related reading

- Project root **README** / **AGENTS.md** for the normal **JAR** build and smoke tests.
- External: [GraalVM Native Build Tools Maven plugin](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html), [`graalvm/setup-graalvm`](https://github.com/graalvm/setup-graalvm), [Inno Setup](https://jrsoftware.org/ishelp/).
