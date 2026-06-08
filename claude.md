# FahCortes — Backend API

Sistema de gerenciamento de barbearia construído com Spring Boot seguindo Clean Architecture.

## Stack

- **Java 17** + **Spring Boot 3.2.5**
- **MySQL 8.0** (via Docker Compose ou local)
- **Spring Security** + **JWT** (auth0 java-jwt 4.4.0)
- **Spring Data JPA** + **ModelMapper 3.2.0** + **Lombok**
- **Maven** como build tool

## Comandos

```bash
# Subir banco de dados via Docker
docker-compose up -d

# Rodar a aplicação
mvn spring-boot:run

# Rodar os testes
mvn test

# Build sem testes
mvn package -DskipTests
```

## Arquitetura

O projeto segue **Clean Architecture** com separação clara entre domínio e infraestrutura:

```
src/main/java/com/barbearia/fahcortes/
├── domain/
│   ├── entities/          # Entidades de domínio puras (sem anotações JPA)
│   ├── gateways/          # Interfaces dos repositórios (contratos)
│   ├── usecases/          # Regras de negócio (um arquivo por caso de uso)
│   └── exception/         # Exceções de domínio
└── infra/
    ├── config/            # Beans de configuração e injeção de dependência
    ├── controller/        # Controllers REST + DTOs
    ├── entities/          # Entidades JPA (mapeamento banco)
    ├── gateways/          # Implementações dos gateways usando JPA
    ├── mapper/            # Mappers entre domain <-> entity <-> dto
    ├── persistence/       # Interfaces JpaRepository
    └── security/          # JWT, filtro de autenticação, UserDetailsService
```

### Fluxo de uma requisição

```
Controller → UseCase → Gateway (interface) → GatewayImp (JPA) → Repository
```

- Controllers recebem DTOs e delegam para UseCases
- UseCases operam com entidades de domínio puras
- Gateways são as interfaces que desacoplam domínio de infraestrutura
- GatewayImp implementa os gateways usando JPA + ModelMapper
- Config classes registram os beans e fazem a injeção de dependência

## Entidades de domínio

| Entidade    | Descrição                                        |
|-------------|--------------------------------------------------|
| Usuario     | Clientes/admins/barbeiros com roles              |
| Barbeiro    | Profissional da barbearia                        |
| Servico     | Tipo de serviço oferecido (ex: corte, barba)     |
| Agendamento | Reserva de horário (clienteId, barbeiroId, etc.) |
| Unidade     | Filial/unidade da barbearia                      |
| Plano       | Planos de assinatura                             |
| Produto     | Produtos à venda                                 |

### Roles de usuario

```java
enum UsuarioEnum { ADMIN, USER, BARBEIRO }
```

### Status de agendamento

```java
enum AgendamentoStatus { CONFIRMADO, PENDENTE, CANCELADO }
```

- Status padrao ao criar: `CONFIRMADO` (definido no `CriarAgendamentoUseCase` se nao informado)
- Cancelamento via `PATCH /agendamentos/{id}/cancelar` muda para `CANCELADO`

## Segurança

- Autenticação via JWT Bearer Token
- Token retornado no login (`POST /login`) com email + senha
- Expiração configurável via `JWT_EXPIRATION` (padrão: 7200000ms = 2h)
- Secret configurável via `JWT_SECRET`

### Endpoints públicos (sem autenticação)

```
POST  /login
POST  /usuarios

GET   /servico
GET   /servico/{id}
GET   /barbeiros
GET   /barbeiros/{id}
PATCH /barbeiros/{id}/curtir
GET   /produtos
GET   /produtos/{id}
GET   /unidades
GET   /unidades/{id}
GET   /planos
GET   /planos/{id}
```

### Endpoints protegidos — apenas autenticado (qualquer role)

```
GET     /agendamentos              (filtrável por ?clienteId=X)
GET     /agendamentos/{id}
POST    /agendamentos
PATCH   /agendamentos/{id}/cancelar

GET     /usuarios
GET     /usuarios/{id}
GET     /usuarios/email?email=X
PUT     /usuarios/{id}
DELETE  /usuarios/{id}
```

### Endpoints protegidos — exigem role ADMIN (`@PreAuthorize("hasRole('ADMIN')")`)

```
POST    /servico
DELETE  /servico/{id}

POST    /barbeiros
PUT     /barbeiros/{id}
DELETE  /barbeiros/{id}

POST    /produtos
PUT     /produtos/{id}
DELETE  /produtos/{id}

POST    /unidades
PUT     /unidades/{id}
DELETE  /unidades/{id}

POST    /planos
PUT     /planos/{id}
DELETE  /planos/{id}

DELETE  /agendamentos/{id}
```

Header obrigatório para endpoints protegidos:
```
Authorization: Bearer <token>
```

## Tratamento de erros

`GlobalExceptionHandler` mapeia exceções para respostas HTTP padronizadas:

| Exceção                          | HTTP | Código                 |
|----------------------------------|------|------------------------|
| `EntidadeNaoEncontradaException` | 404  | NOT_FOUND              |
| `RegraDeNegocioException`        | 409  | CONFLICT               |
| `MethodArgumentNotValidException`| 400  | VALIDATION_ERROR       |
| `AccessDeniedException`          | 403  | FORBIDDEN              |
| `AuthenticationException`        | 401  | UNAUTHORIZED           |
| `Exception` (geral)              | 500  | INTERNAL_SERVER_ERROR  |

Formato da resposta de erro:
```json
{
  "code": "NOT_FOUND",
  "message": "Entidade não encontrada"
}
```

Rate limit excedido retorna HTTP 429:
```json
{
  "code": "RATE_LIMIT_EXCEEDED",
  "message": "Muitas tentativas de login. Aguarde 1 minuto antes de tentar novamente."
}
```

## Banco de dados

- **MySQL 8.0** na porta `3306`
- Database: `fahcortes`
- Usuário: `root` / Senha: `root` (desenvolvimento)
- `spring.jpa.hibernate.ddl-auto=update` — schema atualizado automaticamente
- Docker Compose gerencia o banco via `docker-compose.yml`

Variáveis de ambiente relevantes:
```
JWT_SECRET           # Secret do JWT (padrão: fahcortes-dev-secret-key-2024-change-in-prod)
JWT_EXPIRATION       # Expiração em ms (padrão: 7200000 = 2h)
CORS_ALLOWED_ORIGINS # Origins permitidas (padrão: http://localhost:5173,3000,8080)
```

## Testes

```
src/test/
├── domain/usecases/      # Testes unitários dos use cases (mocks de gateway)
└── infra/controller/     # Testes de controller com MockMvc + spring-security-test
```

- Testes de use case usam Mockito para mockar os gateways
- Testes de controller usam `@WebMvcTest` + `@MockBean`
- `FahcortesApplicationTests` verifica que o contexto Spring carrega corretamente

## Observacoes por entidade

| Entidade    | Operacoes disponíveis                          | Observacao                                            |
|-------------|------------------------------------------------|-------------------------------------------------------|
| Usuario     | CRUD completo                                  | POST é público; resto exige auth                      |
| Barbeiro    | CRUD + Curtir                                  | GET/PATCH curtir público; POST/PUT/DELETE exige ADMIN |
| Servico     | Cadastrar, Buscar, Listar, Remover             | Sem endpoint de atualizar (PUT)                       |
| Agendamento | Criar, Buscar, Listar, Cancelar, Deletar       | Listar aceita `?clienteId=X`; Deletar exige ADMIN     |
| Unidade     | CRUD completo                                  | GET é público; POST/PUT/DELETE exige ADMIN            |
| Plano       | CRUD completo                                  | GET é público; POST/PUT/DELETE exige ADMIN            |
| Produto     | CRUD completo                                  | GET é público; POST/PUT/DELETE exige ADMIN            |

## Auditoria de segurança e pendencias tecnicas

### Critico — antes de qualquer deploy em producao

| # | Item | Status |
|---|------|--------|
| 1 | `@PreAuthorize("hasRole('ADMIN')")` nos endpoints admin | **JA IMPLEMENTADO** em todos os controllers |
| 2 | PUT /usuarios/{id} — senha sem hash | **JA IMPLEMENTADO** — gateway usa `passwordEncoder.encode(novaSenha)` |
| 3 | JWT secret via variavel de ambiente real | **JA IMPLEMENTADO** — usa `${JWT_SECRET:...}` em application.properties |
| 4 | Rate limiting no POST /login | **IMPLEMENTADO** — `LoginRateLimitFilter` (5 req/min por IP, HTTP 429) |
| 5 | POST /login retorna role | **JA IMPLEMENTADO** — `LoginResponseDto(token, role)` |

> **Obs sobre item 3**: Em producao, definir `JWT_SECRET` como variavel de ambiente no servidor com uma string de 256 bits aleatorios. Nunca commitar o valor real no codigo ou no docker-compose.yml.

### Importante — apos estabilizar

| # | Item | Status |
|---|------|--------|
| 6 | HTTPS obrigatorio em producao | Pendente — configurar SSL (Let's Encrypt para VPS, automatico no Railway/Render) |
| 7 | JWT em httpOnly cookie | Pendente — refatoracao grande nos dois lados (backend + frontend) |
| 8 | Expiracao do JWT configurada | **CORRIGIDO** — `TokenService` agora le `${api.security.token.expiration}` via `@Value` |

> **Bug corrigido (item 8)**: A env var `JWT_EXPIRATION` estava declarada em `application.properties` mas o `TokenService` ignorava e hardcodava `plusHours(2)`. Corrigido para usar o valor configurado.

### Pendencias de backend

| # | Item | Status |
|---|------|--------|
| P1 | DELETE /agendamentos/{id} retornava 500 | **IMPLEMENTADO** — endpoint criado com role ADMIN |
| P2 | Curtidas de barbeiro nao persistidas | **IMPLEMENTADO** — `PATCH /barbeiros/{id}/curtir` criado (publico) |
| P3 | POST /servico falha com encoding nao-UTF-8 | **IMPLEMENTADO** — `server.servlet.encoding.force=true` em application.properties |

## Convenções do projeto

- Um arquivo por caso de uso (ex: `CriarAgendamentoUseCase.java`)
- Entidades de domínio sem anotações de framework (POJOs puros)
- DTOs separados por controller em subpacote `dtos/`
- Injeção de dependência por construtor (sem `@Autowired` em campo)
- Mappers dedicados por entidade em `infra/mapper/`
- `Config` classes registram os use cases como `@Bean` com suas dependências

---

## Historico de alteracoes

### 2026-06-08 — Documentacao inicial do CLAUDE.md
- [x] Leitura completa da estrutura do projeto (dominio, infra, testes)
- [x] Documentacao da arquitetura Clean Architecture e fluxo de requisicoes
- [x] Mapeamento de todos os endpoints publicos e protegidos
- [x] Documentacao do sistema de seguranca JWT
- [x] Tabela de tratamento de erros do GlobalExceptionHandler
- [x] Documentacao das configuracoes de banco e variaveis de ambiente
- [x] Descricao da estrategia de testes (unitarios + controller)
- [x] Convencoes de codigo identificadas no projeto
- [x] Roles de usuario mapeadas: ADMIN, USER, BARBEIRO
- [x] AgendamentoStatus: CONFIRMADO, PENDENTE, CANCELADO

### 2026-06-08 — Implementacao das correcoes de seguranca e pendencias
- [x] `LoginRateLimitFilter` criado em `infra/security/` — 5 tentativas/min por IP, retorna HTTP 429
- [x] `TokenService` corrigido — agora le `api.security.token.expiration` ao inves de hardcodar 2h
- [x] `application.properties` — adicionado `server.servlet.encoding.force=true` para UTF-8 forcado
- [x] `DeletarAgendamentoPorIdUseCase` criado e registrado no `AgendamentoConfig`
- [x] `AgendamentoGateway` atualizado com `deletar(Long id)`
- [x] `AgendamentoGatewayImp` implementa `deletar` com verificacao de existencia
- [x] `AgendamentoController` — novo endpoint `DELETE /{id}` com `@PreAuthorize("hasRole('ADMIN')")`
- [x] `CurtirBarbeiroUseCase` criado e registrado no `BarbeiroConfig`
- [x] `BarbeiroGateway` atualizado com `curtir(Long id)`
- [x] `BarbeiroGatewayImp` implementa `curtir` com incremento atomico via `@Transactional`
- [x] `BarbeiroController` — novo endpoint publico `PATCH /{id}/curtir`
- [x] Confirmado: itens 1, 2, 3, 5 da lista critica ja estavam implementados no codigo

### 2026-06-08 — Verificacao de conformidade e correcao de bug
- [x] Todos os arquivos modificados verificados contra o codigo real
- [x] **BUG CORRIGIDO**: `SecurityConfig` nao tinha `permitAll()` para `PATCH /barbeiros/*/curtir` — endpoint curtir retornava 403 sem token. Adicionada a regra no SecurityConfig
- [x] Documentacao corrigida: `PATCH /barbeiros/{id}/curtir` adicionado aos endpoints publicos
- [x] Documentacao corrigida: tabela de entidades atualizada (Barbeiro inclui Curtir; Agendamento inclui Deletar)
