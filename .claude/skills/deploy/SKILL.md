---
name: deploy
description: Deploy simulado do order-service. Valida, gera o artefato e registra o que seria publicado.
disable-model-invocation: true
argument-hint: "<ambiente: dev | hml | prd>"
---

# Deploy (simulado)

Este projeto ainda não tem infraestrutura. Este fluxo simula um deploy real:
executa todas as validações e gera o artefato, mas não publica nada.
Nunca diga que algo foi publicado.

Ambiente pedido: $ARGUMENTS

1. Se o ambiente não for `dev`, `hml` ou `prd`, pergunte e pare.
2. Leia `deploy-config.md` para as regras do ambiente pedido.
3. Confira os pré-requisitos:
    - `git status` limpo; se não estiver, pare;
    - branch permitida para o ambiente (ver `deploy-config.md`).
4. Rode `cd project && ./gradlew cleanTest test` e confirme no log que os testes executaram.
5. Gere o artefato com `cd project && ./gradlew bootJar` e confira que o `.jar` existe em `project/build/libs/`.
6. Para `prd`, mostre o resumo e peça confirmação explícita antes do passo 7.
7. Mostre o relatório final:
    - ambiente, branch e commit (`git log -1 --oneline`);
    - nome e tamanho do `.jar`;
    - o que seria executado num deploy real (ver `deploy-config.md`);
    - a frase: "Deploy SIMULADO: nada foi publicado."