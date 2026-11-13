package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppScriptDirectionalMarksTest {
  @TempDir Path tempDir;

  @Test
  void scriptAssignmentAcceptsDirectionalMarksAroundOperator() throws Exception {
    Path script = tempDir.resolve("directional-assignment.bas");
    Files.writeString(script, "صار ن \u200F=\u200E ٦\nاطبع ن\n", StandardCharsets.UTF_8);

    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();
    PrintStream outPrev = System.out;
    PrintStream errPrev = System.err;
    try {
      System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
      System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));
      int code = App.runScript(script, false);
      assertEquals(0, code, () -> "stderr: " + stderr.toString(StandardCharsets.UTF_8));
      assertTrue(stdout.toString(StandardCharsets.UTF_8).contains("٦"), () -> "stdout: " + stdout);
    } finally {
      System.setOut(outPrev);
      System.setErr(errPrev);
    }
  }
}
