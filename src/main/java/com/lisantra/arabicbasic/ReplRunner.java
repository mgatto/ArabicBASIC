package com.lisantra.arabicbasic;

import org.antlr.v4.runtime.CharStreams;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.EndOfFileException;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Reference;
import org.jline.reader.UserInterruptException;
import org.jline.reader.Widget;
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
  // Keep editable input line RTL-biased and free of ASCII prompt text.
  private static final String RTL_MARK = "\u200F";
  private static final String ALM_MARK = "\u061C";
  private static final String RLI_MARK = "\u2067";
  private static final String REPL_INPUT_MARKER_MODE_PROPERTY = "arabicbasic.repl.input.marker";
  private static final String DEFAULT_REPL_HISTORY_FILE = ".arabicbasic_repl_history";
  private static final String REPL_HISTORY_OVERRIDE_PROPERTY = "arabicbasic.repl.history.path";
  private static final String REPL_PROMPT_HEADER = "arabicbasic>";
  private static final String REPL_INPUT_PREFIX = resolveReplInputPrefix();
  private static final String REPL_WELCOME = "ArabicBASIC REPL. Type :exit to quit.";
  private static final String BIDI_NEUTRAL_WIDGET = "bidi-neutral-insert";

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
          terminal.writer().println(REPL_PROMPT_HEADER);
          terminal.flush();
          input = reader.readLine(REPL_INPUT_PREFIX);
        } catch (UserInterruptException e) {
          continue;
        } catch (EndOfFileException e) {
          break;
        }

        String inputWithBidiMarks = addRtlMarksAroundNeutralOperators(input);
        String trimmed = inputWithBidiMarks.trim();
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
          String sanitizedInput = BidiControlSanitizer.stripDirectionalMarks(inputWithBidiMarks);
          int result =
              ExecutionEngine.runSource(
                  CharStreams.fromString(ensureTrailingLineBreak(sanitizedInput)),
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
        System.out.println(REPL_PROMPT_HEADER);
        System.out.print(REPL_INPUT_PREFIX);
        String input = reader.readLine();
        if (input == null) {
          break;
        }

        String inputWithBidiMarks = addRtlMarksAroundNeutralOperators(input);
        historyLines.add(inputWithBidiMarks);
        String trimmed = inputWithBidiMarks.trim();
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
          String sanitizedInput = BidiControlSanitizer.stripDirectionalMarks(inputWithBidiMarks);
          int result =
              ExecutionEngine.runSource(
                  CharStreams.fromString(ensureTrailingLineBreak(sanitizedInput)),
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
    LineReader lineReader =
        LineReaderBuilder.builder()
            .terminal(terminal)
            .appName("arabicbasic")
            .highlighter(new ReplSyntaxHighlighter())
            .variable(LineReader.HISTORY_FILE, normalizedHistoryPath)
            .build();
    configureBidiOperatorWidget(lineReader);
    return lineReader;
  }

  private static void configureBidiOperatorWidget(LineReader lineReader) {
    Map<String, Widget> widgets = lineReader.getWidgets();
    Widget selfInsert = widgets.get(LineReader.SELF_INSERT);
    if (selfInsert == null) {
      return;
    }

    widgets.put(
        BIDI_NEUTRAL_WIDGET,
        () -> {
          String key = lineReader.getLastBinding();
          if (key == null || key.isEmpty()) {
            return selfInsert.apply();
          }
          int cp = key.codePointAt(0);
          if (!shouldInjectDirectionalMarks(cp, lineReader)) {
            return selfInsert.apply();
          }
          lineReader.getBuffer().write(RTL_MARK + new String(Character.toChars(cp)) + RTL_MARK);
          return true;
        });

    KeyMap<Binding> main = lineReader.getKeyMaps().get(LineReader.MAIN);
    if (main == null) {
      return;
    }
    for (char neutral : new char[] {'=', '+', '-', '*', '/', '<', '>', '^'}) {
      main.bind(new Reference(BIDI_NEUTRAL_WIDGET), String.valueOf(neutral));
    }
  }

  private static boolean shouldInjectDirectionalMarks(int cp, LineReader lineReader) {
    if (!isNeutralOperator(cp)) {
      return false;
    }
    return !isInsideStringLiteralAtCursor(lineReader);
  }

  private static boolean isInsideStringLiteralAtCursor(LineReader lineReader) {
    String line = lineReader.getBuffer().toString();
    int cursor = lineReader.getBuffer().cursor();
    boolean inString = false;
    boolean escaping = false;
    int scanLimit = Math.max(0, Math.min(cursor, line.length()));
    for (int i = 0; i < scanLimit; i++) {
      char ch = line.charAt(i);
      if (inString) {
        if (escaping) {
          escaping = false;
        } else if (ch == '\\') {
          escaping = true;
        } else if (ch == '"') {
          inString = false;
        }
        continue;
      }
      if (ch == '"') {
        inString = true;
      }
    }
    return inString;
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

  private static String resolveReplInputPrefix() {
    String markerMode =
        System.getProperty(REPL_INPUT_MARKER_MODE_PROPERTY, "rli-alm").trim().toLowerCase(Locale.ROOT);
    return switch (markerMode) {
      case "rlm" -> RTL_MARK;
      case "alm" -> ALM_MARK;
      case "rli" -> RLI_MARK;
      default -> RLI_MARK + ALM_MARK;
    };
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
    String normalized = BidiControlSanitizer.stripDirectionalMarks(trimmedInput).toLowerCase(Locale.ROOT);
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

  private static String addRtlMarksAroundNeutralOperators(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    StringBuilder sb = new StringBuilder(input.length() * 2);
    boolean inString = false;
    boolean escaping = false;
    for (int i = 0; i < input.length(); i++) {
      char ch = input.charAt(i);
      if (inString) {
        sb.append(ch);
        if (escaping) {
          escaping = false;
        } else if (ch == '\\') {
          escaping = true;
        } else if (ch == '"') {
          inString = false;
        }
        continue;
      }
      if (ch == '"') {
        inString = true;
        sb.append(ch);
        continue;
      }
      if (isNeutralOperator(ch)) {
        sb.append(RTL_MARK).append(ch).append(RTL_MARK);
      } else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  private static boolean isNeutralOperator(int cp) {
    return switch (cp) {
      case '=', '+', '-', '*', '/', '<', '>', '^' -> true;
      default -> false;
    };
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
