package com.lisantra.arabicbasic;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "run",
    mixinStandardHelpOptions = true,
    versionProvider = ManifestVersionProvider.class,
    resourceBundle = "Messages")
public class App implements Callable<Integer> {
  @CommandLine.Parameters(index = "0", descriptionKey = "fileParam")
  private File file;

  @CommandLine.Option(
      names = {"-d", "--debug"},
      descriptionKey = "debug"
      //    description = "Print out the symbol table after running an ArabicBASIC script"
      )
  private boolean showDebug = false;

  @CommandLine.Option(names = "--locale", descriptionKey = "localeParam", description = "Language tag for interpreter runtime error messages (e.g. en, en-US, ar). Default follows JVM locale after Arabic setup.")
  private String messageLocaleTag;

  /**
   * Runs the main interpreter from the command line.
   *
   * @return A system exit code
   * @throws Exception
   */
  @Override
  public Integer call() throws Exception {
    try {
      Locale messageLocaleOverride = null;
      if (messageLocaleTag != null && !messageLocaleTag.isBlank()) {
        messageLocaleOverride = parseMessageLocaleTag(messageLocaleTag.trim());
      }

      return runScript(file.toPath(), showDebug, messageLocaleOverride);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      return 2;
    }
  }

  private static Locale parseMessageLocaleTag(String raw) {
    Locale loc = Locale.forLanguageTag(raw.replace('_', '-'));
    if (loc.getLanguage().isEmpty()) {
      throw new IllegalArgumentException("Invalid --locale value: " + raw);
    }

    return loc;
  }

  /**
   * Parse and run a UTF-8 script using {@link System#in}, {@link System#out}, and {@link
   * System#err}. Used by the CLI and by tests.
   *
   * @return 0 on success, 1 on parse/runtime failure
   */
  public static int runScript(Path source, boolean showDebug) {
    return runScript(source, showDebug, null);
  }

  /**
   * @param messageLocaleOverride if non-null, used for interpreter {@link RuntimeMessages}; if
   *     null, uses {@link Locale#getDefault()} after Arabic default is installed
   */
  public static int runScript(Path source, boolean showDebug, Locale messageLocaleOverride) {
    Locale previousDefault = Locale.getDefault();

    /* u-nu-Arab is required for arabic digits */
    Locale arabicLocale =
        new Locale.Builder()
            .setLanguage("ar")
            .setExtension(Locale.UNICODE_LOCALE_EXTENSION, "nu-arab")
            .build();

    Locale.setDefault(arabicLocale);

    Locale messageLocale =
        messageLocaleOverride != null ? messageLocaleOverride : Locale.getDefault();

    /* we need separate tables: one for symbols and the other for variable states only = scope */
    /* since BASIC scope is global, we don't need a stack, and a HashMap is great for fast lookup */
    Map<String, Variable> globalScope = new LinkedHashMap<>();
    // TODO wrap in a class: Scope {}? and provide methods like resolve() and define()?
    // If so, then remember that Scope has one Symbol table.

    try {
      /* create an input stream from the string */
      ArabicBASICLexer lexer = new ArabicBASICLexer(CharStreams.fromPath(source));
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

      /* Instantiate my visitor class, which is the actual interpreter */
      InterpreterVisitor interpreter =
          new InterpreterVisitor(arabicLocale, messageLocale, globalScope, showDebug);

      /* Walk the interpreter through the parse tree */
      interpreter.visit(programTree);

      if (showDebug) System.out.println(globalScope);
      if (showDebug) System.out.println("Finished running ArabicBASIC script");

      /* Be a good Unix system citizen and return a numerical exit code */
      return 0;
    } catch (ArabicBasicRuntimeException e) {
      System.err.println(e.getMessage());
      if (showDebug) {
        e.printStackTrace();
      }

      return 1;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      if (showDebug) {
        e.printStackTrace();
      }

      return 1;
    } finally {
      Locale.setDefault(previousDefault);
    }
  }

  /**
   * Main entry point.
   *
   * @param args Array of command-line arguments
   */
  public static void main(String... args) {
    CommandLine interpreter = new CommandLine(new App());

    // alternate way to add resource bundles
    // interpreter.setResourceBundle(ResourceBundle.getBundle("Messages"));

    int exitCode = interpreter.execute(args);
    System.exit(exitCode);
  }
}
