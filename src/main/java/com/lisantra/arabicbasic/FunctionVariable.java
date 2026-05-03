package com.lisantra.arabicbasic;

import java.util.Objects;

// TODO maybe THIS should be parameterized with the type of the Value's val or type of its elements if ArrayList
public class FunctionVariable extends Variable {
  private final String argSymbol;
  private final DeclarationSite argDeclarationSite;
  private final ArabicBASIC.ExpressionContext body;

  public FunctionVariable(
      Symbol symbol,
      Value value,
      String argSymbol,
      DeclarationSite argDeclarationSite,
      ArabicBASIC.ExpressionContext body) {
    super(Objects.requireNonNull(symbol, "symbol"), Objects.requireNonNull(value, "value"));
    String arg = Objects.requireNonNull(argSymbol, "argSymbol");
    if (arg.isBlank()) {
      throw new IllegalArgumentException("argSymbol must not be blank");
    }
    this.argSymbol = arg;
    this.argDeclarationSite = Objects.requireNonNull(argDeclarationSite, "argDeclarationSite");
    this.body = Objects.requireNonNull(body, "body");
  }

  public FunctionVariable(
      Symbol symbol,
      Value value,
      DeclarationSite sourceWriteSite,
      String argSymbol,
      DeclarationSite argDeclarationSite,
      ArabicBASIC.ExpressionContext body) {
    super(
        Objects.requireNonNull(symbol, "symbol"),
        Objects.requireNonNull(value, "value"),
        sourceWriteSite);
    String arg = Objects.requireNonNull(argSymbol, "argSymbol");
    if (arg.isBlank()) {
      throw new IllegalArgumentException("argSymbol must not be blank");
    }
    this.argSymbol = arg;
    this.argDeclarationSite = Objects.requireNonNull(argDeclarationSite, "argDeclarationSite");
    this.body = Objects.requireNonNull(body, "body");
  }

  public ArabicBASIC.ExpressionContext getBody() {
    return body;
  }

  public String getArg() {
    return argSymbol;
  }

  /** Source position of the formal parameter identifier in the function definition. */
  public DeclarationSite getArgDeclarationSite() {
    return argDeclarationSite;
  }

  /**
   * @return function name, parameter, and body as source text (not parse-tree internals).
   */
  @Override
  public String toString() {
    return "["
        + getSymbol().getClass().getSimpleName()
        + " "
        + getSymbol().getName()
        + "] arg '"
        + argSymbol
        + "' @ "
        + argDeclarationSite
        + " -> "
        + body.getText();
  }
}
