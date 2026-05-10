package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class AppReplModeTest {

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
    assertTrue(result.stdout().contains("arabicbasic> "), () -> "stdout: " + result.stdout());
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

  private static CliRunResult runCli(String[] args, String stdinText) {
    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();
    PrintStream outPrev = System.out;
    PrintStream errPrev = System.err;
    InputStream inPrev = System.in;
    try {
      System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
      System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));
      System.setIn(new ByteArrayInputStream(stdinText.getBytes(StandardCharsets.UTF_8)));
      int exitCode = new CommandLine(new App()).execute(args);
      return new CliRunResult(
          exitCode, stdout.toString(StandardCharsets.UTF_8), stderr.toString(StandardCharsets.UTF_8));
    } finally {
      System.setOut(outPrev);
      System.setErr(errPrev);
      System.setIn(inPrev);
    }
  }

  private record CliRunResult(int exitCode, String stdout, String stderr) {}
}
