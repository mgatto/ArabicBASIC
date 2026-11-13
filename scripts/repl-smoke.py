#!/usr/bin/env python3
"""Smoke-test the interactive REPL (JLine) of a native executable under a pseudo-TTY.

The REPL only uses JLine when System.console() != null, so a plain pipe never
reaches that code. This script runs `<exe> --repl` on a pty, types `:version` and
`:exit`, and checks that

  * the REPL answered `:version` and exited with status 0, and
  * no Java exception / Native Image `UnsupportedFeatureError` reached the terminal.

Usage: scripts/repl-smoke.py target/arabicbasic [extra args...]
POSIX only (macOS / Linux); skipped on Windows by the workflow.
"""

import fcntl
import os
import pty
import select
import struct
import sys
import tempfile
import termios
import time

PROMPT = b"arabicbasic>"
TIMEOUT = 20.0


def main() -> int:
    if len(sys.argv) < 2:
        print(__doc__)
        return 2
    exe = sys.argv[1]
    history = os.path.join(tempfile.mkdtemp(prefix="arabicbasic-repl-"), "history")
    argv = [exe, f"-Darabicbasic.repl.history.path={history}", "--repl", *sys.argv[2:]]

    env = dict(os.environ, TERM="xterm-256color")
    pid, fd = pty.fork()
    if pid == 0:
        os.execve(exe, argv, env)

    fcntl.ioctl(fd, termios.TIOCSWINSZ, struct.pack("HHHH", 24, 80, 0, 0))
    out = bytearray()

    def read_until(marker: bytes, count: int = 1) -> bool:
        """Read pty output until `marker` has appeared `count` times (or fail)."""
        deadline = time.time() + TIMEOUT
        while out.count(marker) < count:
            if time.time() > deadline:
                return False
            ready, _, _ = select.select([fd], [], [], 0.2)
            if not ready:
                continue
            try:
                data = os.read(fd, 4096)
            except OSError:  # EIO: child closed the pty
                return out.count(marker) >= count
            if not data:
                return out.count(marker) >= count
            out.extend(data)
        return True

    def send(text: bytes) -> bool:
        try:
            os.write(fd, text)
            return True
        except OSError:  # child already exited
            return False

    ok = read_until(PROMPT)
    # Give JLine time to switch the pty to raw mode before typing.
    time.sleep(1.0)
    ok = ok and send(b":version\r") and read_until(b"ArabicBASIC version")
    # Second prompt proves readLine() returned (signal handler unregistered) and looped.
    ok = ok and read_until(PROMPT, 2)
    time.sleep(0.5)
    ok = ok and send(b":exit\r")
    read_until(b"\0")  # drain until EOF or timeout

    try:
        os.close(fd)
    except OSError:
        pass
    deadline = time.time() + 5
    status = None
    while time.time() < deadline:
        done, raw = os.waitpid(pid, os.WNOHANG)
        if done:
            status = os.waitstatus_to_exitcode(raw)
            break
        time.sleep(0.1)
    if status is None:
        os.kill(pid, 9)
        os.waitpid(pid, 0)

    text = out.decode("utf-8", "replace")
    print(text)
    print(f"--- exit status: {status}")

    problems = []
    if not ok:
        problems.append(
            "REPL did not respond to ':version' (exited early or timed out)"
        )
    if status != 0:
        problems.append(f"exit status {status}, expected 0")
    for needle in ("Exception", "UnsupportedFeatureError", "Support for "):
        if needle in text:
            problems.append(f"output contains '{needle}'")
    if problems:
        print("REPL smoke test FAILED:\n  " + "\n  ".join(problems), file=sys.stderr)
        return 1
    print("REPL smoke test passed")
    return 0


if __name__ == "__main__":
    sys.exit(main())
