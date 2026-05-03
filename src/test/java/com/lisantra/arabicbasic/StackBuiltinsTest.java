package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

class StackBuiltinsTest {

  private static Locale arabicLocale() {
    return new Locale.Builder()
        .setLanguage("ar")
        .setExtension(Locale.UNICODE_LOCALE_EXTENSION, "nu-arab")
        .build();
  }

  private static Map<String, Variable> interpret(String source) {
    Map<String, Variable> globalScope = new LinkedHashMap<>();
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(source, "<test>"));
    ArabicBASIC parser = new ArabicBASIC(new CommonTokenStream(lexer));
    InterpreterVisitor visitor =
        new InterpreterVisitor(arabicLocale(), Locale.ENGLISH, globalScope, false);
    visitor.visit(parser.program());
    return globalScope;
  }

  private static ArabicBasicRuntimeException interpretFailure(String source) {
    return assertThrows(
        ArabicBasicRuntimeException.class,
        () -> {
          interpret(source);
        });
  }

  @Test
  void stackBuiltins_followLifo_andTrackEmptyAsBoolean() {
    String source =
        """
        صار س = مكدس()
        صار ن١ = ادفع(س, ١)
        صار ن٢ = ادفع(س, ٢)
        صار ق = انظر(س)
        صار خ١ = اسحب(س)
        صار خ٢ = اسحب(س)
        صار خ٣ = اسحب(س)
        صار ف = فارغ؟(س)
        """;

    Map<String, Variable> scope = interpret(source);

    assertEquals("Stack", scope.get("س").getValue().getOriginalType());
    assertEquals(1, ((Double) scope.get("ن١").getValue().getVal()).intValue());
    assertEquals(2, ((Double) scope.get("ن٢").getValue().getVal()).intValue());
    assertEquals(2, ((Double) scope.get("ق").getValue().getVal()).intValue());
    assertEquals(2, ((Double) scope.get("خ١").getValue().getVal()).intValue());
    assertEquals(1, ((Double) scope.get("خ٢").getValue().getVal()).intValue());
    assertNull(scope.get("خ٣").getValue().getVal());
    assertEquals("Boolean", scope.get("ف").getValue().getOriginalType());
    assertTrue((Boolean) scope.get("ف").getValue().getVal());
  }

  @Test
  void stackBuiltins_errorWhenFirstArgIsNotStack() {
    String source =
        """
        صار ا = ١
        صار ب = ادفع(ا, ٢)
        """;

    ArabicBasicRuntimeException ex = interpretFailure(source);
    assertTrue(ex.getMessage().contains("not a stack"), ex.getMessage());
  }

  @Test
  void booleanLiteralAndPredicateWorkInConditions() {
    String source =
        """
        صار س = مكدس()
        صار أ = ٠
        اذا فارغ؟(س) ثم
        صار أ = ١
        ختام اذا
        اذا خطأ ثم
        صار أ = ٢
        ختام اذا
        """;

    Map<String, Variable> scope = interpret(source);
    assertEquals(1, ((Double) scope.get("أ").getValue().getVal()).intValue());
  }

  @Test
  void arabicComma_isAcceptedInFunctionArguments() {
    String source =
        """
        صار س = مكدس()
        صار ن = ادفع(س، ٧)
        صار ع = LEFT("سلام"، ٢)
        صار ف = فارغ؟(س)
        """;

    Map<String, Variable> scope = interpret(source);
    assertEquals(1, ((Double) scope.get("ن").getValue().getVal()).intValue());
    assertEquals("سل", scope.get("ع").getValue().getVal());
    assertEquals("Boolean", scope.get("ف").getValue().getOriginalType());
    assertFalse((Boolean) scope.get("ف").getValue().getVal());
  }
}
