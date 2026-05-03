package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.jupiter.api.Test;

/**
 * Ensures {@link ArabicBasicRuntimeException} carries a {@link DeclarationSite} aligned with
 * ANTLR token positions for simple failing programs.
 */
class ArabicBasicDeclarationSiteTest {

  private static Locale arabicLocale() {
    return new Locale.Builder()
        .setLanguage("ar")
        .setExtension(Locale.UNICODE_LOCALE_EXTENSION, "nu-arab")
        .build();
  }

  private static ArabicBasicRuntimeException interpretExpectingFailure(String source) {
    Map<String, Variable> globalScope = new LinkedHashMap<>();
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(source, "<test>"));
    ArabicBASIC parser = new ArabicBASIC(new CommonTokenStream(lexer));
    InterpreterVisitor visitor =
        new InterpreterVisitor(arabicLocale(), Locale.ENGLISH, globalScope, false);
    return assertThrows(
        ArabicBasicRuntimeException.class, () -> visitor.visit(parser.program()), source);
  }

  /**
   * First token on {@code line} whose text equals {@code tokenText} (useful for IDENTIFIER or
   * single-char operators).
   */
  private static DeclarationSite firstTokenSite(String source, int line, String tokenText) {
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(source, "<test>"));
    Token t;
    while ((t = lexer.nextToken()).getType() != Token.EOF) {
      if (t.getLine() == line && tokenText.equals(t.getText())) {
        return DeclarationSite.from(t);
      }
    }
    throw new AssertionError(
        "No token with text '" + tokenText + "' on line " + line + " in:\n" + source);
  }

  @Test
  void unknownVariableInPrint_reportsIdentifierTokenSite() {
    String source =
        """
        صار ا = ١
        اطبع ب
        """;
    ArabicBasicRuntimeException ex = interpretExpectingFailure(source);
    DeclarationSite site = ex.getDeclarationSite();
    assertFalse(site.isUnknown(), "site should be known: " + ex.getMessage());
    assertEquals(2, site.line());
    DeclarationSite expected = firstTokenSite(source, 2, "ب");
    assertEquals(expected.line(), site.line());
    assertEquals(expected.charPositionInLine(), site.charPositionInLine());
    assertTrue(ex.getMessage().contains("line 2,"), () -> "message: " + ex.getMessage());
  }

  @Test
  void divideByZero_reportsOperatorTokenSite() {
    String source = "صار ا = ١ / ٠\n";
    ArabicBasicRuntimeException ex = interpretExpectingFailure(source);
    DeclarationSite site = ex.getDeclarationSite();
    assertFalse(site.isUnknown());
    assertEquals(1, site.line());
    DeclarationSite expected = firstTokenSite(source, 1, "/");
    assertEquals(expected.charPositionInLine(), site.charPositionInLine());
    assertTrue(ex.getMessage().contains("line 1,"), () -> "message: " + ex.getMessage());
  }

  @Test
  void logZero_reportsMathCallStartSite() {
    String source = "صار ا = LOG(٠)\n";
    ArabicBasicRuntimeException ex = interpretExpectingFailure(source);
    DeclarationSite site = ex.getDeclarationSite();
    assertFalse(site.isUnknown());
    assertEquals(1, site.line());
    // Interpreter uses DeclarationSite.from(MathFunctionContext); first token is the builtin name.
    DeclarationSite expected = firstTokenSite(source, 1, "LOG");
    assertEquals(expected.charPositionInLine(), site.charPositionInLine());
    assertTrue(ex.getMessage().contains("LOG"), () -> "message: " + ex.getMessage());
  }

  @Test
  void unknownVariable_prefixInArabic_whenMessageLocaleIsArabic() {
    String source =
        """
        صار ا = ١
        اطبع ب
        """;
    Map<String, Variable> globalScope = new LinkedHashMap<>();
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(source, "<test>"));
    ArabicBASIC parser = new ArabicBASIC(new CommonTokenStream(lexer));
    InterpreterVisitor visitor =
        new InterpreterVisitor(arabicLocale(), Locale.forLanguageTag("ar"), globalScope, false);
    ArabicBasicRuntimeException ex =
        assertThrows(
            ArabicBasicRuntimeException.class, () -> visitor.visit(parser.program()), source);
    assertTrue(
        ex.getMessage().contains("السطر"),
        () -> "expected Arabic source prefix in: " + ex.getMessage());
  }

  @Test
  void forNextVariableMismatch_reportsNextVariableSite() {
    String source =
        """
        لكل س = ١ حتى ٣
        اطبع س
        ص التالي
        """;
    ArabicBasicRuntimeException ex = interpretExpectingFailure(source);
    DeclarationSite site = ex.getDeclarationSite();
    assertFalse(site.isUnknown(), "site should be known: " + ex.getMessage());
    DeclarationSite expected = firstTokenSite(source, 3, "ص");
    assertEquals(expected.line(), site.line());
    assertEquals(expected.charPositionInLine(), site.charPositionInLine());
    assertTrue(ex.getMessage().contains("NEXT variable"), () -> "message: " + ex.getMessage());
  }

  @Test
  void notAnArray_reportsLastWriteSiteHint() {
    String source =
        """
        صار س = ١
        س[٠] = ٢
        """;
    ArabicBasicRuntimeException ex = interpretExpectingFailure(source);
    DeclarationSite site = ex.getDeclarationSite();
    assertFalse(site.isUnknown(), "site should be known: " + ex.getMessage());
    DeclarationSite expected = firstTokenSite(source, 2, "س");
    assertEquals(expected.line(), site.line());
    assertEquals(expected.charPositionInLine(), site.charPositionInLine());
    assertTrue(ex.getMessage().contains("is not an array"), () -> "message: " + ex.getMessage());
    assertTrue(
        ex.getMessage().contains("Last written at line 1, column"),
        () -> "message: " + ex.getMessage());
  }
}
