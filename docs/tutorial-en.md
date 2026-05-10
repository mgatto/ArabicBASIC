# ArabicBASIC — Programmer's Quick Reference

A short tutorial for English speakers on how to write programs in ArabicBASIC. Save your files as **UTF-8** with the extension `.bas` and run them with:

```bash
java -jar target/ArabicBASIC-0.7.2-jar-with-dependencies.jar myscript.bas
```

---

## 1. Encoding and script

- Source files are **UTF-8**.
- All keywords are **Arabic**. Identifiers must start with an Arabic-script letter and may include Arabic letters, digits (Western `0–9` or Eastern Arabic-Indic `٠–٩`), and underscores.
- Both Western (`1 2 3`) and Eastern Arabic-Indic (`١ ٢ ٣`) numerals are accepted in literals.
- Comments begin with `//` and run to end of line.

```
// هذا تعليق — this is a comment
```

---

## 2. Variables and assignment

The keyword **`صار`** (meaning "became") declares and assigns a variable. Multiple variables can be assigned on one line.

```
صار س = ٥
صار أ, ب = ١٠
```

> There are no explicit type declarations. A variable's type is determined by the value assigned to it.

---

## 3. Values and expressions

| Type    | Example             | Notes                            |
|---------|---------------------|----------------------------------|
| Integer | `٣`, `42`           | Western or Eastern Arabic digits |
| Real    | `٣.١٤`, `3.14`      | Decimal point or Arabic decimal separator `٫` |
| String  | `"مرحبا"`           | Double-quoted UTF-8 text         |
| Boolean | `صحيح` / `خطأ`      | True / False                     |

**Arithmetic operators** (standard precedence, `^` for power):

```
صار ن = (٢ + ٣) * ٤ ^ ٢
صار م = ١٠ MOD ٣
```

---

## 4. Output — `اطبع`

Print one or more values, separated by commas or semicolons (`,` or `،` or `;`):

```
اطبع "النتيجة هي" , ن
اطبع أ ؛ ب
```

---

## 5. Input — `ادخل`

Read from the user into one or more variables. An optional prompt string is supported:

```
ادخل "أدخل اسمك"، اسم
ادخل س
```

---

## 6. Conditionals

**Single-line:**

```
اذا س > ١٠ ثم اطبع "كبير"
```

**Block form** — the block closes with **`ختام اذا`**:

```
اذا ن < ١٤ ثم
    اطبع "صغير"
وإلا اذا ن < ٢٠ ثم
    اطبع "متوسط"
وإلا
    اطبع "كبير"
ختام اذا
```

**Boolean operators:**

| Operator | Arabic keyword |
|----------|----------------|
| AND      | `ايضاً`        |
| OR       | `ام`           |
| NOT      | `ليس`          |

Comparison operators: `=` `<>` `<` `>` `<=` `>=`

```
اذا ليس س = ٠ ايضاً ن > ٥ ثم
    اطبع "شرط صحيح"
ختام اذا
```

---

## 7. For loop — `لكل`

```
لكل ص = ١ حتى ١٠
    اطبع ص
ص التالي
```

With an optional step:

```
لكل ص = ٠ حتى ٢٠ درجة = ٢
    اطبع ص
ص التالي
```

The closing adjective `التالي` (or its feminine form `التالية`) must come after the control variable name.

---

## 8. While loop — `طالما`

```
صار ع = ١
طالما ع <= ٥
    اطبع ع
    صار ع = ع + ١
ختام طالما
```

---

## 9. Arrays — `مصفوفة`

Create an array of a given size with **`مصفوفة(...)`**. Index with `[...]` (zero-based):

```
صار ق = مصفوفة(٥)
ق[٠] = "أول"
ق[١] = ٩٩
اطبع ق[٠]
```

---

## 10. Functions — `دالّة` / `اجري`

Single-line functions only. Define with **`دالّة`**, call with **`اجري`**:

```
دالّة مربع(س) = س ^ ٢
صار ن = اجري مربع(٤)
اطبع ن
```

---

## 11. Stacks — `مكدس`

Create a stack with **`مكدس()`** and operate on it with the imperative builtins:

| Operation | Keyword           | Returns              |
|-----------|-------------------|----------------------|
| Push      | `ادفع(س, قيمة)`   | New size (integer)   |
| Pop       | `اسحب(س)`         | Removed top element  |
| Peek      | `انظر(س)`         | Top element (no pop) |
| Empty?    | `فارغ؟(س)`        | `صحيح` or `خطأ`     |

```
صار س = مكدس()
ادفع(س, "أول")
ادفع(س, "ثاني")
اطبع انظر(س)           // ثاني
اطبع اسحب(س)           // ثاني
اطبع فارغ؟(س)          // خطأ
```

---

## 12. Built-in functions

**Math** (standard names):

```
ABS(س)  COS(س)  SIN(س)  TAN(س)  LOG(س)  EXP(س)  INT(س)  SQR(س)  RND(س)
```

**String:**

```
LEN("مرحبا")           // 5
LEFT("سلام", ٢)        // "سل"
RIGHT("سلام", ٢)       // "ام"
MID("سلام", ١, ٢)      // "لا"
CHR(٦٥)               // "A"
ORD("A")              // 65
```

---

## 13. Accepted spelling variants

The parser accepts these equal alternative forms for convenience:

| Canonical  | Also accepted   |
|------------|-----------------|
| `اذا`      | `إذا`           |
| `اطبع`     | `إطبع`          |
| `ادخل`     | `أدخل`          |
| `ام`       | `او` / `أو`     |
| `دالّة`    | `دالة`          |
| `مصفوفة`   | `مصفوفه`        |
| `التالي`   | `التالية`       |
| `فارغ؟`    | `فارغة؟`        |

Both Arabic comma `،` and ASCII comma `,` are accepted everywhere commas appear.
