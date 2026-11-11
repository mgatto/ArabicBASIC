#!/bin/sh
# package-macos-pkg.sh -- build the macOS installer (.pkg) from native binaries.
#
#   scripts/package-macos-pkg.sh <version> <output.pkg> <binary> [<binary>...]
#
# Pass one binary per CPU architecture (e.g. arm64 and x86_64). With more than one,
# they are merged into a single universal binary with lipo. The package installs:
#   /usr/local/lib/arabicbasic/arabicbasic        the executable
#   /usr/local/bin/arabicbasic                    symlink, so `arabicbasic` works in Terminal
#
# Signing is optional and controlled by environment variables (CI sets them from secrets):
#   APPLE_APP_SIGN_IDENTITY        "Developer ID Application: Name (TEAMID)"  (binary)
#   APPLE_INSTALLER_SIGN_IDENTITY  "Developer ID Installer: Name (TEAMID)"    (.pkg)
#   APPLE_ID, APPLE_TEAM_ID, APPLE_APP_PASSWORD   notarytool credentials (app-specific password)
#
# Without APPLE_APP_SIGN_IDENTITY the binary is ad-hoc signed ("codesign --sign -"). That gives
# a valid signature (required on Apple Silicon after lipo) but does NOT satisfy Gatekeeper: a
# downloaded unsigned .pkg needs right-click > Open the first time. Only a Developer ID
# signature plus notarization removes that prompt.
set -eu

if [ "$#" -lt 3 ]; then
    echo "usage: $0 <version> <output.pkg> <binary> [<binary>...]" >&2
    exit 2
fi

version=$1
out=$2
shift 2

for tool in pkgbuild codesign lipo; do
    command -v "$tool" >/dev/null || { echo "$0: $tool not found" >&2; exit 1; }
done
for bin in "$@"; do
    [ -f "$bin" ] || { echo "$0: binary not found: $bin" >&2; exit 1; }
done

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT

root="$work/root"
mkdir -p "$root/usr/local/lib/arabicbasic" "$root/usr/local/bin"
exe="$root/usr/local/lib/arabicbasic/arabicbasic"

if [ "$#" -gt 1 ]; then
    lipo -create "$@" -output "$exe"
else
    cp "$1" "$exe"
fi
chmod 755 "$exe"
lipo -info "$exe"

# Re-sign unconditionally: lipo (and cp) invalidate the signature native-image applied.
if [ -n "${APPLE_APP_SIGN_IDENTITY:-}" ]; then
    codesign --force --options runtime --timestamp --sign "$APPLE_APP_SIGN_IDENTITY" "$exe"
else
    codesign --force --sign - "$exe"
fi
codesign --verify --verbose=2 "$exe"

# Relative symlink so the layout survives a non-default install root.
ln -s ../lib/arabicbasic/arabicbasic "$root/usr/local/bin/arabicbasic"

set -- --root "$root" \
    --identifier com.lisantra.arabicbasic \
    --version "$version" \
    --install-location /
if [ -n "${APPLE_INSTALLER_SIGN_IDENTITY:-}" ]; then
    set -- "$@" --sign "$APPLE_INSTALLER_SIGN_IDENTITY" --timestamp
fi
mkdir -p "$(dirname "$out")"
pkgbuild "$@" "$out"

if [ -n "${APPLE_ID:-}" ] && [ -n "${APPLE_TEAM_ID:-}" ] && [ -n "${APPLE_APP_PASSWORD:-}" ]; then
    if [ -z "${APPLE_INSTALLER_SIGN_IDENTITY:-}" ]; then
        echo "$0: notarization requires APPLE_INSTALLER_SIGN_IDENTITY" >&2
        exit 1
    fi
    xcrun notarytool submit "$out" \
        --apple-id "$APPLE_ID" --team-id "$APPLE_TEAM_ID" --password "$APPLE_APP_PASSWORD" --wait
    xcrun stapler staple "$out"
fi

echo "$0: package ready: $out"
