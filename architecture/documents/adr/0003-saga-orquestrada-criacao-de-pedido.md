# ADR-0003: Saga orquestrada para criação de pedido

- **Status:** Aceita
- **Data:** 2026-10-12

## Contexto
Criar um pedido envolve reservar estoque (módulo `inventory`, novo) e cobrar o pagamento
(módulo `payments`). Cada passo pode falhar de forma independente, e não existe transação entre
módulos. Se o pagamento falhar depois da reserva, o estoque precisa ser devolvido.

O `inventory` não está entre os módulos listados na ADR-0002 (`orders`, `payments`, `shared`).

## Decisão
Saga orquestrada. Um coordenador no módulo `orders` conduz os passos (reservar estoque, cobrar
pagamento, confirmar) e dispara as compensações (devolver estoque) quando um passo falha.
O pedido nasce `PENDENTE` e termina `CONFIRMADO` ou `REJEITADO`.

Módulos:
- `inventory` é um módulo de negócio, ao lado de `orders` e `payments`.
- `orders` depende de `inventory` e de `payments` pela API pública de cada um. `inventory` e `payments`
  não conhecem `orders`. Isso evita ciclo entre módulos e mantém a dependência em uma direção, como
  pede a ADR-0002.
- Com o `inventory`, o projeto passa a ter dois módulos de negócio além de `orders`. Por isso o gatilho
  da verificação com Spring Modulith, definido na ADR-0002, é considerado disparado.

## Alternativas consideradas
- Coreografia por eventos (cada módulo reage ao evento do anterior): desacopla mais, mas espalha o fluxo
  entre os módulos. Fica mais difícil ler, testar e explicar a sequência e as compensações. Faz mais
  sentido com muitos serviços de times diferentes.
- Transação única no banco cobrindo os três módulos: só funciona enquanto tudo estiver no mesmo banco
  e no mesmo processo, e quebra a independência dos módulos. Não sobrevive à extração de um módulo
  para outro serviço.
- Sem compensação, corrigindo as inconsistências manualmente: inaceitável com estoque e dinheiro envolvidos.

## Consequências
- Positivas:
  - A sequência e as compensações ficam num lugar só, fáceis de ler e de testar.
  - Não há ciclo entre módulos: `inventory` e `payments` não dependem de `orders`.
- Negativas e riscos:
  - O `orders` conhece todos os passos. É o acoplamento do coordenador, aceito.
  - O estado do pedido precisa registrar em que passo está, para retomar ou compensar depois de uma falha.
  - As operações de cada passo precisam ser idempotentes, porque podem ser repetidas (ver a próxima ADR).
- Quando reavaliar:
  - Se o número de participantes crescer muito, ou se os módulos passarem a ser de times diferentes:
    considerar coreografia.
