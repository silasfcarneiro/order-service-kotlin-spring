---
description: Revisa as mudanças atuais com os agentes de código e de segurança
argument-hint: "[foco opcional, ex.: testes, performance]"
---

Revise as mudanças ainda não commitadas deste repositório.

1. Levante tudo o que mudou, sem deixar nada de fora:
    - `git status --short` para a lista completa;
    - `git diff` para o que não está em stage;
    - `git diff --staged` para o que já está em stage;
    - leia por inteiro os arquivos não rastreados (`??`), porque o diff não mostra eles.
      Se não houver mudança nenhuma, avise e pare.
2. Use o subagente `code-reviewer`, passando a lista de arquivos alterados e novos.
3. Se a mudança tocar em integração externa, validação de entrada, log, configuração,
   permissões ou dados sensíveis, use também o subagente `security-auditor`, com a mesma lista.
4. Repasse aos dois subagentes o foco adicional, se houver.
5. Junte os resultados num relatório único, sem repetir achados, agrupado em:
   Bloqueante, Importante e Sugestão.
6. Termine dizendo se a mudança está pronta para commit ou não.

Não corrija nada: só relate. As correções eu decido depois.

Foco adicional (se estiver vazio, ignore): $ARGUMENTS