package com.lisantra.arabicbasic.debug;

public interface UnicodeMetadataProvider {
  String unicodeName(int codePoint);

  String unicodeBlock(int codePoint);

  String providerId();
}
