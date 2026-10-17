# ADR-0006: Modelo de leitura do pedido com CQRS (OrderView)

- **Status:** Proposta (aguardando a entrada do Kafka)
- **Data:** 2026-10-16

## Contexto

O estado completo de um pedido fica espalhado em três serviços:
- itens e total no order-service;
- status da reserva no inventory-service;
- status do pagamento no payments-service.

A consulta "acompanhar pedido" precisa dessas informações juntas. Montar a resposta chamando os três serviços a cada consulta acopla a leitura à disponibilidade de todos e aumenta a latência.

Hoje o order-service usa CQRS leve: escrita via command (`CreateOrderCommand` → `OrderService` → domínio → `OrderRepository`) e leitura via `OrderQueryService`, no mesmo banco.

## Decisão

Quando a comunicação por eventos (Kafka) estiver implementada, criar um modelo de leitura dedicado:

- `OrderView`: DTO achatado, sem regras de negócio, com itens, total, status da reserva e status do pagamento.
- Projetor: consumidor que escuta `OrderCreated`, `StockReserved`, `StockReleased`, `PaymentApproved` e `PaymentRejected` e atualiza o `OrderView`.
- `OrderViewRepository`: armazenamento separado do banco de escrita.
- `GET /v1/api/orders/{id}` passa a ler do `OrderView`.

O lado da escrita continua igual: commands passam pelo domínio e gravam no `OrderRepository`.

## Gatilho para implementar

- Kafka funcionando entre order, inventory e payments.
- Eventos de reserva e pagamento publicados.

## Alternativas consideradas

- **Composição síncrona (chamar os três serviços na consulta):** simples, mas a consulta cai se qualquer serviço cair e a latência soma.
- **CQRS completo desde já:** descartada. Sem eventos não há como alimentar o modelo de leitura, e a complexidade não se justifica com um único serviço.
- **Framework (Axon):** descartada. Traz event sourcing e um modelo próprio de arquitetura, e é pesado demais para o escopo do projeto.

## Consequências

- (+) Consulta rápida e independente da disponibilidade dos outros serviços.
- (+) Modelo de leitura pode evoluir sem afetar as regras do domínio.
- (-) Consistência eventual: a consulta pode mostrar um estado atrasado por alguns instantes.
- (-) Mais um consumidor e mais um armazenamento para manter.
- (-) Projetor precisa ser idempotente (o mesmo evento pode chegar mais de uma vez).
