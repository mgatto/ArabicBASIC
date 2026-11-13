package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

class AppReplModeTest {
  private static final String REPL_PROMPT_HEADER = "arabicbasic>";
  private static final String RLI_MARK = "\u2067";
  private static final String ALM_MARK = "\u061C";
  private static final String REPL_INPUT_PREFIX = RLI_MARK + ALM_MARK;
  private static final String HISTORY_PROP = "arabicbasic.repl.history.path";

  @TempDir Path tempDir;

  @Test
  void noArgsStartsReplMode() {
    CliRunResult result = runCli(new String[] {}, ":quit\n");

    assertEquals(0, result.exitCode());
    assertTrue(result.stdout().contains("ArabicBASIC REPL"), () -> "stdout: " + result.stdout());
    assertFalse(
        result.stderr().contains("Missing required parameter"), () -> "stderr: " + result.stderr());
  }

  @Test
  void explicitReplFlagStartsReplMode() {
    CliRunResult result = runCli(new String[] {"--repl"}, ":quit\n");

    assertEquals(0, result.exitCode());
    assertTrue(result.stdout().contains(REPL_PROMPT_HEADER), () -> "stdout: " + result.stdout());
    assertTrue(result.stdout().contains(REPL_INPUT_PREFIX), () -> "stdout: " + result.stdout());
  }

  @Test
  void replFlagTakesPrecedenceOverScriptPath() {
    CliRunResult result = runCli(new String[] {"--repl", "missing-script.bas"}, ":quit\n");

    assertEquals(0, result.exitCode());
    assertTrue(result.stdout().contains("ArabicBASIC REPL"), () -> "stdout: " + result.stdout());
  }

  @Test
  void replInvalidInputDoesNotCrashSession() {
    CliRunResult result = runCli(new String[] {"--repl"}, "(\n:quit\n");

    assertEquals(0, result.exitCode());
    assertTrue(result.stderr().contains("REPL input had parse errors."), () -> "stderr: " + result.stderr());
  }

  @Test
  void replExitCommandAllowsDirectionalMarks() {
    CliRunResult result = runCli(new String[] {"--repl"}, "\u200F:exit\u061C\n");

    assertEquals(0, result.exitCode());
    assertFalse(result.stderr().contains("REPL input had parse errors."), () -> "stderr: " + result.stderr());
  }

  @Test
  void replAssignmentAcceptsDirectionalMarksAroundOperator() {
    CliRunResult result = runCli(new String[] {"--repl"}, "صار ن \u200F=\u200E ٦\n:quit\n");

    assertEquals(0, result.exitCode());
    assertFalse(result.stderr().contains("REPL input had parse errors."), () -> "stderr: " + result.stderr());
  }

  @Test
  void replHelpCommandPrintsBuiltIns() {
    CliRunResult result = runCli(new String[] {"--repl"}, ":help\n:quit\n");

    assertEquals(0, result.exitCode());
    assertTrue(result.stdout().contains("Built-in REPL commands"), () -> "stdout: " + result.stdout());
    assertTrue(result.stdout().contains(":clear"), () -> "stdout: " + result.stdout());
    assertTrue(result.stdout().contains(":reset"), () -> "stdout: " + result.stdout());
  }

  @Test
  void replVersionCommandPrintsVersionLine() {
    CliRunResult result = runCli(new String[] {"--repl"}, ":version\n:quit\n");

    assertEquals(0, result.exitCode());
    assertTrue(result.stdout().contains("ArabicBASIC version:"), () -> "stdout: " + result.stdout());
  }

  @Test
  void replResetCommandClearsScope() {
    CliRunResult result =
        runCli(new String[] {"--repl"}, "صار ن = ٦\n:reset\nاطبع ن\n:quit\n");

    assertEquals(0, result.exitCode());
    assertTrue(result.stdout().contains("REPL scope reset."), () -> "stdout: " + result.stdout());
    assertFalse(result.stderr().isBlank(), () -> "stderr should not be blank after scope reset");
  }

  @Test
  void replHistoryPersistsAcrossSessions() throws Exception {
    Path historyPath = tempDir.resolve("history-file");
    runCliWithHistoryPath(new String[] {"--repl"}, ":help\n:quit\n", historyPath);
    runCliWithHistoryPath(new String[] {"--repl"}, ":version\n:quit\n", historyPath);

    String historyData = Files.readString(historyPath, StandardCharsets.UTF_8);
    assertTrue(historyData.contains(":help"), () -> "history: " + historyData);
    assertTrue(historyData.contains(":version"), () -> "history: " + historyData);
  }

  private static CliRunResult runCli(String[] args, String stdinText) {
    return runCliWithHistoryPath(args, stdinText, null);
  }

  private static CliRunResult runCliWithHistoryPath(
      String[] args, String stdinText, Path historyPathOverride) {
    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();
    PrintStream outPrev = System.out;
    PrintStream errPrev = System.err;
    InputStream inPrev = System.in;
    String historyPrev = System.getProperty(HISTORY_PROP);
    try {
      System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
      System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));
      System.setIn(new ByteArrayInputStream(stdinText.getBytes(StandardCharsets.UTF_8)));
      if (historyPathOverride != null) {
        System.setProperty(HISTORY_PROP, historyPathOverride.toString());
      } else {
        System.clearProperty(HISTORY_PROP);
      }
      int exitCode = new CommandLine(new App()).execute(args);
      return new CliRunResult(
          exitCode, stdout.toString(StandardCharsets.UTF_8), stderr.toString(StandardCharsets.UTF_8));
    } finally {
      if (historyPrev != null) {
        System.setProperty(HISTORY_PROP, historyPrev);
      } else {
        System.clearProperty(HISTORY_PROP);
      }
      System.setOut(outPrev);
      System.setErr(errPrev);
      System.setIn(inPrev);
    }
  }

  private record CliRunResult(int exitCode, String stdout, String stderr) {}
}
