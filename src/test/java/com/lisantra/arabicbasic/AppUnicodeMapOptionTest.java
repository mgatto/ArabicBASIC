package com.lisantra.arabicbasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class AppUnicodeMapOptionTest {

  @Test
  void unicodeMapLongFlag_printsMappingAndExitsZero() throws Exception {
    Path unicodeFile = Files.createTempFile("unicode-map", ".txt");
    Files.writeString(unicodeFile, "a");

    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    ByteArrayOutputStream stderr = new ByteArrayOutputStream();
    PrintStream outPrev = System.out;
    PrintStream errPrev = System.err;
    try {
      System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
      System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));
      int exitCode = new CommandLine(new App()).execute("--unicode-map", unicodeFile.toString());
      String out = stdout.toString(StandardCharsets.UTF_8);

      assertEquals(0, exitCode);
      assertTrue(out.contains("LATIN SMALL LETTER A"), () -> "stdout: " + out);
      assertTrue(stderr.toString(StandardCharsets.UTF_8).isBlank());
    } finally {
      System.setOut(outPrev);
      System.setErr(errPrev);
      Files.deleteIfExists(unicodeFile);
    }
  }

  @Test
  void unicodeMapTakesPrecedenceOverScriptPath() throws Exception {
    Path unicodeFile = Files.createTempFile("unicode-map", ".txt");
    Files.writeString(unicodeFile, "ب");

    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    PrintStream outPrev = System.out;
    try {
      System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
      int exitCode =
          new CommandLine(new App())
              .execute("this-script-path-should-be-ignored.bas", "-u", unicodeFile.toString());
      String out = stdout.toString(StandardCharsets.UTF_8);

      assertEquals(0, exitCode);
      assertTrue(out.contains("ARABIC LETTER BEH"), () -> "stdout: " + out);
      assertTrue(!out.contains("نعم"), () -> "stdout: " + out);
    } finally {
      System.setOut(outPrev);
      Files.deleteIfExists(unicodeFile);
    }
  }

  @Test
  void unicodeMapMissingFile_returnsNonZero() {
    int exitCode =
        new CommandLine(new App()).execute("--unicode-map", "missing-file-for-unicode-map.txt");

    assertTrue(exitCode != 0);
  }
}
