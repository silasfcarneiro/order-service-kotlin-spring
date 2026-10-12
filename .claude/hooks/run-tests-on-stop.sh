#!/bin/bash
# Stop: roda quando o Claude diz que terminou.
# exit 2 = "não terminou ainda": a mensagem do stderr volta pra ele e ele continua.

INPUT=$(cat)

# Trava contra loop: se ele já está continuando por causa deste hook, deixa parar.
echo "$INPUT" | grep -q '"stop_hook_active": *true' && exit 0

cd "$CLAUDE_PROJECT_DIR" || exit 0

# Só roda os testes se algum .kt mudou. Pergunta simples não dispara build.
git status --porcelain | grep -q '\.kt' || exit 0

cd project || exit 0
OUTPUT=$(./gradlew test 2>&1)

if [ $? -ne 0 ]; then
  echo "Os testes falharam. Corrija antes de terminar. Final do log:" >&2
  echo "$OUTPUT" | tail -40 >&2
  exit 2
fi

exit 0