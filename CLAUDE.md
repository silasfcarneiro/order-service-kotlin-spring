# order-service

Serviço de pedidos em Kotlin + Spring Boot, construído do zero para estudo e portfólio.

## Estrutura
- `architecture/documents/`: documentação de arquitetura.
    - `adr/`: decisões registradas. Leia as ADRs relacionadas antes de propor mudança de arquitetura.
      Decisão nova ou que contradiz uma ADR existente vira uma ADR nova, nunca uma edição silenciosa.
    - `mappings/`: de-para de payloads entre sistemas. Consulte antes de criar ou alterar um adapter.
    - `diagrams/`: diagramas C4.

## Comandos
- Testes: `cd project && ./gradlew cleanTest test`
- Subir a API: `cd project && ./gradlew bootRun` (porta 8080)

## Arquitetura alvo
- Hexagonal: `domain` (regras puras, sem Spring), `application` (casos de uso e portas),
  `adapters` (HTTP, persistência, mensageria).
- Dependências só apontam para dentro: o domínio não conhece banco, HTTP nem Kafka.
- Crescer aos poucos: só criar camada, interface ou módulo quando houver necessidade real.

## Regras que não podem ser quebradas
- Dinheiro nunca é `Double` ou `Float`: usar um tipo próprio sobre `BigDecimal`.
- Entidades de domínio são imutáveis: transições devolvem uma cópia. Nunca ignorar o retorno.
- Validar o estado antes de agir no mundo externo (cobrança, estorno, envio de mensagem).
- Retry só em operação idempotente.

## Como trabalhar
- Este é um projeto de aprendizado: explique a decisão antes de implementar,
  e prefira passos pequenos que eu consiga revisar.
- Toda mudança de comportamento vem com teste.
- Antes de dizer que terminou, rode os testes e confirme que eles executaram.
- Eu escrevo o código de produção e os testes. Não implemente nada sem eu pedir
    explicitamente: explique, aponte o caminho ou revise.

Detalhes em `.claude/rules/`.
