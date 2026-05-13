package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ReplSyntaxHighlighterTest {

  @Test
  void detectsArabicKeywordTokens() {
    List<ReplSyntaxHighlighter.TokenSpan> spans =
        ReplSyntaxHighlighter.detectTokenSpans("صار ن = ٦");

    assertTrue(
        spans.stream().anyMatch(s -> s.type() == ReplSyntaxHighlighter.TokenType.KEYWORD),
        () -> "spans: " + spans);
  }

  @Test
  void detectsNumberTokensIncludingArabicIndicDigits() {
    List<ReplSyntaxHighlighter.TokenSpan> spans =
        ReplSyntaxHighlighter.detectTokenSpans("اطبع ١٢٣");

    assertTrue(
        spans.stream().anyMatch(s -> s.type() == ReplSyntaxHighlighter.TokenType.NUMBER),
        () -> "spans: " + spans);
  }

  @Test
  void detectsStringTokens() {
    List<ReplSyntaxHighlighter.TokenSpan> spans =
        ReplSyntaxHighlighter.detectTokenSpans("اطبع \"مرحبا\"");

    assertTrue(
        spans.stream().anyMatch(s -> s.type() == ReplSyntaxHighlighter.TokenType.STRING),
        () -> "spans: " + spans);
  }

  @Test
  void detectsCommandTokens() {
    List<ReplSyntaxHighlighter.TokenSpan> spans = ReplSyntaxHighlighter.detectTokenSpans(":help");

    assertEquals(1, spans.size(), () -> "spans: " + spans);
    assertEquals(ReplSyntaxHighlighter.TokenType.COMMAND, spans.getFirst().type());
  }
}
