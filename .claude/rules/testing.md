---
paths:
  - "project/src/test/**/*.kt"
---

# Testes

- Nome do teste em português, entre crases, descrevendo o comportamento:
  `cancelar duas vezes nao estorna duas vezes`.
- Estrutura dado / quando / então, separada por linha em branco.
- Teste de unidade monta o serviço sem Spring, com fakes das portas
  (repositório, notificação, gateway). Prefira fake a mock quando a porta for nossa.
- Teste de integração (`@SpringBootTest`) só para verificar a ligação dos beans.
- Para cada regra, cubra o caminho feliz, o erro esperado e o caso de borda
  (repetição, valor zero, estado inválido).
- Nunca altere o código de produção só para fazer um teste errado passar:
  primeiro confirme qual dos dois está certo.
- Depois de rodar, confirme no log que os testes executaram de fato.