# ADR-0001: Money como valor monetário

- **Status:** Aceita
- **Data:** 2026-10-12

## Contexto
O serviço calcula valores de pedido e pagamento: soma de itens, preço vezes quantidade, percentuais.
Esses cálculos precisam ser exatos e dar sempre o mesmo resultado, com a mesma escala e o mesmo
arredondamento em qualquer ponto do código. Sem um tipo próprio, cada chamador decidiria escala,
arredondamento e validação da entrada por conta própria, e um erro em qualquer um desses pontos
muda o valor cobrado.

## Decisão
Todo valor monetário é um `Money`: uma `value class` sobre `BigDecimal`, com escala 2, criada
só pelo `Money.of(...)`. O projeto trabalha somente em BRL, por isso o `Money` não carrega moeda.

Regras do tipo:
- O construtor é privado: o `of()` é a única forma de criar um `Money`.
- Resultados de cálculo (`plus`, `minus`, `times`, `percent`) são arredondados para 2 casas com `HALF_EVEN`.
- `Money.of(String)` não arredonda. Uma entrada com mais de 2 casas (`"19.999"`) ou com formato
  inválido (`"19,90"`, `"abc"`) é erro de quem enviou. Ela é recusada com uma exceção de negócio clara,
  e não com a `NumberFormatException` do `BigDecimal`. Arredondar em silêncio esconderia o erro.
- Valores negativos são permitidos. O `Money` é só um valor, e as regras de sinal (por exemplo,
  "o total do pedido não pode ser negativo") ficam nas entidades de domínio.

## Alternativas consideradas
Comparadas antes da implementação:
- `data class` em vez de `value class`, descartada por dois motivos, ambos com ressalvas:
  - O `copy()` público permitiria criar um `Money` sem passar pelo `of()`, furando a validação.
    Isso pode ser evitado: desde o Kotlin 2.0.20, `@ConsistentCopyVisibility` deixa o `copy()` com a
    mesma visibilidade do construtor. É um ponto a menos com que se preocupar, não um impedimento.
  - A `data class` sempre cria um objeto em memória. A `value class` evita esse objeto só quando é usada
    diretamente como `Money`. Como `Comparable`, em tipos genéricos (`List<Money>`) ou como `Money?`,
    ela também vira objeto (boxing). O ganho existe, mas é parcial e pequeno neste projeto.
- `Double`/`Float`: imprecisão binária (`0.1 + 0.2` não dá `0.3`).

Avaliadas ao escrever esta ADR, não antes da implementação:
- `BigDecimal` cru, sem tipo próprio: nada garante as 2 casas, e `2.0` é diferente de `2.00` no `equals`.
  Cada uso teria que repetir as regras.
- `Long` em centavos: é preciso, mas exige conversão em toda entrada e saída, e é fácil errar a unidade.
- Moneta (JSR 354): completa demais para um projeto só em BRL. É uma dependência pesada para pouco ganho.

## Consequências
- Positivas:
  - Escala, arredondamento e validação da entrada ficam definidos num lugar só.
  - Como todo `Money` tem escala 2, `equals`, `hashCode` e `compareTo` concordam entre si.
  - Um erro de entrada aparece como exceção de negócio, em vez de virar um valor arredondado.
- Negativas e riscos:
  - A garantia da escala 2 depende de todo `Money` nascer pelo `of()`. Reflexão e desserialização
    (Jackson, JPA) podem contornar o construtor, então as bordas precisam converter pelo `of()`.
  - Sem moeda no tipo, não há como detectar a soma de valores de moedas diferentes.
  - Permitir negativos transfere para cada entidade a responsabilidade de validar o sinal.
- Quando reavaliar:
  - Se surgir uma segunda moeda: o `Money` passa a carregar a moeda, e o Moneta volta a ser opção.
  - Se algum cálculo precisar de mais de 2 casas no meio (câmbio, juros compostos).
