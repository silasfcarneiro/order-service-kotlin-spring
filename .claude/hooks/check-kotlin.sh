#!/bin/bash
# PostToolUse: roda depois de cada Edit ou Write.
# Olha só o arquivo que acabou de ser editado e cobra as regras do code-style.md.
# exit 2 aqui não desfaz a edição, mas a mensagem volta pro Claude corrigir.

INPUT=$(cat)

# Pega o caminho do arquivo editado (tool_input.file_path).
# No Windows o JSON traz barras invertidas duplas (E:\\Cursos\\...): troca por /.
FILE=$(echo "$INPUT" \
  | grep -o '"file_path" *: *"[^"]*"' \
  | head -1 \
  | sed -E 's/.*: *"(.*)"/\1/' \
  | sed 's#\\\\#/#g')

# Só interessa arquivo Kotlin.
[[ "$FILE" == *.kt ]] || exit 0

cd "$CLAUDE_PROJECT_DIR" || exit 0

# Arquivo já conhecido pelo Git: olha só as linhas adicionadas.
# Arquivo novo: olha o arquivo inteiro, porque todas as linhas são novas.
if git ls-files --error-unmatch "$FILE" > /dev/null 2>&1; then
  ADDED=$(git diff -U0 -- "$FILE" | grep '^+[^+]')
else
  ADDED=$(cat "$FILE" 2>/dev/null)
fi

if echo "$ADDED" | grep -q '!!'; then
  echo "Regra quebrada em $FILE: usou '!!'. Trate o nulo com ?., ?: ou exceção de negócio (ver .claude/rules/code-style.md)." >&2
  exit 2
fi

if echo "$ADDED" | grep -qE '\bDouble\b|\bFloat\b'; then
  echo "Regra quebrada em $FILE: usou Double/Float. Dinheiro nunca é Double ou Float (ver CLAUDE.md)." >&2
  exit 2
fi

exit 0