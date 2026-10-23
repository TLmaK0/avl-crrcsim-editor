#!/usr/bin/env bash
# The guard for .github/docs-only.sh. Run as the FIRST step of every `decide`
# job, before the verdict is computed: a run never trusts a classifier whose
# control cases stopped holding. The cases are lifted from lexboe's
# backend/tests/test_deploy_docs_only_guard.py, and the red ones are the point:
# a classifier can go green for the wrong reason.
set -u
SCRIPT="$(cd "$(dirname "$0")" && pwd)/docs-only.sh"
fails=0

expect() { # expect <true|false> <why> <path>...
  want="$1"
  why="$2"
  shift 2
  got=$(printf '%s\n' "$@" | bash "$SCRIPT")
  if [ "$got" = "$want" ]; then
    echo "ok   ($want) $why"
  else
    echo "FAIL (wanted $want, got $got) $why"
    fails=$((fails + 1))
  fi
}

# --- documentation and nothing else: no build, no release ----------------------
expect true "a single document" \
  "AGENTS.md"
expect true "several documents at once" \
  "AGENTS.md" "docs/xfoil-analysis.md" "CLAUDE.md"
expect true "README alone is documentation (checked: nothing in this repo builds from it)" \
  "README.md"

# --- everything MUST run: build AND release ------------------------------------
expect false "a code file" \
  "src/main/scala/com/abajar/avleditor/Main.scala"
expect false "documentation mixed with code: the exact [skip ci] hole this closes" \
  "AGENTS.md" "docs/a.md" "CLAUDE.md" "src/main/scala/com/abajar/avleditor/XfoilManager.scala"
expect false "an empty change set: unknowable fails toward running" \
  ""
expect false "a markdown lookalike: the extension decides, not a substring" \
  "docs/README.md.j2"
expect false "the build definition" \
  "build.sbt"
expect false "a build plugin" \
  "project/plugins.sbt"
expect false "a workflow, including the one that carries this rule" \
  ".github/workflows/release.yml"
expect false "this classifier itself" \
  ".github/docs-only.sh"
expect false "a sample aircraft: data, not documentation" \
  "samples/eurofighter.avl"

if [ "$fails" -ne 0 ]; then
  echo "$fails control case(s) no longer hold: the classifier cannot be trusted in either direction." >&2
  exit 1
fi
echo "every control case holds"
