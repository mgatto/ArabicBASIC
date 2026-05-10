package com.lisantra.arabicbasic.debug;

public final class JdkUnicodeProvider implements UnicodeMetadataProvider {
  public static final JdkUnicodeProvider INSTANCE = new JdkUnicodeProvider();

  private JdkUnicodeProvider() {}

  @Override
  public String unicodeName(int codePoint) {
    String name = Character.getName(codePoint);
    if (name == null || name.isBlank()) {
      return "<no unicode name>";
    }
    return name;
  }

  @Override
  public String unicodeBlock(int codePoint) {
    Character.UnicodeBlock block = Character.UnicodeBlock.of(codePoint);
    if (block == null) {
      return "<no unicode block>";
    }
    return block.toString();
  }

  @Override
  public String providerId() {
    return "jdk";
  }
}
