package com.lisantra.arabicbasic;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Locale;
import java.util.Map;

/**
 * Shared interpreter execution helper used by script and REPL flows.
 */
final class ExecutionEngine {
  private ExecutionEngine() {}

  static int runSource(
      CharStream source,
      Locale arabicLocale,
      Locale messageLocale,
      Map<String, Variable> globalScope,
      boolean showDebug,
      boolean printDebugSummary) {
    /* create an input stream from the string */
    ArabicBASICLexer lexer = new ArabicBASICLexer(source);
    ArabicBASIC parser = new ArabicBASIC(new CommonTokenStream(lexer));

    /* my custom error listener is needed for cleaner, and Arabic error messages.
     * However, TODO it throws a cancellation exception even on resolved ambiguities which is
     *           obviously not desirable. */
    //    lexer.removeErrorListeners();
    //    lexer.addErrorListener(BASICErrorListener.INSTANCE);
    //    parser.removeErrorListeners();
    //    parser.addErrorListener(BASICErrorListener.INSTANCE);

    /* listen and warn for ambiguous grammar, but recover and continue if possible */
    if (showDebug) {
      parser.addErrorListener(new DiagnosticErrorListener());
      parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
    }

    /* Instantiate the parse tree */
    ParseTree programTree = parser.program();
    if (parser.getNumberOfSyntaxErrors() > 0) {
      return 1;
    }

    /* Instantiate my visitor class, which is the actual interpreter */
    InterpreterVisitor interpreter =
        new InterpreterVisitor(arabicLocale, messageLocale, globalScope, showDebug);

    /* Walk the interpreter through the parse tree */
    interpreter.visit(programTree);

    if (showDebug && printDebugSummary) System.out.println(globalScope);
    if (showDebug && printDebugSummary) System.out.println("Finished running ArabicBASIC script");

    /* Be a good Unix system citizen and return a numerical exit code */
    return 0;
  }
}
