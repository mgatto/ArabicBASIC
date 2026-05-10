package com.lisantra.arabicbasic.debug;

import java.util.List;

public record UnicodeDebugInfo(
    String input,
    String charsetName,
    String detectedCharsetName,
    Integer detectionConfidence,
    boolean containsNonAscii,
    byte[] fullEncodedBytes,
    List<UnicodeCharInfo> chars) {}
