package com.lisantra.arabicbasic.debug;

public record UnicodeCharInfo(
    int codePointIndex,
    String glyph,
    int codePoint,
    String codePointHex,
    String unicodeName,
    String unicodeBlock,
    byte[] encodedBytes,
    String encodedBytesHex,
    Integer sourceByteOffset) {}
