package com.lisantra.arabicbasic;

import org.antlr.v4.runtime.CharStreams;
import org.jline.reader.EndOfFileException;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class ReplRunner {
  // Isolate the ASCII prompt as LTR, then bias user input direction to RTL.
  private static final String LTR_ISOLATE = "\u2066";
  private static final String POP_DIRECTIONAL_ISOLATE = "\u2069";
  private static final String RTL_MARK = "\u200F";
  private static final String DEFAULT_REPL_HISTORY_FILE = ".arabicbasic_repl_history";
  private static final String REPL_HISTORY_OVERRIDE_PROPERTY = "arabicbasic.repl.history.path";
  private static final String REPL_PROMPT =
      LTR_ISOLATE + "arabicbasic> " + POP_DIRECTIONAL_ISOLATE + RTL_MARK;
  private static final String REPL_WELCOME = "ArabicBASIC REPL. Type :exit to quit.";

  private ReplRunner() {}

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
              ExecutionEngine.runSource(
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
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
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
              ExecutionEngine.runSource(
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

  private static Terminal createReplTerminal() throws Exception {
    return TerminalBuilder.builder().encoding(StandardCharsets.UTF_8).build();
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

  private static void runReplCommandToStdout(
      ReplCommand command, Map<String, Variable> globalScope) {
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

  private enum ReplCommand {
    NONE,
    HELP,
    EXIT,
    CLEAR,
    RESET,
    VERSION
  }
}
