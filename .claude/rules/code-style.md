---
paths:
  - "project/src/**/*.kt"
---

# Estilo de código Kotlin

- Prefira `val`. Use `var` só quando a mutação for necessária e local.
- Nunca use `!!`. Trate o nulo com `?.`, `?:` ou falhe com uma exceção de negócio clara.
- Modelos de domínio são `data class` imutáveis; mudanças de estado devolvem `copy()`.
- Estados com dados diferentes por caso viram `sealed class` / `sealed interface`, tratados com `when` sem `else`.
- Não crie Builder: use argumentos nomeados e valores padrão.
- Não use `Double` ou `Float` para dinheiro. Use `Money`.
- Classes de domínio não importam nada de Spring.
- Nomes dizem a verdade: um campo `total` não pode guardar preço unitário.
- Injeção sempre pelo construtor, nunca `@Autowired` em campo.