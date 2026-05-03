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

/** Type rules and localized errors for {@link InterpreterVisitor#visitComparitiveBoolean}. */
class ComparisonTypingTest {

  private static Locale arabicLocale() {
    return new Locale.Builder()
        .setLanguage("ar")
        .setExtension(Locale.UNICODE_LOCALE_EXTENSION, "nu-arab")
        .build();
  }

  private static void interpret(String source, Locale messageLocale) {
    Map<String, Variable> globalScope = new LinkedHashMap<>();
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(source, "<test>"));
    ArabicBASIC parser = new ArabicBASIC(new CommonTokenStream(lexer));
    InterpreterVisitor visitor =
        new InterpreterVisitor(arabicLocale(), messageLocale, globalScope, false);
    visitor.visit(parser.program());
  }

  private static ArabicBasicRuntimeException interpretFailure(String source, Locale messageLocale) {
    Map<String, Variable> globalScope = new LinkedHashMap<>();
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(source, "<test>"));
    ArabicBASIC parser = new ArabicBASIC(new CommonTokenStream(lexer));
    InterpreterVisitor visitor =
        new InterpreterVisitor(arabicLocale(), messageLocale, globalScope, false);
    return assertThrows(
        ArabicBasicRuntimeException.class, () -> visitor.visit(parser.program()), source);
  }

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

  /** When several tokens on one line match, return the last (e.g. outer {@code >} in chained look). */
  private static DeclarationSite lastTokenSiteOnLine(String source, int line, String tokenText) {
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(source, "<test>"));
    DeclarationSite last = null;
    Token t;
    while ((t = lexer.nextToken()).getType() != Token.EOF) {
      if (t.getLine() == line && tokenText.equals(t.getText())) {
        last = DeclarationSite.from(t);
      }
    }
    if (last == null) {
      throw new AssertionError(
          "No token with text '" + tokenText + "' on line " + line + " in:\n" + source);
    }
    return last;
  }

  @Test
  void numericOrdering_succeeds() {
    String source =
        """
        صار ا = ٣
        صار ب = ١
        اذا ا > ب ثم
        اطبع "ok"
        ختام اذا
        """;
    interpret(source, Locale.ENGLISH);
  }

  @Test
  void stringOrdering_reportsOperatorSiteAndEnglishMessage() {
    String source =
        """
        صار س = "أ"
        صار ت = "ب"
        اذا س > ت ثم
        اطبع "x"
        ختام اذا
        """;
    ArabicBasicRuntimeException ex = interpretFailure(source, Locale.ENGLISH);
    assertTrue(ex.getMessage().contains("Ordering comparisons"), ex.getMessage());
    DeclarationSite site = ex.getDeclarationSite();
    assertFalse(site.isUnknown(), "site: " + site);
    DeclarationSite expected = firstTokenSite(source, 3, ">");
    assertEquals(expected.line(), site.line());
    assertEquals(expected.charPositionInLine(), site.charPositionInLine());
  }

  @Test
  void stringEquality_succeeds() {
    String source =
        """
        صار س = "x"
        صار ت = "x"
        اذا س = ت ثم
        اطبع "ok"
        ختام اذا
        """;
    interpret(source, Locale.ENGLISH);
  }

  @Test
  void stringNumberEquality_reportsEnglishMessage() {
    String source =
        """
        صار س = "x"
        صار ا = ١
        اذا س = ا ثم
        اطبع "x"
        ختام اذا
        """;
    ArabicBasicRuntimeException ex = interpretFailure(source, Locale.ENGLISH);
    assertTrue(ex.getMessage().contains("string to a number"), ex.getMessage());
    DeclarationSite expected = firstTokenSite(source, 3, "=");
    assertEquals(expected.charPositionInLine(), ex.getDeclarationSite().charPositionInLine());
  }

  @Test
  void arrayInComparison_reportsUnsupportedType() {
    String source =
        """
        صار م = مصفوفة(2)
        صار ا = ١
        اذا م = م ثم
        اطبع "x"
        ختام اذا
        """;
    ArabicBasicRuntimeException ex = interpretFailure(source, Locale.ENGLISH);
    assertTrue(ex.getMessage().contains("cannot be used in a comparison"), ex.getMessage());
    assertTrue(ex.getMessage().contains("Array"), ex.getMessage());
  }

  @Test
  void functionInComparison_reportsUnsupportedType() {
    String source =
        """
        دالّة ف(س) = س^2
        صار ا = ١
        اذا ف = ف ثم
        اطبع "x"
        ختام اذا
        """;
    ArabicBasicRuntimeException ex = interpretFailure(source, Locale.ENGLISH);
    assertTrue(ex.getMessage().contains("cannot be used in a comparison"), ex.getMessage());
    assertTrue(ex.getMessage().contains("Function"), ex.getMessage());
  }

  @Test
  void nestedBooleanAsOperand_reportsNotValue() {
    String source =
        """
        صار ا = ١
        صار ب = ٢
        اذا ( ا > ب ) > ب ثم
        اطبع "x"
        ختام اذا
        """;
    ArabicBasicRuntimeException ex = interpretFailure(source, Locale.ENGLISH);
    assertTrue(
        ex.getMessage().contains("boolean subexpression")
            || ex.getMessage().contains("Comparison operands"),
        ex.getMessage());
    DeclarationSite expected = lastTokenSiteOnLine(source, 3, ">");
    assertEquals(expected.line(), ex.getDeclarationSite().line());
    assertEquals(expected.charPositionInLine(), ex.getDeclarationSite().charPositionInLine());
  }
}
