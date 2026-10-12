#!/bin/bash
# PostToolUse: roda depois de cada Edit ou Write.
# Olha só as linhas ADICIONADAS nos .kt e cobra as regras do code-style.md.
# exit 2 aqui não desfaz a edição, mas a mensagem volta pro Claude corrigir.

cd "$CLAUDE_PROJECT_DIR" || exit 0

ADDED=$(git diff -U0 -- '*.kt' | grep '^+[^+]')

if echo "$ADDED" | grep -q '!!'; then
  echo "Regra quebrada: usou '!!'. Trate o nulo com ?., ?: ou exceção de negócio (ver .claude/rules/code-style.md)." >&2
  exit 2
fi

if echo "$ADDED" | grep -qE '\bDouble\b|\bFloat\b'; then
  echo "Regra quebrada: usou Double/Float. Dinheiro nunca é Double ou Float (ver CLAUDE.md)." >&2
  exit 2
fi

exit 0