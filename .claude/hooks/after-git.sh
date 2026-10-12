#!/bin/bash
# PostToolUse no Bash: se o comando foi push ou abertura de PR, notifica.
# O JSON de entrada traz o comando (tool_input) e a saída dele (tool_response).

INPUT=$(cat)
NOTIFY="$CLAUDE_PROJECT_DIR/.claude/hooks/notify.sh"

if echo "$INPUT" | grep -q 'gh pr create'; then
  URL=$(echo "$INPUT" | grep -oE 'https://github\.com/[^" ]+/pull/[0-9]+' | head -1)
  bash "$NOTIFY" "PR aberto: ${URL:-veja no GitHub}"
elif echo "$INPUT" | grep -q 'git push'; then
  BRANCH=$(cd "$CLAUDE_PROJECT_DIR" && git branch --show-current)
  bash "$NOTIFY" "Push feito na branch $BRANCH"
fi

exit 0