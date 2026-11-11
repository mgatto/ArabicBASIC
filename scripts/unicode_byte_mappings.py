#!/usr/bin/env python3
import unicodedata


def inspect_string(s: str, encoding: str = "utf-8") -> None:
    # In Python 3, every `str` is Unicode.
    # We treat "is unicode" as: contains at least one non-ASCII character.
    is_non_ascii = any(ord(ch) > 127 for ch in s)

    print(f"Input: {s!r}")
    print(f"Contains non-ASCII Unicode characters: {is_non_ascii}")
    print(f"Encoding used for bytes: {encoding}\n")

    print("Per-character breakdown:")
    for i, ch in enumerate(s):
        codepoint = ord(ch)
        utf_bytes = ch.encode(encoding)
        byte_hex = " ".join(f"{b:02X}" for b in utf_bytes)
        try:
            name = unicodedata.name(ch)
        except ValueError:
            name = "<no official Unicode name>"

        print(
            f"[{i}] char={ch!r}  codepoint=U+{codepoint:04X}  "
            f"bytes={byte_hex}  name={name}"
        )

    print("\nWhole-string bytes:")
    full_bytes = s.encode(encoding)
    for i, b in enumerate(full_bytes):
        print(f"byte[{i}] = 0x{b:02X} ({b})")


if __name__ == "__main__":
    text = input("Enter text: ")
    inspect_string(text)
