package com.lisantra.arabicbasic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class InterpreterVisitor extends ArabicBASICBaseVisitor<Object> {
  private final Locale arabicLocale;
  private final RuntimeMessages msgs;
  private final Map<String, Variable> globalScope;
  private final boolean showDebug;

  public InterpreterVisitor(
      Locale arabicLocale,
      Locale messageLocale,
      Map<String, Variable> globalScope,
      boolean showDebug) {
    super();
    this.arabicLocale = Objects.requireNonNull(arabicLocale);
    this.msgs = new RuntimeMessages(Objects.requireNonNull(messageLocale));
    this.globalScope = globalScope;
    this.showDebug = showDebug;
  }

  private ArabicBasicRuntimeException error(String key, DeclarationSite site, Object... formatArgs) {
    return new ArabicBasicRuntimeException(msgs.fullMessage(site, key, formatArgs), site);
  }

  private ArabicBasicRuntimeException error(
      String key, DeclarationSite site, Throwable cause, Object... formatArgs) {
    return new ArabicBasicRuntimeException(msgs.fullMessage(site, key, formatArgs), site, cause);
  }

  private ArabicBasicRuntimeException errorWithLastWriteSite(
      String key, DeclarationSite site, Variable variable, Object... formatArgs) {
    String body = msgs.fullMessage(site, key, formatArgs);
    DeclarationSite lastWrite = variable.getLastWriteSite();
    if (lastWrite != null && !lastWrite.isUnknown()) {
      body += " " + msgs.format("error.lastWriteSiteHint", lastWrite.line(), lastWrite.charPositionInLine() + 1);
    }
    return new ArabicBasicRuntimeException(body, site);
  }

  public Object visitProgram(ArabicBASIC.ProgramContext ctx) {
    if (showDebug)
      System.out.println("I visited Program");

    return visitChildren(ctx);
  }

  /* This is where we'd implement lexical block scope if we wanted */
  public Object visitBlock(ArabicBASIC.BlockContext ctx) {
    if (showDebug)
      System.out.println("I visited Block");

    return visitChildren(ctx);
  }

  public Object visitStatement(ArabicBASIC.StatementContext ctx) {
    if (showDebug)
      System.out.println("I visited Statement");

    return visitChildren(ctx);
  }

  public Void visitSimpleAssignment(ArabicBASIC.SimpleAssignmentContext ctx) {
    if (showDebug)
      System.out.println("I visited Simple Assignment");

    /*
     * If val is another variable [A = B], then a new value is returned; is
     * "copy by value"
     */
    Value val = (Value) visit(ctx.expression());
    Variable var = null;

    int varCount = ctx.name.size();
    for (int i = 0; i < varCount; i++) {
      String id = ctx.IDENTIFIER(i).getText();
      DeclarationSite writeSite = DeclarationSite.from(ctx.IDENTIFIER(i).getSymbol());

      Symbol s = new VariableSymbol(id, writeSite);

      switch (val.getOriginalType()) {
        case "String":
          var = new StringVariable(s, val, writeSite);
          break;

        case "Integer":
        case "Real":
          var = new NumericVariable(s, val, writeSite);
          break;

        case "Array": {
          ArrayVariable av = new ArrayVariable(s, val, writeSite);
          Object raw = val.getVal();
          int n = 0;

          if (raw instanceof List) {
            n = ((List<?>) raw).size();
          }

          av.setUpperBound(n > 0 ? n - 1 : 0);
          var = av;
        }
          break;

        case "Stack":
          var = new StackVariable(s, val, writeSite);
          break;

        case "Boolean":
          var = new Variable(s, val, writeSite);
          break;

        case "":
        case "Unknown":
          var = new Variable(s, val, writeSite);
          break;

        case "Function":
          var = new Variable(s, val, writeSite);
          break;

        default:
          throw error(
              "error.assignUnsupportedType", DeclarationSite.from(ctx), val.getOriginalType());
      }

      /* this covers both creation and updating a variable */
      globalScope.put(id, var);
    }

    return null;
  }

  public Void visitArrayAssignment(ArabicBASIC.ArrayAssignmentContext ctx) {
    if (showDebug)
      System.out.println("I visited Array Assignment");

    /* we don't need to create a new symbol for element assignment */
    String id = ctx.IDENTIFIER().getSymbol().getText();
    Integer index = (Integer) visit(ctx.subscript());

    if (showDebug)
      System.out.println("Index = " + index);

    // get the widened, stored Variable associated with id. It should be an Array,
    // but better to not cast it and instead to test for class type
    Variable existingArray = globalScope.get(id);
    if (existingArray == null) {
      throw error("error.variableUndeclared", DeclarationSite.from(ctx.IDENTIFIER().getSymbol()), id);
    }
    if (!existingArray.getClass().getSimpleName().equals("ArrayVariable")) {
      throw errorWithLastWriteSite(
          "error.notAnArray", DeclarationSite.from(ctx.IDENTIFIER().getSymbol()), existingArray, id);
    }
    existingArray.markWriteFromSource(DeclarationSite.from(ctx.IDENTIFIER().getSymbol()));

    // visit expression to get value to insert
    Value newElement = (Value) visit(ctx.expression());
    if (showDebug)
      System.out.println("New element = " + newElement);

    // TODO check type of value to insert; check the Value's originalType

    Value arrayContainer = existingArray.getValue();
    arrayContainer.setOriginalType(newElement.getOriginalType());

    int maxIndex = ((ArrayVariable) existingArray).getUpperBound();

    ArrayList targetArray = (ArrayList) arrayContainer.getVal();

    // Copy! this fixed an error where the elements were refs if
    // "targetArray.set(index,
    // newElement);"
    Value existingElement = (Value) targetArray.get(index);
    existingElement.setVal(newElement.getVal());
    existingElement.setOriginalType(newElement.getOriginalType());

    /*
     * must test for existing index; add() for new element, and set() for updating
     */
    /*
     * try {
     * // could just try to get it and deal with exception? expensive in resources
     * (?)
     * // Object existingElement = targetArray.get(index);
     * // update
     * targetArray.set(index, newElement); // TODO enforce consistent typing of
     * elements
     * } catch (IndexOutOfBoundsException idxe) {
     * add new element and enforce array capacity *
     * 
     * 
     * if (index > maxIndex) {
     * System.out.println(globalScope);
     * throw new ArrayIndexOutOfBoundsException(
     * "You tried to add a new element at position: "
     * + index
     * + ", but the array '"
     * + id
     * + "' only has elements from position: 0 to position: "
     * + maxIndex);
     * }
     * 
     * // targetArray.add(index, newElement);
     * }
     */

    return null;
  }

  public Value visitNested(ArabicBASIC.NestedContext ctx) {
    return (Value) visit(ctx.expression());
  }

  public Value visitUnary(ArabicBASIC.UnaryContext ctx) {
    Value expr = (Value) visit(ctx.expression());
    Double exprVal = makeNumber(expr, DeclarationSite.from(ctx));

    /*
     * Copy by value here may only be necessary if there is a variable in the
     * expression.
     * Otherwise, it mutates the original like this A = 1, X=-A and negates A
     * retroactively.
     */
    return new Value(-exprVal, expr.getOriginalType());
  }

  public Value visitAddSub(ArabicBASIC.AddSubContext ctx) {
    Value left = (Value) visit(ctx.expression(0));
    Value right = (Value) visit(ctx.expression(1));

    if (left.getVal() instanceof ArrayList && right.getVal() instanceof ArrayList) {
      if (ctx.op.getText().equals("+")) {
        ArrayList<?> combined = new ArrayList<>();

        combined.addAll((ArrayList) left.getVal());
        combined.addAll((ArrayList) right.getVal());

        return new Value(combined, "Array");
      } else {
        throw error("error.arraysMayNotSubtract", DeclarationSite.from(ctx.op));
      }
    }

    // Are we operating on strings? Only valid for "+"
    if (Objects.equals(left.getOriginalType(), "String")
        && Objects.equals(right.getOriginalType(), "String")) {
      if (ctx.op.getText().equals("+")) {
        return new Value((String) left.getVal() + (String) right.getVal(), "String");
      } else {
        throw error("error.stringsMayNotSubtract", DeclarationSite.from(ctx.op));
      }
    }

    // ensure both terms are addable/subtractable
    Double leftVal = makeNumber(left, DeclarationSite.from(ctx));
    Double rightVal = makeNumber(right, DeclarationSite.from(ctx));

    String resultType = getResultType(left, right);

    // TODO can use getType() if I specify the operators as terminals by aliasing
    // them as
    // tokens in the grammar.
    if (ctx.op.getText().equals("+")) {
      return new Value(leftVal + rightVal, resultType);
    }

    return new Value(leftVal - rightVal, resultType);
  }

  private Double makeNumber(Value val, DeclarationSite site) {
    if (val.getVal() instanceof Double) {
      return (Double) val.getVal();
    }
    throw error("error.numberExpected", site, val.getVal());
  }

  /** {@code Integer}/{@code Real} with a {@link Double} payload (excludes empty/unknown tags). */
  private static boolean isNumericValue(Value v) {
    String t = v.getOriginalType();
    return ("Integer".equals(t) || "Real".equals(t)) && v.getVal() instanceof Double;
  }

  private Value requireComparisonValue(Object visited, DeclarationSite site) {
    if (visited instanceof Boolean || visited == null || !(visited instanceof Value)) {
      throw error("error.comparisonOperandNotValue", site);
    }
    return (Value) visited;
  }

  private void rejectArrayOrFunction(Value v, DeclarationSite site) {
    String t = v.getOriginalType();
    if ("Array".equals(t) || "Function".equals(t)) {
      throw error("error.comparisonUnsupportedType", site, t);
    }
  }

  private void requireOrderingNumericPair(Value left, Value right, DeclarationSite site) {
    if (!isNumericValue(left) || !isNumericValue(right)) {
      throw error("error.orderingRequiresNumeric", site);
    }
  }

  private String getResultType(Value left, Value right) {
    /* Widen the result in case left and right have different original types */
    String resultType;
    String leftType = left.getOriginalType();
    String rightType = right.getOriginalType();

    if (Objects.equals(leftType, "Integer") && leftType.equals(rightType)) {
      resultType = "Integer";
    } else {
      resultType = "Real";
    }

    return resultType;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code
   * ctx}.
   */
  @Override
  public Value visitModulus(ArabicBASIC.ModulusContext ctx) {
    Value left = (Value) visit(ctx.expression(0));
    Value right = (Value) visit(ctx.expression(1));

    Double leftVal = makeNumber(left, DeclarationSite.from(ctx));
    Double rightVal = makeNumber(right, DeclarationSite.from(ctx));

    return new Value(leftVal % rightVal, "Integer");
  }

  public Value visitMulDiv(ArabicBASIC.MulDivContext ctx) {
    Value left = (Value) visit(ctx.expression(0));
    Value right = (Value) visit(ctx.expression(1));

    Double leftVal = makeNumber(left, DeclarationSite.from(ctx));
    Double rightVal = makeNumber(right, DeclarationSite.from(ctx));

    String resultType = getResultType(left, right);

    // TODO can use getType() if I specify the operators as terminals
    if (ctx.op.getText().equals("*")) {
      // TODO use Google Guava's "int mustNotOverflow = IntMath.checkedMultiply(x, y);"
      return new Value(leftVal * rightVal, resultType);
    }

    if (rightVal == 0) {
      throw error("error.divideByZero", DeclarationSite.from(ctx.op));
    }

    return new Value(leftVal / rightVal, resultType);
  }

  public Value visitExponentation(ArabicBASIC.ExponentationContext ctx) {
    Value base = (Value) visit(ctx.expression(0));
    Value exponent = (Value) visit(ctx.expression(1));

    Double basePrimitive = makeNumber(base, DeclarationSite.from(ctx));
    Double exponentPrimitive = makeNumber(exponent, DeclarationSite.from(ctx));

    String resultType = getResultType(base, exponent);

    // Copy by value here may only be necessary if there is a variable in the
    // expression.
    // Otherwise, it mutates the original like this A = 1, X=-A and negates A
    // retroactively.
    return new Value(Math.pow(basePrimitive, exponentPrimitive), resultType);
  }

  /**
   * Resolves a symbol into its value
   *
   * @param ctx
   * @return the value associated with the var name
   */
  public Value visitName(ArabicBASIC.NameContext ctx) {
    if (showDebug)
      System.out.println("I visited Identifier");

    String id = ctx.IDENTIFIER().getText();
    if (!globalScope.containsKey(id)) {
      throw error("error.variableUndeclared", DeclarationSite.from(ctx.IDENTIFIER().getSymbol()), id);
    }

    // The symbol table's value is of custom type Value
    return globalScope.get(id).getValue();
  }

  @Override
  public Value visitArrayAccess(ArabicBASIC.ArrayAccessContext ctx) {
    if (showDebug)
      System.out.println("I visited array access");

    String id = ctx.IDENTIFIER().getText();
    if (!globalScope.containsKey(id)) {
      throw error("error.variableUndeclared", DeclarationSite.from(ctx.IDENTIFIER().getSymbol()), id);
    }

    // get index
    Integer idx = (Integer) visit(ctx.subscript());

    Value val = globalScope.get(id).getValue();
    List targetArray = (ArrayList) val.getVal();

    // TODO use upperBound instead of size()
    int numberOfElements = targetArray.size();
    if (idx > numberOfElements) {
      throw error(
          "error.arrayIndexOutOfRange",
          DeclarationSite.from(ctx),
          idx,
          id,
          numberOfElements);
    }

    String elementsType = val.getOriginalType();
    return new Value(targetArray.get(idx), elementsType);
  }

  public Value visitNumeric(ArabicBASIC.NumericContext ctx) {
    // all get treated as Double anyways, but let's track the original type

    // Double.valueOf causes NumberFormat exception with Arabic numbers
    // https://stackoverflow.com/questions/60044997/integer-valueof-arabic-number-works-fine-but-float-valueof-the-same-number-gives
    NumberFormat arabicNumberFormat = NumberFormat.getNumberInstance(this.arabicLocale);

    String inputNumerical = "";
    String type = "";

    if (null != ctx.INTEGER()) {

      inputNumerical = ctx.INTEGER().getText();
      type = "Integer";
    } else {
      inputNumerical = ctx.REAL().getText();
      type = "Real";
    }

    // convert the text to english digits, and then re-Arabicize later on output
    try {
      double parsedArabicNumeral = arabicNumberFormat.parse(inputNumerical).doubleValue();

      return new Value(parsedArabicNumeral, type);
    } catch (ParseException pe) {
      throw error("error.numberParseFailed", DeclarationSite.from(ctx), pe, inputNumerical);
    }
  }

  public Value visitText(ArabicBASIC.TextContext ctx) {
    return new Value(ctx.STRING().getText(), "String");
  }

  public Value visitBool(ArabicBASIC.BoolContext ctx) {
    boolean bool = "صحيح".equals(ctx.getText());
    return new Value(bool, "Boolean");
  }

  public Value visitArrayCreation(ArabicBASIC.ArrayCreationContext ctx) {
    if (showDebug)
      System.out.println("I visited Array Creation");

    // get array_size
    Integer size = (Integer) visit(ctx.arraySize());

    // wrap in Value; the type of List's elements are unknowable at this stage.
    List<Value> newArray = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      // add blank Value as placeholder
      newArray.add(new Value(null, ""));
    }

    return new Value(newArray, "Array");
  }

  public Integer visitArraySize(ArabicBASIC.ArraySizeContext ctx) {
    Value size = (Value) visit(ctx.expression());

    // 2. ensure it is numeric
    if (!(size.getOriginalType().equals("Integer"))) {
      throw error("error.arraySizeNotInteger", DeclarationSite.from(ctx), size);
    }

    return ((Double) size.getVal()).intValue();
  }

  public Integer visitSubscript(ArabicBASIC.SubscriptContext ctx) {
    // OK, there are no children to visit, so work with the Terminals...
    Integer index = null;

    TerminalNode subscriptName = ctx.IDENTIFIER();
    TerminalNode subscriptInt = ctx.INTEGER();

    if (subscriptInt != null) {
      index = Integer.valueOf(subscriptInt.getText());
    } else if (subscriptName != null) {
      String id = subscriptName.getText();
      if (!globalScope.containsKey(id)) {
        throw error("error.variableUndeclared", DeclarationSite.from(subscriptName.getSymbol()), id);
      }

      // The symbol table's value is of custom type Value
      Value indexVal = globalScope.get(id).getValue();

      /* validate that it's an integer */
      if (!indexVal.getOriginalType().equals("Integer")) {
        throw error("error.subscriptNotInteger", DeclarationSite.from(subscriptName.getSymbol()), id);
      }

      index = ((Double) indexVal.getVal()).intValue();
    }

    return index;
  }

  public Void visitConditionalBlock(ArabicBASIC.ConditionalBlockContext ctx) {
    // TODO may need Apache Commons library BooleanUtils class
    Boolean condition = null;

    // How many conditions to run?
    int testCount = ctx.booleanExpression().size();
    if (showDebug)
      System.out.println("There are " + testCount + " IFs and ELSE IFs.");

    /* how many blocks are here? */
    int blockCount = ctx.block().size();
    if (showDebug)
      System.out.println("There are " + blockCount + " blocks in this IF statement.");

    for (int i = 0; i < testCount; i++) {
      // it could be Boolean or Value from atomicBoolean rule
      Object conditionalExpr = visit(ctx.booleanExpression(i));
      condition = coerceCondition(conditionalExpr);
      if (showDebug)
        System.out.println(
            "condition #" + i + ": " + ctx.booleanExpression(i).getText() + " is " + condition);

      if (Boolean.TRUE.equals(condition)) {
        visit(ctx.block(i));
        return null;
      }
    }

    // Up to HERE, all the conditions evaluated to false, so let's see if there is
    // an ELSE
    if (blockCount == testCount + 1) {
      // if condition is false, and there is an else block, then visit it.
      visit(ctx.block(blockCount - 1));
    }

    return null;

    /*
     * with named keyword tokens, I can do this:
     * if (ctx.start.getType() == YourLexer.BOOL) {
     * // it's a BOOL token
     * }
     * or this (this is python?)
     * myLexer.getVocabulary.getSymbolicName(myTerminalNode.getSymbol.getType)
     * Those vocabulary methods seem to be the preferred way get at the tokens in
     * Antlr 4.5, and tokenNames appears to be deprecated.
     * 
     * ctx.start.getLine();
     */
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code
   * ctx}.
   */
  @Override
  public Void visitSingleLineConditional(ArabicBASIC.SingleLineConditionalContext ctx) {
    Boolean condition = null;

    Object conditionalExpr = visit(ctx.booleanExpression()); // it could be Boolean or Value from atomicBoolean rule
    condition = coerceCondition(conditionalExpr);

    if (showDebug)
      System.out.println(
          "condition " + ": " + ctx.booleanExpression().getText() + " is " + condition);

    if (Boolean.TRUE.equals(condition)) {
      visit(ctx.statement());
      return null;
    }

    return null;
  }

  @Override
  public Boolean visitComparitiveBoolean(ArabicBASIC.ComparitiveBooleanContext ctx) {
    DeclarationSite opSite = DeclarationSite.from(ctx.comp);

    Object l = visit(ctx.booleanExpression(0));
    Object r = visit(ctx.booleanExpression(1));

    Value left = requireComparisonValue(l, opSite);
    Value right = requireComparisonValue(r, opSite);

    rejectArrayOrFunction(left, opSite);
    rejectArrayOrFunction(right, opSite);

    String op = ctx.comp.getText();

    if (showDebug) {
      System.out.println("left: " + left.getVal() + " " + op + " right: " + right.getVal());
    }

    switch (op) {
      case ">":
      case ">=":
      case "<":
      case "<=":
        requireOrderingNumericPair(left, right, opSite);
        double ld = (Double) left.getVal();
        double rd = (Double) right.getVal();
        return switch (op) {
          case ">" -> ld > rd;
          case ">=" -> ld >= rd;
          case "<" -> ld < rd;
          case "<=" -> ld <= rd;
          default -> throw error("error.unknownComparisonOperator", opSite, op);
        };

      case "=":
      case "<>": {
        boolean bothString =
            "String".equals(left.getOriginalType()) && "String".equals(right.getOriginalType());
        boolean bothNumeric = isNumericValue(left) && isNumericValue(right);
        
        if (bothString) {
          boolean eq = Objects.equals(left.getVal(), right.getVal());
          return op.equals("=") ? eq : !eq;
        }

        if (bothNumeric) {
          boolean eq = Objects.equals(left.getVal(), right.getVal());
          return op.equals("=") ? eq : !eq;
        }
        
        if (("String".equals(left.getOriginalType()) && isNumericValue(right))
            || (isNumericValue(left) && "String".equals(right.getOriginalType()))) {
          throw error("error.comparisonStringNumberMix", opSite);
        }
        throw error("error.comparisonUnsupportedType", opSite, describeComparisonTypes(left, right));
      }

      default:
        throw error("error.unknownComparisonOperator", opSite, op);
    }
  }

  private static String describeComparisonTypes(Value left, Value right) {
    return left.getOriginalType() + " / " + right.getOriginalType();
  }

  @Override
  public Boolean visitLogicalAnd(ArabicBASIC.LogicalAndContext ctx) {
    Boolean left = coerceCondition(visit(ctx.booleanExpression(0)));
    Boolean right = coerceCondition(visit(ctx.booleanExpression(1)));

    if (showDebug)
      System.out.println(left);
    if (showDebug)
      System.out.println(right);

    return (left && right);
  }

  @Override
  public Boolean visitLogicalOr(ArabicBASIC.LogicalOrContext ctx) {
    Boolean left = coerceCondition(visit(ctx.booleanExpression(0)));
    Boolean right = coerceCondition(visit(ctx.booleanExpression(1)));

    if (showDebug)
      System.out.println(left);
    if (showDebug)
      System.out.println(right);

    return (left || right);
  }

  @Override
  public Boolean visitNestedBoolean(ArabicBASIC.NestedBooleanContext ctx) {
    return coerceCondition(visit(ctx.booleanExpression()));
  }

  @Override
  public Boolean visitNegatingBoolean(ArabicBASIC.NegatingBooleanContext ctx) {
    Boolean test = coerceCondition(visit(ctx.booleanExpression()));
    return !test; // TODO should I copy by value? or does it even matter?
  }

  /**
   * Tests only for existence; a variable is defined or a constant (which is
   * always true)
   *
   * @param ctx
   * @return
   */
  @Override
  public Value visitAtomicBoolean(ArabicBASIC.AtomicBooleanContext ctx) {
    /* it could be visitName(), visitNumeric() or visitText() */
    // Object x = visit(ctx.variable(ctx));
    // how to tell if it should return a value or be evaulated as a Boolean??
    // just run visitChildren().

    return (Value) visitChildren(ctx);
  }

  @Override
  public Void visitPrint(ArabicBASIC.PrintContext ctx) {
    if (showDebug)
      System.out.println("I visited Print");

    // loop through how many expressions there are
    int exprCount = ctx.expression().size();
    for (int i = 0; i < exprCount; i++) {
      Value exprToPrint = (Value) visit(ctx.expression(i));

      // Pad output with spacing depending on type of separator
      // there will always be size - 1 spacers
      String spacingSeparator = " ";
      int spacerCount = ctx.spacer.size();
      if (i < spacerCount) {
        String spacingController = ctx.spacer.get(i).getText();
        if (Objects.equals(spacingController, ";"))
          spacingSeparator = "";
      }

      NumberFormat arabicNumberFormat;
      Object boxedPrimitive = exprToPrint.getVal();

      if (showDebug)
        System.out.println(boxedPrimitive);

      // Odd situation in an array where it's wrapped again
      if (exprToPrint.getVal() instanceof Value) {
        // System.out.println("It IS a Value obj!");
        // System.out.println(exprToPrint.getVal());
        boxedPrimitive = ((Value) boxedPrimitive).getVal();
      }

      /*
       * special case for arrays, since getOriginalType becomes that of it's elements
       */
      if (boxedPrimitive instanceof ArrayList) {
        for (Value element : (ArrayList<Value>) boxedPrimitive) {
          // TODO ugh, I have to check by type just like below!
          // TODO this is a good use case for recursion!
          // arabicNumberFormat = NumberFormat.getNumberInstance(this.arabicLocale);
          // System.out.println(arabicNumberFormat.format(boxedPrimitive));
          System.out.println(element.getVal());
        }

        return null;
      }

      switch (exprToPrint.getOriginalType()) {
        case "String":
          System.out.print(boxedPrimitive + spacingSeparator);
          break;
        case "Boolean":
          System.out.print((Boolean.TRUE.equals(boxedPrimitive) ? "صحيح" : "خطأ") + spacingSeparator);
          break;
        case "Real":
          arabicNumberFormat = NumberFormat.getNumberInstance(this.arabicLocale);
          System.out.println(arabicNumberFormat.format(boxedPrimitive));
          break;
        case "Integer":
          arabicNumberFormat = NumberFormat.getNumberInstance(this.arabicLocale);

          // TODO we may have an Array element here

          // truncate it
          /*
           * new DecimalFormat(
           * "#,###.##",
           * DecimalFormatSymbols.getInstance(customLocale)).format(d))
           */

          boxedPrimitive = ((Double) boxedPrimitive).intValue();
          System.out.println(arabicNumberFormat.format(boxedPrimitive));
          break;

        case "Array":
          break;

        default:
      }
    }

    return null;
  }

  @Override
  public Void visitInput(ArabicBASIC.InputContext ctx) {
    // TODO Maybe a "next?" default non-first prompt would be nice?

    ListIterator<Token> varTokenIter = ctx.var.listIterator();
    while (varTokenIter.hasNext()) {
      System.out.println(); // blank line before prompt

      // the prompt should apply to each variable in the list, but only printed once
      if (ctx.prompt != null && varTokenIter.nextIndex() == 0) {
        System.out.print(ctx.prompt.getText().replaceAll("^\"|\"$", "") + " ");
      } else {
        // if no prompt, default to "?"
        System.out.print("? ");
      }

      Integer intInput = null;
      Double floatInput = null;
      String textInput = null;
      Token varToken = varTokenIter.next();
      String id = varToken.getText();
      DeclarationSite writeSite = DeclarationSite.from(varToken);
      Symbol s = new VariableSymbol(id, writeSite);
      Variable variable = null;

      try {
        // get line input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // using java.io.*
        String input = reader.readLine(); // it reads everything into string
        // or, String str = System.console().readLine();

        // Nested exceptions and use of exceptions like this is considered bad style.
        try {
          intInput = Integer.parseInt(input);
          Value val = new Value(Double.valueOf(intInput), "Integer");
          variable = new NumericVariable(s, val, writeSite);

        } catch (NumberFormatException ne0) {
          // try to get float/real
          try {
            NumberFormat arabicNumberFormat = NumberFormat.getNumberInstance(this.arabicLocale);
            double parsedArabicNumeral = arabicNumberFormat.parse(input).doubleValue();

            // make Value and Variable here
            Value val = new Value(parsedArabicNumeral, "Real");
            variable = new NumericVariable(s, val, writeSite);

          } catch (IllegalArgumentException | ParseException e) {
            // keep it as a string
            textInput = input;
            // make Value and Variable here
            Value val = new Value(textInput, "String");
            variable = new StringVariable(s, val, writeSite);
          }
        }

        globalScope.put(id, variable);

      } catch (IOException e) {
        String detail = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        throw error("error.inputIoFailed", DeclarationSite.from(varToken), e, detail);
      }
    }

    return null;
  }

  /**
   * If val is another variable [A = B], then a new value is returned; is "copy by
   * value"
   */
  @Override
  public Void visitForLoop(ArabicBASIC.ForLoopContext ctx) {

    int lower = Integer.parseInt(ctx.lower.getText());

    /* upper bound can be an expression */
    Value upperExpression = (Value) visit(ctx.expression());
    if (!(upperExpression.getOriginalType().equals("Integer"))) {
      throw error(
          "error.forUpperBoundNotInteger", DeclarationSite.from(ctx.expression()), upperExpression);
    }
    int upper = ((Double) upperExpression.getVal()).intValue();
    // int upper = Integer.parseInt(ctx.upper.getText());

    // TODO if I decide to be not inclusive, then either subtract 1 here or change
    // "while" below

    int counter = lower;

    int step = 1;
    if (ctx.step != null) {
      step = Integer.parseInt(ctx.step.getText());
    }

    if (showDebug)
      System.out.printf("lower=%d%n", lower);
    if (showDebug)
      System.out.printf("upper=%d%n", upper);

    // 1. instantiate control variable = "lower" and add it to var table
    String id = ctx.control.getText();
    String nextVar = ctx.next.getText();
    if (!id.equals(nextVar)) {
      throw error("error.forNextVariableMismatch", DeclarationSite.from(ctx.next), id, nextVar);
    }

    DeclarationSite controlWriteSite = DeclarationSite.from(ctx.control);
    Symbol s = new VariableSymbol(id, controlWriteSite);

    // temp val using lower, which should be the same as counter...
    Value controlVal = new Value((double) lower, "Integer");
    Variable controlVar = new NumericVariable(s, controlVal, controlWriteSite);
    // globalScope.put(id, controlVar);

    // Start Java while loop
    // 2. if control var is less than "upper", then visit(block)
    while (counter >= lower && counter <= upper) {
      // 3. store updated value FIRST
      controlVal.setVal((double) counter);
      controlVar.markWriteFromSource(controlWriteSite);
      globalScope.put(id, controlVar);

      visit(ctx.block());

      if (showDebug)
        System.out.printf("counter=%d%n", counter);

      // 4. increment control var by step
      counter += step;
    }

    return null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code
   * ctx}.
   */
  @Override
  public Void visitWhileLoop(ArabicBASIC.WhileLoopContext ctx) {
    // assume true, so we can enter the loop; can be "Break"ed if condition
    // evaluates to false
    // before executing "block"
    Boolean condition = true;

    // it could be Boolean or Value from atomicBoolean rule
    while (condition) {
      Object conditionalExpr = visit(ctx.booleanExpression());
      condition = coerceCondition(conditionalExpr);
      if (showDebug)
        System.out.println("condition: " + ctx.booleanExpression().getText() + " is " + condition);

      if (Boolean.FALSE.equals(condition)) {
        break;
      }

      visit(ctx.block());
    }

    return null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code
   * ctx}.
   */
  @Override
  public Void visitDefineSingleLineFunction(ArabicBASIC.DefineSingleLineFunctionContext ctx) {
    // 1. get funcName
    String id = ctx.funcName.getText();
    Symbol s = new FunctionSymbol(id);

    // 2. get arg and visit(identifier)
    ArabicBASIC.VariableContext argCtx = ctx.variable();
    String argumentPlaceholder = argCtx.getText();
    DeclarationSite argDeclarationSite = declarationSiteForFormalParameter(argCtx);

    // 3. get the body/expression
    ArabicBASIC.ExpressionContext functionExpression = ctx.expression();

    // 4. save the expression
    globalScope.put(
        id,
        new FunctionVariable(
            s,
            new Value(null, "Function"),
            DeclarationSite.from(ctx.funcName),
            argumentPlaceholder,
            argDeclarationSite,
            functionExpression));

    return null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code
   * ctx}.
   */
  @Override
  public Value visitCallFunction(ArabicBASIC.CallFunctionContext ctx) {
    // 1. retrieve function from the symbol table
    String fnName = ctx.funcName.getText();
    Object fn = globalScope.get(fnName);

    // should be a FunctionVariable
    FunctionVariable fnVar = null;
    if (fn instanceof FunctionVariable) {
      fnVar = (FunctionVariable) fn;
    } else {
      throw error("error.notAFunction", DeclarationSite.from(ctx.funcName), fnName);
    }

    // 2. run the arg context to get a value, the passed-in value
    Value argValue = (Value) visit(ctx.variable());
    // Object actualArgumentContent = argValue.getVal();
    if (showDebug)
      System.out.println(argValue);

    // 3. check it has the argument (s) <-- grammar takes care of it,
    // and any type mismatch will be caught by visitNumeric/visitText() etc

    // 4. copy the current arg value into a new var whose symbol was originally
    // defined as a
    // function arg symbol and set the value of the arg into the Symbol table so the
    // visit*() can
    // get to it
    String argSymbol = fnVar.getArg();
    globalScope.put(
        fnVar.getArg(),
        new Variable(
            new VariableSymbol(fnVar.getArg(), fnVar.getArgDeclarationSite()),
            argValue,
            DeclarationSite.from(ctx.variable())));
    // ?? TODO needs to replicate switch to make it the right sub-type of Variable

    // 5. apply the raw value to the functionExpression context
    // let it run now, looking for the value of the ARG variable just recently added
    // to the variable
    // table
    Value fnResult2 = (Value) visit(fnVar.getBody()); // should return a Value() instance
    if (showDebug)
      System.out.println(fnResult2);

    // 6. Destroy the arg variable in the symbol table
    globalScope.remove(fnVar.getArg());

    // 7. return the raw result wrapped, or just forward the Value class,
    // actually...
    return fnResult2;
  }

  @Override
  public Value visitStringFunction(ArabicBASIC.StringFunctionContext ctx) {
    // 4. get name
    String operation = ctx.name.getText();

    // 1. Get value to operate upon
    // multiple args are allowed such as for RIGHT, so now it's a List
    Value argValue1 = (Value) visit(ctx.variable(0));

    // 2. construct a default return value
    Value retValue = new Value("", "String");

    // special case for CHR()
    if (operation.equals("CHR")) {
      // oh, this casting is a doozey!
      char ch = (char) ((Double) argValue1.getVal()).intValue();
      retValue.setVal(ch);
      return retValue;
    }

    // 2. ensure it is a string
    if (!argValue1.getOriginalType().equals("String")) {
      throw error("error.stringFnArgNotString", DeclarationSite.from(ctx), argValue1);
    }

    String str = (String) argValue1.getVal();

    // if there's a 2nd arg, it has to be numeric
    Value argValue2 = null;
    if (null != ctx.variable(1)) {
      argValue2 = (Value) visit(ctx.variable(1));

      if (!argValue2.getOriginalType().equals("Integer")) {
        throw error("error.stringFnSecondArgNotNumber", DeclarationSite.from(ctx), argValue2);
      }
    }

    switch (operation) {
      // MID
      case "LEN":
        retValue.setVal((double) str.length());
        retValue.setOriginalType("Integer");

        if (showDebug)
          System.out.println(retValue);
        break;

      case "LEFT":
        // the grammar won't catch an absent 2nd arg
        if (null == argValue2) {
          throw error("error.leftRequiresSecondArg", DeclarationSite.from(ctx));
        }

        if (((Double) argValue2.getVal()).intValue() > str.length()) {
          throw error("error.leftLengthTooLong", DeclarationSite.from(ctx), str.length());
        }

        retValue.setVal(str.substring(0, ((Double) argValue2.getVal()).intValue()));
        break;

      case "RIGHT":
        // the grammar won't catch an absent 2nd arg
        if (null == argValue2) {
          throw error("error.rightRequiresSecondArg", DeclarationSite.from(ctx));
        }

        if (((Double) argValue2.getVal()).intValue() > str.length()) {
          throw error("error.rightLengthTooLong", DeclarationSite.from(ctx), str.length());
        }
        // will throw IndexOutOfBoundsException
        retValue.setVal(
            str.substring(str.length() - ((Double) argValue2.getVal()).intValue(), str.length()));
        break;

      case "MID":
        break;

      case "ORD":
        int ascii = (int) str.charAt(0);
        retValue.setVal((double) ascii);
        retValue.setOriginalType("Integer");
        break;

      default:
        throw error("error.unrecognizedStringFunction", DeclarationSite.from(ctx.name), operation);
    }

    return retValue;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code
   * ctx}.
   */
  @Override
  public Value visitMathFunction(ArabicBASIC.MathFunctionContext ctx) {
    // 2. construct a return value
    Value retValue = new Value(null, "Real"); // default to REAL

    // 4. get name
    String operation = ctx.name.getText();

    if (null == ctx.expression()) {
      throw error("error.mathFnRequiresArgument", DeclarationSite.from(ctx));
    }

    // 1. Get value to operate upon
    Value argValue = (Value) visit(ctx.expression());

    // 2. ensure it is numeric
    if (!(argValue.getOriginalType().equals("Integer")
        || argValue.getOriginalType().equals("Real"))) {
      throw error("error.mathFnArgNotNumber", DeclarationSite.from(ctx), argValue);
    }

    // TODO has to be a better way to proxy the calls...
    switch (operation) {
      case "ABS":
        retValue.setVal(Math.abs((double) argValue.getVal()));
        retValue.setOriginalType(argValue.getOriginalType());
        break;

      case "SQR":
        retValue.setVal(Math.sqrt((double) argValue.getVal()));
        break;

      case "LOG":
        if ((double) argValue.getVal() == 0.0) {
          throw error("error.logZero", DeclarationSite.from(ctx));
        }

        retValue.setVal(Math.log10((double) argValue.getVal()));
        break;

      case "COS":
        retValue.setVal(Math.cos((double) argValue.getVal()));
        break;

      case "SIN":
        retValue.setVal(Math.sin((double) argValue.getVal()));
        break;

      case "TAN":
        retValue.setVal(Math.tan((double) argValue.getVal()));
        break;

      case "INT":
        // this is a really roundabout thing...
        retValue.setVal((double) ((Double) argValue.getVal()).intValue());
        retValue.setOriginalType("Integer");
        break;

      case "RND":
        retValue.setVal(
            (double) ThreadLocalRandom.current()
                .nextInt(0, ((Double) argValue.getVal()).intValue() + 1));
        retValue.setOriginalType("Integer");
        break;

      case "EXP":
        break;

      default:
        throw error("error.unrecognizedMathFunction", DeclarationSite.from(ctx.name), operation);
    }

    return retValue;
  }

  /**
   * Formal parameters use {@code variable} in the grammar; only an {@link
   * ArabicBASIC.NameContext} has an identifier token to anchor a declaration site.
   */
  private static DeclarationSite declarationSiteForFormalParameter(
      ArabicBASIC.VariableContext varCtx) {
    if (varCtx instanceof ArabicBASIC.NameContext nameCtx) {
      return DeclarationSite.from(nameCtx.IDENTIFIER().getSymbol());
    }

    return DeclarationSite.UNKNOWN;
  }

  @SuppressWarnings("unchecked")
  private Deque<Value> requireStack(ArabicBASIC.VariableContext ctx, DeclarationSite site) {
    Object visited = visit(ctx);
    if (!(visited instanceof Value stackValue)) {
      throw error("error.notAStack", site, ctx.getText());
    }
    if (!"Stack".equals(stackValue.getOriginalType()) || !(stackValue.getVal() instanceof Deque<?>)) {
      throw error("error.notAStack", site, ctx.getText());
    }
    return (Deque<Value>) stackValue.getVal();
  }

  private void markStackWriteIfNamed(
      ArabicBASIC.VariableContext ctx, DeclarationSite sourceWriteSite) {
    if (ctx instanceof ArabicBASIC.NameContext nameCtx) {
      String id = nameCtx.IDENTIFIER().getText();
      Variable v = globalScope.get(id);
      if (v != null) {
        v.markWriteFromSource(sourceWriteSite);
      }
    }
  }

  private static Value copyValue(Value v) {
    return new Value(v.getVal(), v.getOriginalType());
  }

  private static Boolean coerceCondition(Object conditionalExpr) {
    if (conditionalExpr instanceof Boolean b) {
      return b;
    }
    if (conditionalExpr instanceof Value v) {
      if ("Boolean".equals(v.getOriginalType()) && v.getVal() instanceof Boolean b) {
        return b;
      }
      return true;
    }
    return false;
  }

  public Value visitStackFactory(ArabicBASIC.StackFactoryContext ctx) {
    return new Value(new ArrayDeque<Value>(), "Stack");
  }

  public Value visitStackPushFunction(ArabicBASIC.StackPushFunctionContext ctx) {
    DeclarationSite site = DeclarationSite.from(ctx);
    Deque<Value> stack = requireStack(ctx.stack, site);
    Value pushed = (Value) visit(ctx.value);
    stack.addLast(copyValue(pushed));
    markStackWriteIfNamed(ctx.stack, site);
    return new Value((double) stack.size(), "Integer");
  }

  public Value visitStackPopFunction(ArabicBASIC.StackPopFunctionContext ctx) {
    DeclarationSite site = DeclarationSite.from(ctx);
    Deque<Value> stack = requireStack(ctx.stack, site);
    Value popped = stack.pollLast();
    markStackWriteIfNamed(ctx.stack, site);
    if (popped == null) {
      return new Value(null, "");
    }
    return copyValue(popped);
  }

  public Value visitStackPeekFunction(ArabicBASIC.StackPeekFunctionContext ctx) {
    Deque<Value> stack = requireStack(ctx.stack, DeclarationSite.from(ctx));
    Value peeked = stack.peekLast();
    if (peeked == null) {
      return new Value(null, "");
    }
    return copyValue(peeked);
  }

  public Value visitStackEmptyFunction(ArabicBASIC.StackEmptyFunctionContext ctx) {
    Deque<Value> stack = requireStack(ctx.stack, DeclarationSite.from(ctx));
    return new Value(stack.isEmpty(), "Boolean");
  }
}
