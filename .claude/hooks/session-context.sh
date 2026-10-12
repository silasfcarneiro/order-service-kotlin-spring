#!/bin/bash
# SessionStart: tudo que este script imprime no stdout vira contexto pro Claude.

cd "$CLAUDE_PROJECT_DIR" || exit 0

echo "## Estado do repositório"
echo "Branch atual: $(git branch --show-current)"
echo ""
echo "Últimos commits:"
git log --oneline -5
echo ""
echo "Arquivos alterados e não commitados:"
git status --short

exit 0