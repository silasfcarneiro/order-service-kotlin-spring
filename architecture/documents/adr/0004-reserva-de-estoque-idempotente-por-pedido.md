# ADR-0004: Reserva de estoque idempotente por pedido

- **Status:** Aceita
- **Data:** 2026-10-12

## Contexto
Na saga de criação de pedido (ADR-0003), cada passo pode ser executado mais de uma vez: retry depois de
um timeout, mensagem reentregue pelo broker, ou o coordenador retomando depois de uma queda.

Hoje o `StockItem` opera só por quantidade. Um `reserve(5)` repetido reserva 10, e um `release`
duplicado pode devolver a reserva de outro pedido. A regra do CLAUDE.md diz que retry só é permitido
em operação idempotente.

## Decisão
A reserva passa a ser por pedido. Cada reserva é identificada pelo `orderId` e guarda sku, quantidade
e status (`RESERVADA`, `CONFIRMADA`, `DEVOLVIDA`).

Regras:
- `reserve(orderId, quantidade)`: se já existe reserva para o `orderId` com o mesmo sku e a mesma
  quantidade, não faz nada e devolve a existente.
- `reserve` repetido para o mesmo `orderId` com sku ou quantidade diferentes da reserva existente é
  recusado como conflito, e não tratado como repetição.
- `release(orderId)` e `confirm(orderId)`: agem só sobre a reserva daquele pedido, usando a quantidade
  registrada nela, e não uma quantidade informada por quem chama.
- Repetir `release` numa reserva já `DEVOLVIDA`, ou `confirm` numa já `CONFIRMADA`, não faz nada.
- Transição inválida (devolver uma reserva `CONFIRMADA`, ou confirmar uma `DEVOLVIDA`) é recusada como
  erro de estado.

## Alternativas consideradas
- Chave de idempotência genérica (tabela de chaves já processadas): serve para qualquer operação, mas não
  amarra a reserva ao pedido. O `release` continuaria podendo devolver a quantidade errada.
- Deduplicação só no consumidor de mensagens (ids já processados): protege contra a reentrega do broker,
  mas não contra o retry por HTTP nem contra o coordenador repetindo um passo. Será usada também, como
  camada adicional, quando o Kafka entrar.
- Confiar em entrega exatamente uma vez: não existe de ponta a ponta entre sistemas. Qualquer timeout
  deixa o chamador sem saber se a operação aconteceu.

## Consequências
- Positivas:
  - `reserve`, `release` e `confirm` podem ser repetidos com segurança. Os passos da saga passam a
    poder entrar em retry, como exige o CLAUDE.md.
  - Um pedido só consegue devolver ou confirmar a própria reserva, e sempre na quantidade registrada.
- Negativas e riscos:
  - O `StockItem` muda de forma: passa a conhecer as reservas por pedido. É melhor mudar agora, antes
    da persistência.
  - A concorrência entre pedidos diferentes disputando o último item continua sendo um problema separado,
    a decidir junto com a persistência.
- Quando reavaliar:
  - Se um pedido precisar de reservas parciais, ou em várias etapas, para o mesmo sku.
