package com.lisantra.arabicbasic.debug;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class UnicodeInspector {
  private static final Map<String, String> CHARSET_ALIAS_MAP = buildCharsetAliasMap();

  private UnicodeInspector() {}

  public static UnicodeDebugInfo inspect(String input) {
    return inspect(input, StandardCharsets.UTF_8);
  }

  public static UnicodeDebugInfo inspect(String input, Charset charset) {
    return inspect(input, charset, JdkUnicodeProvider.INSTANCE);
  }

  public static UnicodeDebugInfo inspect(
      String input, Charset charset, UnicodeMetadataProvider provider) {
    String safeInput = input == null ? "" : input;
    Charset safeCharset = charset == null ? StandardCharsets.UTF_8 : charset;
    UnicodeMetadataProvider safeProvider =
        provider == null ? JdkUnicodeProvider.INSTANCE : provider;

    byte[] fullEncodedBytes = safeInput.getBytes(safeCharset);
    List<UnicodeCharInfo> chars = inspectCodePoints(safeInput, safeCharset, safeProvider);
    boolean containsNonAscii = safeInput.codePoints().anyMatch(cp -> cp > 0x7F);

    return new UnicodeDebugInfo(
        safeInput,
        safeCharset.name(),
        null,
        null,
        containsNonAscii,
        fullEncodedBytes,
        List.copyOf(chars));
  }

  public static UnicodeDebugInfo inspect(byte[] rawBytes) {
    byte[] safeBytes = rawBytes == null ? new byte[0] : rawBytes;
    Detection detection = detectCharset(safeBytes);
    Charset resolved = detection.resolvedCharset();
    String decoded = new String(safeBytes, resolved);
    UnicodeDebugInfo base = inspect(decoded, resolved);
    return new UnicodeDebugInfo(
        base.input(),
        base.charsetName(),
        detection.detectedLabel(),
        detection.confidence(),
        base.containsNonAscii(),
        Arrays.copyOf(safeBytes, safeBytes.length),
        base.chars());
  }

  public static UnicodeDebugInfo inspect(byte[] rawBytes, Charset charset) {
    byte[] safeBytes = rawBytes == null ? new byte[0] : rawBytes;
    Charset safeCharset = charset == null ? StandardCharsets.UTF_8 : charset;
    String decoded = new String(safeBytes, safeCharset);
    UnicodeDebugInfo base = inspect(decoded, safeCharset);
    return new UnicodeDebugInfo(
        base.input(),
        base.charsetName(),
        safeCharset.name(),
        100,
        base.containsNonAscii(),
        Arrays.copyOf(safeBytes, safeBytes.length),
        base.chars());
  }

  private static List<UnicodeCharInfo> inspectCodePoints(
      String input, Charset charset, UnicodeMetadataProvider provider) {
    List<UnicodeCharInfo> chars = new ArrayList<>();
    int index = 0;
    for (int offset = 0; offset < input.length(); ) {
      int codePoint = input.codePointAt(offset);
      String glyph = new String(Character.toChars(codePoint));
      byte[] encoded = glyph.getBytes(charset);
      String unicodeName = provider.unicodeName(codePoint);
      String unicodeBlock = provider.unicodeBlock(codePoint);

      if (codePoint == 0xFFFD) {
        unicodeName = "<unknown unicode name>";
        unicodeBlock = "<unknown unicode block>";
      }

      chars.add(
          new UnicodeCharInfo(
              index,
              glyph,
              codePoint,
              "U+%04X".formatted(codePoint),
              unicodeName,
              unicodeBlock,
              encoded,
              toHex(encoded),
              null));

      index++;
      offset += Character.charCount(codePoint);
    }
    return chars;
  }

  private static Detection detectCharset(byte[] rawBytes) {
    CharsetDetector detector = new CharsetDetector();
    detector.setText(rawBytes);
    CharsetMatch match = detector.detect();
    if (match == null) {
      Charset fallback = StandardCharsets.UTF_8;
      return new Detection(fallback.name(), fallback, null);
    }

    String detected = match.getName();
    Charset resolved = resolveCharset(detected).orElse(StandardCharsets.UTF_8);
    return new Detection(detected, resolved, match.getConfidence());
  }

  private static java.util.Optional<Charset> resolveCharset(String inputName) {
    if (inputName == null || inputName.isBlank()) {
      return java.util.Optional.empty();
    }

    String key = normalizeKey(inputName);
    String mapped = CHARSET_ALIAS_MAP.getOrDefault(key, inputName);
    try {
      return java.util.Optional.of(Charset.forName(mapped));
    } catch (Exception ignored) {
      return java.util.Optional.empty();
    }
  }

  private static String toHex(byte[] bytes) {
    if (bytes.length == 0) {
      return "";
    }
    StringBuilder builder = new StringBuilder(bytes.length * 3);
    for (int i = 0; i < bytes.length; i++) {
      if (i > 0) {
        builder.append(' ');
      }
      builder.append("%02X".formatted(bytes[i] & 0xFF));
    }
    return builder.toString();
  }

  private static Map<String, String> buildCharsetAliasMap() {
    Map<String, String> aliases = new LinkedHashMap<>();
    aliases.put(normalizeKey("cp1256"), "windows-1256");
    aliases.put(normalizeKey("windows-1256"), "windows-1256");
    aliases.put(normalizeKey("ibm864"), "IBM864");
    aliases.put(normalizeKey("cp864"), "IBM864");
    aliases.put(normalizeKey("cp720"), "windows-1256");
    aliases.put(normalizeKey("iso-8859-6-i"), "ISO-8859-6");
    aliases.put(normalizeKey("iso-8859-6-e"), "ISO-8859-6");
    return aliases;
  }

  private static String normalizeKey(String name) {
    return name.toLowerCase(Locale.ROOT).replace("_", "-").trim();
  }

  private record Detection(String detectedLabel, Charset resolvedCharset, Integer confidence) {}
}
