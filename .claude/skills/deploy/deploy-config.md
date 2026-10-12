# Configuração de deploy (simulada)

| Ambiente | Branch permitida | Aprovação humana | Destino real (futuro) |
| --- | --- | --- | --- |
| dev | qualquer | não | ECS Fargate, cluster `orders-dev` |
| hml | `main` | não | ECS Fargate, cluster `orders-hml` |
| prd | `main`, com tag `vX.Y.Z` | sim, explícita | ECS Fargate, cluster `orders-prd` |

## O que um deploy real faria

1. Build da imagem Docker a partir do `.jar`.
2. Push da imagem para o ECR com a tag do commit.
3. Atualização da task definition do ECS com a nova imagem.
4. Deploy rolling com health check em `/actuator/health`.
5. Rollback automático se o health check falhar.

## Regras

- `prd` nunca recebe build de branch que não seja a `main`.
- Segredos vêm do Secrets Manager, nunca de variável escrita no pipeline.