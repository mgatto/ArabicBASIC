#!/usr/bin/env bash
set -u

# Ensures PNG signatures have the expected owner/group and mode.
# Cron example (every 5 minutes):
# */5 * * * * /bin/bash /path/to/fix_signature_png_permissions.sh >> /var/log/fix_signature_png_permissions.log 2>&1

TARGET_DIR="/mojoe/signatures"
EXPECTED_OWNER_GROUP="lucee:apache"
EXPECTED_MODE="755"

if [[ ! -d "${TARGET_DIR}" ]]; then
  echo "ERROR: Target directory does not exist: ${TARGET_DIR}" >&2
  exit 1
fi

cd "${TARGET_DIR}" || {
  echo "ERROR: Unable to cd to ${TARGET_DIR}" >&2
  exit 1
}

CURRENT_DIR="$(pwd -P)"
if [[ "${CURRENT_DIR}" != "${TARGET_DIR}" ]]; then
  echo "ERROR: Working directory mismatch. Expected ${TARGET_DIR}, got ${CURRENT_DIR}" >&2
  exit 1
fi

if stat -c '%U:%G' . >/dev/null 2>&1; then
  stat_owner_group() {
    stat -c '%U:%G' "$1"
  }

  stat_mode() {
    stat -c '%a' "$1"
  }
elif stat -f '%Su:%Sg' . >/dev/null 2>&1; then
  stat_owner_group() {
    stat -f '%Su:%Sg' "$1"
  }

  stat_mode() {
    stat -f '%OLp' "$1"
  }
else
  echo "ERROR: Unsupported 'stat' command format on this system." >&2
  exit 1
fi

total_scanned=0
changed_count=0
correct_count=0
failure_count=0

# Recursively process one child directory level under /mojoe/signatures.
while IFS= read -r -d '' png_file; do
  ((total_scanned += 1))

  current_owner_group="$(stat_owner_group "${png_file}")" || {
    echo "ERROR: Could not read owner/group for ${png_file}" >&2
    ((failure_count += 1))
    continue
  }

  current_mode="$(stat_mode "${png_file}")" || {
    echo "ERROR: Could not read mode for ${png_file}" >&2
    ((failure_count += 1))
    continue
  }

  needs_change=0
  if [[ "${current_owner_group}" != "${EXPECTED_OWNER_GROUP}" ]]; then
    needs_change=1
    if ! chown "${EXPECTED_OWNER_GROUP}" "${png_file}"; then
      echo "ERROR: chown failed for ${png_file}" >&2
      ((failure_count += 1))
    fi
  fi

  if [[ "${current_mode}" != "${EXPECTED_MODE}" ]]; then
    needs_change=1
    if ! chmod "${EXPECTED_MODE}" "${png_file}"; then
      echo "ERROR: chmod failed for ${png_file}" >&2
      ((failure_count += 1))
    fi
  fi

  if [[ ${needs_change} -eq 1 ]]; then
    ((changed_count += 1))
  else
    ((correct_count += 1))
  fi
done < <(find . -mindepth 2 -maxdepth 2 -type f -name '*.png' -print0)

echo "directory=${TARGET_DIR} scanned=${total_scanned} changed=${changed_count} correct=${correct_count} failures=${failure_count}"

if [[ ${failure_count} -gt 0 ]]; then
  exit 2
fi

exit 0
