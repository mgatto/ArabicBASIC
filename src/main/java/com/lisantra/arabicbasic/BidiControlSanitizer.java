package com.lisantra.arabicbasic;

final class BidiControlSanitizer {
  private BidiControlSanitizer() {}

  static String stripDirectionalMarks(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    StringBuilder sb = new StringBuilder(input.length());
    boolean inString = false;
    boolean escaping = false;

    for (int i = 0; i < input.length(); i++) {
      char ch = input.charAt(i);

      if (inString) {
        sb.append(ch);
        if (escaping) {
          escaping = false;
        } else if (ch == '\\') {
          escaping = true;
        } else if (ch == '"') {
          inString = false;
        }
        continue;
      }

      if (ch == '"') {
        inString = true;
        sb.append(ch);
        continue;
      }

      if (isDirectionalMark(ch)) {
        continue;
      }
      sb.append(ch);
    }

    return sb.toString();
  }

  private static boolean isDirectionalMark(char ch) {
    return switch (ch) {
      case '\u200E', '\u200F', '\u061C', '\u202A', '\u202B', '\u202C', '\u202D', '\u202E', '\u2066',
          '\u2067', '\u2068', '\u2069' -> true;
      default -> false;
    };
  }
}
