package com.lisantra.arabicbasic.debug;

public final class UnicodeDebugFormatter {
  private UnicodeDebugFormatter() {}

  public static String toMultilineText(UnicodeDebugInfo info) {
    StringBuilder out = new StringBuilder();
    out.append("Input: ").append(String.valueOf(info.input())).append('\n');
    out.append("Contains non-ASCII Unicode characters: ")
        .append(info.containsNonAscii())
        .append('\n');
    out.append("Encoding used for bytes: ").append(info.charsetName()).append('\n');
    if (info.detectedCharsetName() != null) {
      out.append("Detected charset: ").append(info.detectedCharsetName()).append('\n');
    }
    if (info.detectionConfidence() != null) {
      out.append("Detection confidence: ").append(info.detectionConfidence()).append('\n');
    }
    out.append('\n');

    out.append("Per-character breakdown:").append('\n');
    for (UnicodeCharInfo ch : info.chars()) {
      out.append('[')
          .append(ch.codePointIndex())
          .append("] char='")
          .append(ch.glyph())
          .append("' codepoint=")
          .append(ch.codePointHex())
          .append(" bytes=")
          .append(ch.encodedBytesHex())
          .append(" block=")
          .append(ch.unicodeBlock())
          .append(" name=")
          .append(ch.unicodeName())
          .append('\n');
    }

    out.append('\n').append("Whole-string bytes:").append('\n');
    byte[] allBytes = info.fullEncodedBytes();
    for (int i = 0; i < allBytes.length; i++) {
      int value = allBytes[i] & 0xFF;
      out.append("byte[")
          .append(i)
          .append("] = 0x")
          .append("%02X".formatted(value))
          .append(" (")
          .append(value)
          .append(')')
          .append('\n');
    }
    return out.toString().trim();
  }
}
