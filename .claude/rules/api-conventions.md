---
paths:
  - "project/src/main/kotlin/**/adapters/web/**/*.kt"
---

# Convenções da API REST

- Criação devolve `201 Created` com header `Location`.
- Ação de negócio é `POST` em sub-recurso (`POST /orders/{id}/cancel`), não `DELETE` nem `PUT`.
- Valores monetários trafegam como texto (`"100.00"`), nunca como número no JSON.
- Erros no formato `ProblemDetail` (RFC 9457), mapeados no `ApiExceptionHandler`:
    - entrada inválida: 400
    - recurso não encontrado: 404
    - transição de estado inválida: 409
    - regra de negócio impede a operação: 422
- Erro do cliente nunca vira 500.
- O controller não tem regra de negócio: converte o DTO, chama o caso de uso e converte a resposta.
- O domínio nunca é exposto direto na resposta: sempre um DTO.