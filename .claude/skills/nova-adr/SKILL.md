---
name: nova-adr
description: Registra uma decisão de arquitetura como ADR. Use quando o usuário pedir uma ADR ou quando uma decisão de arquitetura for tomada na conversa (escolha de tecnologia, padrão, estrutura, trade-off relevante).
argument-hint: "[título da decisão]"
---

# Nova ADR

Título sugerido: $ARGUMENTS

1. Liste `architecture/documents/adr/` e use o próximo número livre, com 4 dígitos (`0003`).
2. Copie a estrutura de `architecture/documents/adr/0000-template.md`.
3. Nome do arquivo: `NNNN-titulo-curto-em-kebab-case.md`.
4. Preencha a partir da conversa:
    - Contexto: o problema real, não a solução;
    - Decisão: uma ou duas frases;
    - Alternativas: pelo menos duas, cada uma com o motivo do descarte;
    - Consequências: positivas, negativas e quando reavaliar.
5. Se faltar informação (alternativas, motivo, trade-off), pergunte. Não invente justificativa.
6. Status inicial: `Proposta`. Só marque `Aceita` se eu confirmar.
7. Se a decisão substitui uma ADR existente, crie a nova e altere na antiga
   apenas o status para `Substituída por ADR-NNNN`. Nada mais na antiga muda.
8. Siga também `.claude/rules/adr.md`.
9. Ao terminar, mostre o caminho do arquivo e um resumo de três linhas.