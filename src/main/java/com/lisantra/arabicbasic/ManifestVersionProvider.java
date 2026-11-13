package com.lisantra.arabicbasic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import picocli.CommandLine;

/**
 * Supplies {@code --version} from {@code arabicbasic-version.properties}, a classpath resource that
 * Maven filters with {@code ${project.version}}. A resource file (rather than the JAR manifest's
 * {@code Implementation-Version}) also works inside a GraalVM native executable, which has no
 * manifest. When the resource is absent or unfiltered (e.g. some IDE runs), a fallback string is
 * used.
 */
public final class ManifestVersionProvider implements CommandLine.IVersionProvider {

  private static final String VERSION_RESOURCE = "arabicbasic-version.properties";

  @Override
  public String[] getVersion() {
    String v = readResourceVersion();
    if (v == null || v.isEmpty() || v.startsWith("${")) {
      v = "development";
    }
    return new String[] {v};
  }

  private static String readResourceVersion() {
    try (InputStream in =
        ManifestVersionProvider.class.getClassLoader().getResourceAsStream(VERSION_RESOURCE)) {
      if (in == null) {
        return null;
      }
      Properties props = new Properties();
      props.load(in);
      return props.getProperty("version");
    } catch (IOException e) {
      return null;
    }
  }
}
