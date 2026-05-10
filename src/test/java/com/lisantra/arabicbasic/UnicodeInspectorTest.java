package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lisantra.arabicbasic.debug.UnicodeDebugInfo;
import com.lisantra.arabicbasic.debug.UnicodeInspector;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class UnicodeInspectorTest {

  @Test
  void inspectArabicInput_hasUnicodeNamesAndBlocks() {
    UnicodeDebugInfo info = UnicodeInspector.inspect("اب");

    assertTrue(info.containsNonAscii());
    assertEquals(2, info.chars().size());
    assertEquals("U+0627", info.chars().getFirst().codePointHex());
    assertTrue(info.chars().getFirst().unicodeName().contains("ARABIC"));
    assertEquals("ARABIC", info.chars().getFirst().unicodeBlock());
  }

  @Test
  void inspectAsciiInput_marksAsciiOnly() {
    UnicodeDebugInfo info = UnicodeInspector.inspect("abc");

    assertFalse(info.containsNonAscii());
    assertEquals(3, info.chars().size());
    assertEquals("LATIN SMALL LETTER A", info.chars().get(0).unicodeName());
  }

  @Test
  void inspectBytesAutoDetect_populatesDetectionMetadata() {
    byte[] raw = "مرحبا".getBytes(StandardCharsets.UTF_8);

    UnicodeDebugInfo info = UnicodeInspector.inspect(raw);

    assertNotNull(info.detectedCharsetName());
    assertNotNull(info.detectionConfidence());
    assertTrue(info.chars().size() >= 2);
  }
}
