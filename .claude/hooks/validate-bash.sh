#!/bin/bash
# Roda antes de todo comando Bash do Claude.
# Recebe no stdin um JSON com o comando em tool_input.command.
# exit 0 = deixa passar | exit 2 = bloqueia, e a mensagem do stderr vai pro Claude.

INPUT=$(cat)

block() {
  echo "Bloqueado pelo hook: $1" >&2
  exit 2
}

echo "$INPUT" | grep -q 'rm -rf'              && block "rm -rf não é permitido. Apague arquivos específicos."
echo "$INPUT" | grep -q 'git reset --hard'    && block "git reset --hard descarta trabalho. Peça ao Silas."
echo "$INPUT" | grep -qE 'push.*(--force|-f)' && block "push forçado não é permitido."
echo "$INPUT" | grep -q 'gradlew' && ! echo "$INPUT" | grep -q 'cd project' \
  && block "rode o Gradle dentro de project/: cd project && ./gradlew ..."

exit 0