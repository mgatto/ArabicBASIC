package com.lisantra.arabicbasic;

import com.lisantra.arabicbasic.debug.UnicodeDebugFormatter;
import com.lisantra.arabicbasic.debug.UnicodeDebugInfo;
import com.lisantra.arabicbasic.debug.UnicodeInspector;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jline.reader.EndOfFileException;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "run",
    mixinStandardHelpOptions = true,
    versionProvider = ManifestVersionProvider.class,
    resourceBundle = "Messages")
public class App implements Callable<Integer> {
  // Isolate the ASCII prompt as LTR, then bias user input direction to RTL.
  private static final String LTR_ISOLATE = "\u2066";
  private static final String POP_DIRECTIONAL_ISOLATE = "\u2069";
  private static final String RTL_MARK = "\u200F";
  private static final String DEFAULT_REPL_HISTORY_FILE = ".arabicbasic_repl_history";
  private static final String REPL_HISTORY_OVERRIDE_PROPERTY = "arabicbasic.repl.history.path";
  private static final String REPL_PROMPT =
      LTR_ISOLATE + "arabicbasic> " + POP_DIRECTIONAL_ISOLATE + RTL_MARK;
  private static final String REPL_WELCOME = "ArabicBASIC REPL. Type :exit to quit.";

  @CommandLine.Parameters(index = "0", arity = "0..1", descriptionKey = "fileParam")
  private File file;

  @CommandLine.Option(
      names = {"-d", "--debug"},
      descriptionKey = "debug"
      //    description = "Print out the symbol table after running an ArabicBASIC script"
      )
  private boolean showDebug = false;

  @CommandLine.Option(names = "--locale", descriptionKey = "localeParam", description = "Language tag for interpreter runtime error messages (e.g. en, en-US, ar). Default follows JVM locale after Arabic setup.")
  private String messageLocaleTag;

  @CommandLine.Option(
      names = {"-u", "--unicode-map"},
      paramLabel = "<file>",
      descriptionKey = "unicodeMapParam")
  private File unicodeMapFile;

  @CommandLine.Option(names = {"-r", "--repl"}, descriptionKey = "replParam")
  private boolean replMode;

  @CommandLine.Option(
      names = {"--repl-history-file"},
      paramLabel = "<file>",
      descriptionKey = "replHistoryParam")
  private File replHistoryFile;

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

      if (unicodeMapFile != null) {
        return runUnicodeMap(unicodeMapFile.toPath());
      }

      if (replMode || file == null) {
        return runRepl(
            showDebug,
            messageLocaleOverride,
            replHistoryFile != null ? replHistoryFile.toPath() : null);
      }

      return runScript(file.toPath(), showDebug, messageLocaleOverride);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      return 2;
    }
  }

  static int runUnicodeMap(Path source) {
    try {
      String input = Files.readString(source);
      UnicodeDebugInfo info = UnicodeInspector.inspect(input);
      System.out.println(UnicodeDebugFormatter.toMultilineText(info));
      return 0;
    } catch (Exception e) {
      System.err.println("Unable to read unicode-map file: " + source);
      System.err.println(e.getMessage());
      return 1;
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
      int exitCode =
          runSource(
              CharStreams.fromPath(source),
              arabicLocale,
              messageLocale,
              globalScope,
              showDebug,
              true);
      return exitCode;
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

  static int runRepl(boolean showDebug, Locale messageLocaleOverride) {
    return runRepl(showDebug, messageLocaleOverride, null);
  }

  static int runRepl(boolean showDebug, Locale messageLocaleOverride, Path historyFileOverride) {
    Locale previousDefault = Locale.getDefault();
    Locale arabicLocale =
        new Locale.Builder()
            .setLanguage("ar")
            .setExtension(Locale.UNICODE_LOCALE_EXTENSION, "nu-arab")
            .build();

    Locale.setDefault(arabicLocale);
    Locale messageLocale =
        messageLocaleOverride != null ? messageLocaleOverride : Locale.getDefault();
    Map<String, Variable> globalScope = new LinkedHashMap<>();
    Path historyPath = resolveReplHistoryPath(historyFileOverride);

    if (!shouldUseJline()) {
      return runBufferedRepl(globalScope, arabicLocale, messageLocale, showDebug, historyPath);
    }

    try (Terminal terminal = createReplTerminal()) {
      LineReader reader = createLineReader(terminal, historyPath);
      terminal.writer().println(REPL_WELCOME);
      terminal.flush();
      while (true) {
        String input;
        try {
          input = reader.readLine(REPL_PROMPT);
        } catch (UserInterruptException e) {
          continue;
        } catch (EndOfFileException e) {
          break;
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
          continue;
        }

        ReplCommand replCommand = resolveReplCommand(trimmed);
        if (replCommand == ReplCommand.EXIT) {
          break;
        }
        if (replCommand != ReplCommand.NONE) {
          runReplCommand(replCommand, globalScope, terminal);
          continue;
        }

        try {
          int result =
              runSource(
                  CharStreams.fromString(ensureTrailingLineBreak(input)),
                  arabicLocale,
                  messageLocale,
                  globalScope,
                  showDebug,
                  false);
          if (result != 0) {
            System.err.println("REPL input had parse errors.");
          }
        } catch (ArabicBasicRuntimeException e) {
          System.err.println(e.getMessage());
          if (showDebug) {
            e.printStackTrace();
          }
        } catch (Exception e) {
          System.err.println(e.getMessage());
          if (showDebug) {
            e.printStackTrace();
          }
        }
      }

      persistHistory(reader.getHistory(), showDebug);
      return 0;
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

  private static int runBufferedRepl(
      Map<String, Variable> globalScope,
      Locale arabicLocale,
      Locale messageLocale,
      boolean showDebug,
      Path historyPath) {
    List<String> historyLines = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      System.out.println(REPL_WELCOME);
      while (true) {
        System.out.print(REPL_PROMPT);
        String input = reader.readLine();
        if (input == null) {
          break;
        }

        historyLines.add(input);
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
          continue;
        }

        ReplCommand replCommand = resolveReplCommand(trimmed);
        if (replCommand == ReplCommand.EXIT) {
          break;
        }
        if (replCommand != ReplCommand.NONE) {
          runReplCommandToStdout(replCommand, globalScope);
          continue;
        }

        try {
          int result =
              runSource(
                  CharStreams.fromString(ensureTrailingLineBreak(input)),
                  arabicLocale,
                  messageLocale,
                  globalScope,
                  showDebug,
                  false);
          if (result != 0) {
            System.err.println("REPL input had parse errors.");
          }
        } catch (ArabicBasicRuntimeException e) {
          System.err.println(e.getMessage());
          if (showDebug) {
            e.printStackTrace();
          }
        } catch (Exception e) {
          System.err.println(e.getMessage());
          if (showDebug) {
            e.printStackTrace();
          }
        }
      }
      persistBufferedHistory(historyPath, historyLines, showDebug);
      return 0;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      if (showDebug) {
        e.printStackTrace();
      }
      return 1;
    }
  }

  private static int runSource(
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

  private static Terminal createReplTerminal() throws Exception {
    return TerminalBuilder.builder()
        .encoding(StandardCharsets.UTF_8)
        .build();
  }

  private static boolean shouldUseJline() {
    if (Boolean.getBoolean("arabicbasic.repl.forceJline")) {
      return true;
    }
    return System.console() != null;
  }

  private static LineReader createLineReader(Terminal terminal, Path historyPath) throws Exception {
    Path normalizedHistoryPath = historyPath.toAbsolutePath();
    Path historyDirectory = normalizedHistoryPath.getParent();
    if (historyDirectory != null) {
      Files.createDirectories(historyDirectory);
    }
    return LineReaderBuilder.builder()
        .terminal(terminal)
        .appName("arabicbasic")
        .highlighter(new ReplSyntaxHighlighter())
        .variable(LineReader.HISTORY_FILE, normalizedHistoryPath)
        .build();
  }

  private static Path resolveReplHistoryPath(Path historyFileOverride) {
    if (historyFileOverride != null) {
      return historyFileOverride;
    }

    String propertyOverride = System.getProperty(REPL_HISTORY_OVERRIDE_PROPERTY);
    if (propertyOverride != null && !propertyOverride.isBlank()) {
      return Paths.get(propertyOverride);
    }

    String userHome = System.getProperty("user.home", ".");
    return Paths.get(userHome).resolve(DEFAULT_REPL_HISTORY_FILE);
  }

  private static void persistHistory(History history, boolean showDebug) {
    try {
      history.save();
    } catch (Exception e) {
      if (showDebug) {
        e.printStackTrace();
      }
    }
  }

  private static void persistBufferedHistory(
      Path historyPath, List<String> historyLines, boolean showDebug) {
    if (historyLines.isEmpty()) {
      return;
    }
    try {
      Path normalizedHistoryPath = historyPath.toAbsolutePath();
      Path historyDirectory = normalizedHistoryPath.getParent();
      if (historyDirectory != null) {
        Files.createDirectories(historyDirectory);
      }
      String payload = String.join(System.lineSeparator(), historyLines) + System.lineSeparator();
      Files.writeString(
          normalizedHistoryPath,
          payload,
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);
    } catch (Exception e) {
      if (showDebug) {
        e.printStackTrace();
      }
    }
  }

  private static ReplCommand resolveReplCommand(String trimmedInput) {
    String normalized = stripDirectionalMarks(trimmedInput).toLowerCase(Locale.ROOT);
    return switch (normalized) {
      case ":help", "help" -> ReplCommand.HELP;
      case ":exit", ":quit", "exit", "quit" -> ReplCommand.EXIT;
      case ":clear", "clear" -> ReplCommand.CLEAR;
      case ":reset", "reset" -> ReplCommand.RESET;
      case ":version", "version" -> ReplCommand.VERSION;
      default -> ReplCommand.NONE;
    };
  }

  private static void runReplCommand(
      ReplCommand command, Map<String, Variable> globalScope, Terminal terminal) {
    switch (command) {
      case HELP -> printReplHelp(terminal);
      case CLEAR -> clearReplScreen(terminal);
      case RESET -> {
        globalScope.clear();
        terminal.writer().println("REPL scope reset.");
        terminal.flush();
      }
      case VERSION -> {
        try {
          String[] versions = new ManifestVersionProvider().getVersion();
          terminal.writer().println("ArabicBASIC version: " + versions[0]);
        } catch (Exception e) {
          terminal.writer().println("ArabicBASIC version: unknown");
        }
        terminal.flush();
      }
      case NONE, EXIT -> {
        // handled by caller
      }
    }
  }

  private static void runReplCommandToStdout(ReplCommand command, Map<String, Variable> globalScope) {
    switch (command) {
      case HELP -> {
        System.out.println("Built-in REPL commands:");
        System.out.println("  :help     Show this help");
        System.out.println("  :clear    Clear the screen");
        System.out.println("  :reset    Clear REPL runtime scope");
        System.out.println("  :version  Show interpreter version");
        System.out.println("  :exit     Exit REPL");
        System.out.println("  :quit     Exit REPL");
      }
      case CLEAR -> System.out.print("\u001B[H\u001B[2J");
      case RESET -> {
        globalScope.clear();
        System.out.println("REPL scope reset.");
      }
      case VERSION -> {
        try {
          String[] versions = new ManifestVersionProvider().getVersion();
          System.out.println("ArabicBASIC version: " + versions[0]);
        } catch (Exception e) {
          System.out.println("ArabicBASIC version: unknown");
        }
      }
      case NONE, EXIT -> {
        // handled by caller
      }
    }
  }

  private static void printReplHelp(Terminal terminal) {
    terminal.writer().println("Built-in REPL commands:");
    terminal.writer().println("  :help     Show this help");
    terminal.writer().println("  :clear    Clear the screen");
    terminal.writer().println("  :reset    Clear REPL runtime scope");
    terminal.writer().println("  :version  Show interpreter version");
    terminal.writer().println("  :exit     Exit REPL");
    terminal.writer().println("  :quit     Exit REPL");
    terminal.flush();
  }

  private static void clearReplScreen(Terminal terminal) {
    if (!terminal.puts(InfoCmp.Capability.clear_screen)) {
      terminal.writer().print("\u001B[H\u001B[2J");
    }
    terminal.flush();
  }

  private static String stripDirectionalMarks(String input) {
    // Some terminals inject/retain BiDi controls while editing; keep REPL commands robust.
    return input
        .replace("\u200E", "")
        .replace("\u200F", "")
        .replace("\u061C", "")
        .replace("\u202A", "")
        .replace("\u202B", "")
        .replace("\u202C", "")
        .replace("\u202D", "")
        .replace("\u202E", "")
        .replace("\u2066", "")
        .replace("\u2067", "")
        .replace("\u2068", "")
        .replace("\u2069", "");
  }

  private static String ensureTrailingLineBreak(String input) {
    if (input.endsWith("\n") || input.endsWith("\r")) {
      return input;
    }
    return input + System.lineSeparator();
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

  private enum ReplCommand {
    NONE,
    HELP,
    EXIT,
    CLEAR,
    RESET,
    VERSION
  }
}
