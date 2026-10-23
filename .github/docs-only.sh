#!/usr/bin/env bash
# Is this change documentation and NOTHING else? Reads the changed paths on
# stdin, one per line, and prints `true` or `false`.
#
# This classifier is what retires `[skip ci]` — the owner's order (2026-10-22):
# "pues no debe existir el skip ci". `[skip ci]` checks nothing: GitHub's
# dispatcher believes whatever the message says, so a commit with three .md AND
# a .scala plus `[skip ci]` lands code on master with no gate having run — and
# shows green, because nothing ran that could go red. It cannot be switched off
# either (it is GitHub's feature, not this repo's), and branch protection
# cannot back the gates up on this plan (measured 2026-10-22: the protection
# API answers `403 Upgrade to GitHub Pro`). So release.yml decides by the FILES
# of the push instead: a documentation-only commit publishes no release on its
# own, and the magic word has nothing left to do.
#
# WHAT COUNTS AS DOCUMENTATION is one line — `_DOCS` — and nothing else: paths
# ending in `.md`. Not build.sbt, not workflows, not code, not samples, not
# data — however the commit message describes them. (README.md was checked
# before being left inside the rule: nothing here builds from it — build.sbt
# never reads a .md and no packaging step copies one.)
#
# It FAILS TOWARD RUNNING. An empty list is NOT documentation-only, because an
# empty list is also what a diff that could not be computed produces, and "no
# files changed" is indistinguishable here from "I could not tell". A false
# "docs only" leaves a merge unpublished and unmeasured; a false "has code"
# spends some GitHub-hosted runner minutes. Those are not comparable.
#
# The shape is lexboe's (its deploy.yml `decide` job, in production since
# 2026-08-31), copied rather than reinvented, and the same two files ship in
# craftplanes. `.github/docs-only-test.sh` runs THIS file against the control
# cases as the first step of every `decide` job, so what is tested cannot
# drift from what ships.
set -u
CHANGED=$(cat)
_DOCS='\.md$'
docs_only=true
[ -z "$CHANGED" ] && docs_only=false
while IFS= read -r path; do
  [ -z "$path" ] && continue
  printf '%s' "$path" | grep -qE "$_DOCS" || docs_only=false
done <<EOF
$CHANGED
EOF
echo "$docs_only"
