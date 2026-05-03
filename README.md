# ArabicBASIC

*الحوسبة الآمنة بلغتك*
*برمجة آمنة بلغتك الأم*
*Safe computing in your own language*

"A programming language should do what you mean"

A standalone **interpreter** for Arabic BASIC: programs use **Arabic keywords and identifiers** (and may use **Eastern Arabic–Indic digits** in numeric literals). The runtime is written in **Java** (ANTLR 4, Picocli) and ships as a JAR for any platform with a **Java 21+** runtime.

The interpreter sets the JVM default locale to **Arabic** with the **`ar-u-nu-arab`** numbering extension so numeric formatting and related locale behavior align with Arabic contexts.

A native executable for macOS (and later Windows/Linux) may follow once the build pipeline is in place.

---

## Requirements

- **Java 21 or higher** on your `PATH`
- Scripts saved as **UTF-8** (without BOM is fine). The lexer reads the source file as Unicode text.

Helpful links:

- OpenJDK install: https://openjdk.org/install/
- Command-line basics: https://tutorial.djangogirls.org/en/intro_to_command_line/

---

## Language and samples

For a full walkthrough of syntax and features, see **[docs/tutorial-en.md](docs/tutorial-en.md)**.

The grammar is split between `src/main/antlr4/ArabicBASIC.g4` (parser rules) and `src/main/antlr4/ArabicBASICLexer.g4` (lexer/token rules). Together they define the current syntax—for example assignments with **صار**, conditionals with **اذا** / **ثم** / **ختام اذا**, **اطبع** for output, **ادخل** for input, **مصفوفة(...)** for array construction, **مكدس()** + `ادفع`/`اسحب`/`انظر`/`فارغ؟` for stacks, and Arabic **IDENTIFIER** tokens.

Boolean and predicate wording conventions are documented in [`docs/boolean-style-guide.md`](docs/boolean-style-guide.md) (for example, canonical `صحيح` / `خطأ` literals and `...؟` predicate naming).

Accepted orthographic aliases are intentionally limited. The parser accepts common variants such as `إذا`/`اذا`, `أو`/`او`/`ام`, `دالة`/`دالّة`, `مصفوفه`/`مصفوفة`, imperative variants like `أدخل`/`ادخل`, `إطبع`/`اطبع`, `أنظر`/`انظر`, plus equal adjectival/predicate pairs `التالي`/`التالية` and `فارغ؟`/`فارغة؟` (Arabic `؟` only).

**Working examples** that match the current parser live under **`examples/`** (see [`examples/README.md`](examples/README.md)), alongside older numbered demos:

| Scripts (Arabic syntax) | Rough purpose |
|-------------------------|----------------|
| `examples/CONDITIONAL_Ar.bas` | Simple `اذا` / `ختام اذا` |
| `examples/INPUT_Ar.bas` | `ادخل`, loop, array |
| `examples/LOOP_Ar.bas` | Loop-style sample |
| `examples/STACK_Ar.bas` | Native stack builtins: `مكدس`, `ادفع`, `اسحب`, `انظر`, `فارغ؟` |

Many **`test_*.bas`** files in **`examples/`** still use **legacy Latin** keywords (`LET`, `IF`, `PRINT`, …). They do **not** match the current Arabic grammar; use them only with a build that still exposes that syntax, or after updating them to the Arabic forms.

---

## Running from the pre-built JAR

A JAR may be provided under **`release/`** (for example `ArabicBASIC-1.0.0.jar`). From the **repository root**, if that artifact is a single “fat” JAR with dependencies, you can run:

```bash
java -cp release/ArabicBASIC-1.0.0.jar com.lisantra.arabicbasic.App examples/CONDITIONAL_Ar.bas
```

Enable debug output (parse/runtime diagnostics and end-of-run scope dump when implemented for your build):

```bash
java -cp release/ArabicBASIC-1.0.0.jar com.lisantra.arabicbasic.App --debug examples/CONDITIONAL_Ar.bas
```

CLI help:

```bash
java -cp release/ArabicBASIC-1.0.0.jar com.lisantra.arabicbasic.App -h
```

If `java` reports missing classes for ANTLR or Picocli, build the **jar-with-dependencies** from source (next section) and use `java -jar` instead of `-cp` with a thin JAR.

---

## Building from source (recommended for latest grammar)

This project uses **Maven**. From the repository root:

```bash
mvn clean package
```

That produces a dependency-fat JAR:

```text
target/ArabicBASIC-0.7.0-release-jar-with-dependencies.jar
```

(The version segment matches `<version>` in `pom.xml`.)

Run an Arabic script:

```bash
java -jar target/ArabicBASIC-0.7.0-release-jar-with-dependencies.jar examples/CONDITIONAL_Ar.bas
```

With debug:

```bash
java -jar target/ArabicBASIC-0.7.0-release-jar-with-dependencies.jar --debug examples/INPUT_Ar.bas
```

Automated smoke tests (`examples/` Arabic scripts) run with:

```bash
mvn test
```

### In an IDE

Use the IDE’s Maven integration and run the **`package`** lifecycle on the project, then run the same `java -jar …` command in a terminal, or configure a run configuration with main class **`com.lisantra.arabicbasic.App`** and the path to your `.bas` file as the first program argument.

---

## What to expect

- **Normal mode:** Program output goes to **stdout**; errors to **stderr**.
- **Debug mode (`-d` / `--debug`):** Extra diagnostics (e.g. ambiguous-parse warnings via ANTLR’s diagnostic listener when enabled in code) and, when wired in your tree, a dump of the global variable map after a successful run.

Variables are global and mutable like classic BASIC; the Arabic samples illustrate typical usage.

---

<!-- ## Agent / contributor notes

For build commands, layout, ANTLR regeneration, and verification checklists, see **[AGENTS.md](AGENTS.md)**. -->
