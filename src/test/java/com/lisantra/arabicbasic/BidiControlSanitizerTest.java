package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BidiControlSanitizerTest {
  @Test
  void stripsDirectionalMarksOutsideStrings() {
    String input = "صار ن \u200F=\u200E ٦";

    assertEquals("صار ن = ٦", BidiControlSanitizer.stripDirectionalMarks(input));
  }

  @Test
  void preservesDirectionalMarksInsideStringLiterals() {
    String input = "صار س \u200F=\u200E \"أ\u200Fب\"";

    assertEquals("صار س = \"أ\u200Fب\"", BidiControlSanitizer.stripDirectionalMarks(input));
  }
}
