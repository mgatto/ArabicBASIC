#!/usr/bin/env bash
# Helper for the Maven `release` profile (see pom.xml and AGENTS.md "Releasing").
# Not meant to be run directly; invoked by exec-maven-plugin in this order:
#   check   -> refuse to release with a dirty tree
#   props   -> after semantic-version: write target/release.properties
#   commit  -> after git-changelog: commit pom.xml + CHANGELOG.md and tag
set -euo pipefail

cd "$(dirname "$0")/.."

# Latest stable release tag (v<major>.<minor>.<patch>); pre-release tags such as
# v0.2.0-alpha are ignored.
last_tag() {
  git describe --tags --abbrev=0 --match 'v[0-9]*.[0-9]*.[0-9]*' --exclude 'v*-*' 2>/dev/null || true
}

# Project version: first top-level <version> in pom.xml.
pom_version() {
  sed -n 's|^    <version>\(.*\)</version>.*|\1|p' pom.xml | head -1
}

case "${1:-}" in
  check)
    if [ -z "$(last_tag)" ]; then
      echo "release: no baseline tag found. Tag the current release first, e.g. git tag v0.7.3" >&2
      exit 1
    fi
    if ! git diff --quiet HEAD --; then
      echo "release: tracked files have uncommitted changes; commit or stash them first." >&2
      exit 1
    fi
    ;;
  props)
    version="$(pom_version)"
    from="$(last_tag)"
    if [ "v${version}" = "${from}" ]; then
      # semantic-version found nothing to bump; undo its (formatting-only) pom rewrite.
      git checkout -- pom.xml
      echo "release: no feat/fix/breaking commits since ${from}; nothing to release." >&2
      exit 1
    fi
    mkdir -p target
    {
      echo "release.version=${version}"
      echo "release.tag=v${version}"
      echo "release.from=${from}"
      echo "release.date=$(date +%Y-%m-%d)"
    } > target/release.properties
    ;;
  commit)
    version="$(pom_version)"
    git add pom.xml CHANGELOG.md
    git commit -m "chore(release): ${version}"
    git tag -a "v${version}" -m "chore(release): ${version}"
    echo "release: committed and tagged v${version}. Push with: git push --follow-tags"
    ;;
  *)
    echo "usage: $0 check|props|commit" >&2
    exit 2
    ;;
esac
