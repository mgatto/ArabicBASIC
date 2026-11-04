# ArabicBASIC examples

UTF-8 **`.bas`** scripts for manual runs and automated smoke tests.

| Files | Notes |
|-------|--------|
| `CONDITIONAL_Ar.bas`, `INPUT_Ar.bas`, `LOOP_Ar.bas` | **Arabic** syntax — match the current grammar. |
| `test_*.bas` | **Legacy Latin** keywords (`LET`, `IF`, …) — kept for reference; they **do not** parse with the current Arabic lexer/parser. |

Run from the repo root after `mvn package`:

```bash
java -jar target/ArabicBASIC-*-jar-with-dependencies.jar examples/CONDITIONAL_Ar.bas
```

(The glob matches the fat JAR whose version follows `<version>` in `pom.xml`.)
