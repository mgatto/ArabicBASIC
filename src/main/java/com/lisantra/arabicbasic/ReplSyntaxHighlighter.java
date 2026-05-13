package com.lisantra.arabicbasic;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.jline.reader.LineReader;
import org.jline.reader.impl.DefaultHighlighter;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lexer-driven token highlighter for the REPL (ANTLR lexer only, no parser pass).
 */
final class ReplSyntaxHighlighter extends DefaultHighlighter {
  private static final Pattern COMMAND_PATTERN = Pattern.compile("^\\s*:[A-Za-z]+");

  @Override
  public AttributedString highlight(LineReader reader, String buffer) {
    if (buffer == null || buffer.isEmpty()) {
      return AttributedString.EMPTY;
    }

    List<TokenSpan> spans = detectTokenSpans(buffer);
    if (spans.isEmpty()) {
      return new AttributedString(buffer);
    }

    spans.sort(Comparator.comparingInt(TokenSpan::start));
    AttributedStringBuilder builder = new AttributedStringBuilder(buffer.length());
    int cursor = 0;
    for (TokenSpan span : spans) {
      if (span.start() > cursor) {
        builder.append(buffer.substring(cursor, span.start()));
      }
      builder.style(styleFor(span.type()));
      builder.append(buffer.substring(span.start(), span.end()));
      builder.style(AttributedStyle.DEFAULT);
      cursor = span.end();
    }
    if (cursor < buffer.length()) {
      builder.append(buffer.substring(cursor));
    }
    return builder.toAttributedString();
  }

  static List<TokenSpan> detectTokenSpans(String buffer) {
    List<TokenSpan> spans = new ArrayList<>();
    addReplCommandSpan(spans, buffer);
    addLexerTokenSpans(spans, buffer);
    return removeOverlaps(spans);
  }

  private static void addReplCommandSpan(List<TokenSpan> spans, String buffer) {
    Matcher commandMatcher = COMMAND_PATTERN.matcher(buffer);
    if (commandMatcher.find()) {
      spans.add(new TokenSpan(commandMatcher.start(), commandMatcher.end(), TokenType.COMMAND));
    }
  }

  private static void addLexerTokenSpans(List<TokenSpan> spans, String buffer) {
    ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromString(buffer));
    lexer.removeErrorListeners();
    for (Token token : lexer.getAllTokens()) {
      TokenType tokenType = classifyToken(token);
      if (tokenType == null) {
        continue;
      }
      int start = token.getStartIndex();
      int stopExclusive = token.getStopIndex() + 1;
      if (start >= 0 && stopExclusive > start && stopExclusive <= buffer.length()) {
        spans.add(new TokenSpan(start, stopExclusive, tokenType));
      }
    }
  }

  private static TokenType classifyToken(Token token) {
    int type = token.getType();
    if (type == Token.EOF) {
      return null;
    }
    if (type == ArabicBASICLexer.STRING) {
      return TokenType.STRING;
    }
    if (type == ArabicBASICLexer.INTEGER || type == ArabicBASICLexer.REAL) {
      return TokenType.NUMBER;
    }
    if (type == ArabicBASICLexer.COMMENT) {
      return TokenType.COMMENT;
    }

    String symbolic = ArabicBASICLexer.VOCABULARY.getSymbolicName(type);
    if (symbolic == null) {
      return null;
    }

    if (symbolic.contains("_KW")
        || symbolic.endsWith("_FN")
        || symbolic.startsWith("BOOL_")
        || "NEXT_ADJ".equals(symbolic)
        || "STACK_EMPTY_PRED".equals(symbolic)) {
      return TokenType.KEYWORD;
    }
    return null;
  }

  private static List<TokenSpan> removeOverlaps(List<TokenSpan> spans) {
    if (spans.isEmpty()) {
      return spans;
    }
    spans.sort(Comparator.comparingInt(TokenSpan::start).thenComparingInt(TokenSpan::end));
    List<TokenSpan> filtered = new ArrayList<>();
    int previousEnd = -1;
    for (TokenSpan span : spans) {
      if (span.start() >= previousEnd) {
        filtered.add(span);
        previousEnd = span.end();
      }
    }
    return filtered;
  }

  private static AttributedStyle styleFor(TokenType tokenType) {
    return switch (tokenType) {
      case COMMAND -> AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW);
      case KEYWORD -> AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN);
      case NUMBER -> AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA);
      case STRING -> AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN);
      case COMMENT -> AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE);
    };
  }

  record TokenSpan(int start, int end, TokenType type) {}

  enum TokenType {
    COMMAND,
    KEYWORD,
    NUMBER,
    STRING,
    COMMENT
  }
}
