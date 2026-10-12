# ADR-0002: Monólito modular por pacote, com verificação automática a partir do segundo módulo

- **Status:** Aceita
- **Data:** 2026-10-12

## Contexto
O serviço é dividido em `orders`, `payments` e `shared`. Os módulos só conversam pela API pública
(`PaymentApi`) ou por eventos, e a dependência vai num sentido só: `orders` -> `payments`.
Hoje essas regras dependem só de disciplina e de revisão. Uma revisão já encontrou código de pedido
fora do módulo `orders`, e nada no build teria apontado isso.

O projeto deve crescer aos poucos: só criar estrutura quando houver necessidade real. Ele é mantido
por uma pessoa e ainda tem poucos módulos, e as fronteiras entre eles ainda não foram validadas na prática.

## Decisão
Cada módulo é um pacote de primeiro nível em `com.silas.order_service` (`orders`, `payments`, `shared`),
todos no mesmo projeto Gradle. As regras entre módulos serão verificadas pelo Spring Modulith, num teste,
a partir do momento em que existir o segundo módulo de negócio (`payments`).

Detalhes:
- O `shared` não conta como módulo de negócio. Até o `payments` existir, não há fronteira a verificar.
- A verificação fica num teste (`ApplicationModules.of(...).verify()`). O domínio continua sem
  dependência de Spring.

## Alternativas consideradas
Para separar os módulos:
- Subprojetos Gradle, com o compilador barrando os imports proibidos: o custo de configuração e de build
  vem antes do benefício. Com uma pessoa e poucos módulos, o teste do Modulith pega as mesmas violações.
- Microsserviços: rede, falha parcial, consistência eventual e deploy de vários serviços, antes de as
  fronteiras estarem validadas. Errar uma fronteira no monólito é mover um pacote; em microsserviços,
  é migrar dados.

Para a verificação automática:
- ArchUnit: exigiria escrever à mão as regras que o Modulith já conhece.
- Konsist: tem adoção bem menor no mercado.

O Spring Modulith foi escolhido por três motivos. Ele entende os pacotes de primeiro nível como módulos
sem configuração. A verificação fica num teste. E o suporte a eventos (registro de publicação) vai servir
de comparação com o Outbox quando o Kafka entrar.

## Consequências
- Positivas:
  - Um único build e um único deploy enquanto as fronteiras ainda estão sendo descobertas.
  - Corrigir uma fronteira errada custa mover um pacote.
  - A partir do `payments`, um import proibido entre módulos quebra o teste, sem depender de revisão.
  - A API pública de cada módulo permite extraí-lo depois, como subprojeto ou como serviço.
- Negativas e riscos:
  - Até o `payments` existir, nada automático impede que o código saia do lugar. A revisão continua
    sendo a única barreira.
  - A verificação acontece no teste, não na compilação. Uma violação compila e só falha ao rodar os testes.
  - O `shared` precisa ser configurado no Modulith para que os outros módulos possam depender dele.
  - Acrescenta uma dependência de teste do ecossistema Spring.
- Quando reavaliar:
  - Subprojetos Gradle: se o time crescer, ou se um módulo precisar de dependências que não podem vazar
    para os outros.
  - Microsserviços: se um módulo precisar escalar ou ter deploy independente.
