# FahCortes — Documentação Completa para Estudos

API REST para gerenciamento de uma barbearia: agendamentos, barbeiros, serviços, produtos, planos e unidades.  
Stack: **Spring Boot 3.2.5 · Java 17 · Spring Security (JWT) · JPA + MySQL · Clean Architecture**

---

## Sumário

1. [Visão Geral](#visão-geral)
2. [Tecnologias e Dependências](#tecnologias-e-dependências)
3. [Arquitetura Limpa (Clean Architecture)](#arquitetura-limpa)
4. [Estrutura de Pastas](#estrutura-de-pastas)
5. [Entidades do Domínio](#entidades-do-domínio)
6. [Camada de Casos de Uso](#camada-de-casos-de-uso)
7. [Padrão Gateway](#padrão-gateway)
8. [Configuração de Dependências (XConfig)](#configuração-de-dependências)
9. [Camada de Infraestrutura](#camada-de-infraestrutura)
10. [Segurança e JWT](#segurança-e-jwt)
11. [Endpoints da API](#endpoints-da-api)
12. [Tratamento de Erros](#tratamento-de-erros)
13. [Testes](#testes)
14. [Como Executar o Projeto](#como-executar-o-projeto)

---

## Visão Geral

O **FahCortes** é uma API REST que gerencia o back-end de uma barbearia. As principais funcionalidades são:

- Cadastro e autenticação de usuários com JWT
- CRUD completo de barbeiros, serviços, produtos, unidades e planos
- Criação e cancelamento de agendamentos
- Controle de acesso por roles: `ADMIN` e `USER`

---

## Tecnologias e Dependências

| Dependência | Versão | Finalidade |
|---|---|---|
| Spring Boot | 3.2.5 | Framework principal |
| Java | 17 | Linguagem |
| Spring Data JPA | (boot) | Acesso ao banco de dados |
| Spring Security | (boot) | Autenticação e autorização |
| MySQL Connector | (runtime) | Driver do banco de dados |
| Lombok | (opcional) | Redução de boilerplate |
| ModelMapper | 3.2.0 | Conversão entre camadas |
| auth0 java-jwt | 4.4.0 | Geração e validação de JWT |
| Spring Boot Validation | (boot) | Validação de DTOs com `@Valid` |
| Spring Boot Test + Security Test | (test) | JUnit 5 + Mockito + MockMvc |

---

## Arquitetura Limpa

O projeto segue os princípios da **Clean Architecture** (Robert C. Martin), dividindo o sistema em camadas concêntricas onde as dependências apontam sempre para dentro — a camada de domínio não conhece nada da infraestrutura.

```
┌──────────────────────────────────────────────────┐
│  Infraestrutura (Spring, JPA, Controllers, JWT)  │
│  ┌────────────────────────────────────────────┐  │
│  │  Casos de Uso (Regras de Negócio)          │  │
│  │  ┌──────────────────────────────────────┐  │  │
│  │  │  Entidades do Domínio (POJO puro)    │  │  │
│  │  └──────────────────────────────────────┘  │  │
│  └────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────┘
```

**Por que isso importa?**

- As entidades do domínio são POJOs Java simples — sem anotações do Spring, JPA ou qualquer framework
- Os casos de uso contêm toda a lógica de negócio e dependem apenas de interfaces (gateways)
- A infraestrutura implementa essas interfaces e pode ser trocada sem alterar o domínio
- Isso torna o código testável de forma isolada e fácil de evoluir

---

## Estrutura de Pastas

```
src/main/java/com/barbearia/fahcortes/
│
├── FahcortesApplication.java          ← Ponto de entrada + cria admin no startup
│
├── domain/                            ← DOMÍNIO (sem Spring, sem JPA)
│   ├── entities/
│   │   ├── agendamento/
│   │   │   ├── Agendamento.java
│   │   │   └── AgendamentoStatus.java (enum: CONFIRMADO, CANCELADO)
│   │   ├── barbeiro/Barbeiro.java
│   │   ├── plano/Plano.java
│   │   ├── produto/Produto.java
│   │   ├── servico/Servico.java
│   │   ├── unidade/Unidade.java
│   │   └── usuario/UsuarioEnum.java   (enum: ADMIN, USER)
│   │
│   ├── exception/
│   │   ├── EntidadeNaoEncontradaException.java
│   │   └── RegraDeNegocioException.java
│   │
│   ├── gateways/                      ← Interfaces que definem o contrato de persistência
│   │   ├── agendamento/AgendamentoGateway.java
│   │   ├── barbeiro/BarbeiroGateway.java
│   │   ├── plano/PlanoGateway.java
│   │   ├── produto/ProdutoGateway.java
│   │   ├── servico/ServicoGateway.java
│   │   ├── unidade/UnidadeGateway.java
│   │   └── usuario/UsuarioGateway.java
│   │
│   └── usecases/                      ← Uma classe por operação de negócio
│       ├── agendamento/
│       │   ├── CriarAgendamentoUseCase.java
│       │   ├── BuscarAgendamentoPorIdUseCase.java
│       │   ├── ListarAgendamentosUseCase.java
│       │   └── CancelarAgendamentoUseCase.java
│       ├── barbeiro/ (Cadastrar, Buscar, Listar, Atualizar, Deletar)
│       ├── plano/    (Cadastrar, Buscar, Listar, Atualizar, Deletar)
│       ├── produto/  (Cadastrar, Buscar, Listar, Atualizar, Deletar)
│       ├── servico/  (Cadastrar, Buscar, Listar, Remover)
│       ├── unidade/  (Cadastrar, Buscar, Listar, Atualizar, Deletar)
│       └── usuario/  (Cadastrar, Buscar por ID, Buscar por Email, Listar, Atualizar, Deletar)
│
└── infra/                             ← INFRAESTRUTURA (Spring, JPA, JWT)
    ├── config/
    │   ├── SecurityConfig.java        ← Regras de segurança + AuthenticationManager
    │   ├── CorsConfig.java            ← Configuração de CORS
    │   ├── ModelMapperConfig.java     ← Bean do ModelMapper
    │   ├── AgendamentoConfig.java     ← Instancia use cases de Agendamento
    │   ├── BarbeiroConfig.java        ← Instancia use cases de Barbeiro
    │   ├── PlanoConfig.java           ← Instancia use cases de Plano
    │   ├── ProdutoConfig.java         ← Instancia use cases de Produto
    │   ├── ServicoConfig.java         ← Instancia use cases de Servico
    │   ├── UnidadeConfig.java         ← Instancia use cases de Unidade
    │   └── UsuarioConfig.java         ← Instancia use cases de Usuario
    │
    ├── controller/
    │   ├── agendamento/AgendamentoController.java + dtos/
    │   ├── barbeiro/BarbeiroController.java + dtos/
    │   ├── plano/PlanoController.java + dtos/
    │   ├── produto/ProdutoController.java + dtos/
    │   ├── servico/ServicoController.java + dtos/
    │   ├── unidade/UnidadeController.java + dtos/
    │   ├── usuario/AuthController.java + UsuarioController.java + dtos/
    │   └── exception/
    │       ├── GlobalExceptionHandler.java
    │       └── ErrorResponse.java
    │
    ├── entities/                      ← Entidades JPA (@Entity, @Table)
    │   ├── AgendamentoEntity.java
    │   ├── BarbeiroEntity.java
    │   ├── PlanoEntity.java
    │   ├── ProdutoEntity.java
    │   ├── UnidadeEntity.java
    │   └── UsuarioEntity.java
    │
    ├── gateways/                      ← Implementações das interfaces do domínio
    │   ├── agendamento/AgendamentoGatewayImp.java
    │   ├── barbeiro/BarbeiroGatewayImp.java
    │   ├── plano/PlanoGatewayImp.java
    │   ├── produto/ProdutoGatewayImp.java
    │   ├── servico/ServicoGatewayImp.java
    │   ├── unidade/UnidadeGatewayImp.java
    │   └── usuario/UsuarioGatewayImp.java
    │
    ├── mapper/                        ← Conversão Domain ↔ Entity ↔ DTO (ModelMapper)
    │   ├── agendamento/AgendamentoMapper.java
    │   ├── barbeiro/BarbeiroMapper.java
    │   ├── plano/PlanoMapper.java
    │   ├── produto/ProdutoMapper.java
    │   ├── servico/ServicoMapper.java
    │   ├── unidade/UnidadeMapper.java
    │   └── usuario/UsuarioMapper.java
    │
    ├── persistence/                   ← Repositories Spring Data JPA
    │   ├── AgendamentoRepository.java
    │   ├── BarbeiroRepository.java
    │   ├── PlanoRepository.java
    │   ├── ProdutoRepository.java
    │   ├── ServicoRepository.java
    │   ├── UnidadeRepository.java
    │   └── UsuarioRepository.java
    │
    └── security/
        ├── SecurityFilter.java        ← Extrai e valida o JWT em cada requisição
        ├── TokenService.java          ← Gera e valida tokens JWT (auth0)
        ├── UsuarioDetailService.java  ← Carrega usuário do banco para o Spring Security
        ├── UsuarioDetails.java        ← Implementa UserDetails do Spring
        └── DadosTokenJWT.java         ← Record com o token retornado no login
```

---

## Entidades do Domínio

São POJOs Java puros (sem anotações de framework). Cada entidade representa um conceito do negócio.

### Agendamento

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `clienteId` | `Long` | ID do usuário que agendou |
| `barbeiroId` | `Long` | ID do barbeiro escolhido |
| `servicoId` | `Long` | ID do serviço contratado |
| `unidadeId` | `Long` | ID da unidade onde será atendido |
| `dataHora` | `LocalDateTime` | Data e hora do agendamento |
| `status` | `AgendamentoStatus` | CONFIRMADO ou CANCELADO |
| `observacoes` | `String` | Observações opcionais |

### Barbeiro

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `nome` | `String` | Nome do barbeiro |
| `especialidade` | `String` | Ex: "Corte Masculino" |
| `foto` | `String` | URL da foto |
| `curtidas` | `Integer` | Quantidade de curtidas |
| `ativo` | `Boolean` | Se está disponível |

### Servico

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `descricao` | `String` | Nome do serviço |
| `tempo` | `Long` | Duração em minutos |
| `valor` | `BigDecimal` | Preço |

### Produto

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `nome` | `String` | Nome do produto |
| `descricao` | `String` | Descrição |
| `preco` | `BigDecimal` | Preço |
| `imagem` | `String` | URL da imagem |
| `estoque` | `Integer` | Quantidade em estoque |

### Unidade

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `nome` | `String` | Nome da unidade |
| `endereco` | `String` | Endereço completo |
| `telefone` | `String` | Telefone de contato |
| `latitude` | `Double` | Coordenada geográfica |
| `longitude` | `Double` | Coordenada geográfica |
| `horarioAbertura` | `String` | Ex: "08:00" |
| `horarioFechamento` | `String` | Ex: "20:00" |

### Plano

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `nome` | `String` | Nome do plano |
| `descricao` | `String` | Descrição do plano |
| `preco` | `BigDecimal` | Preço mensal |
| `beneficios` | `List<String>` | Lista de benefícios |
| `validade` | `Integer` | Validade em dias |

### Usuario (via UsuarioEntity — persiste no banco)

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Identificador único |
| `nome` | `String` | Nome do usuário |
| `email` | `String` | Login (único) |
| `senha` | `String` | Hash BCrypt |
| `role` | `UsuarioEnum` | ADMIN ou USER |

---

## Camada de Casos de Uso

Cada operação de negócio é uma classe separada com um método `execute()`. Isso segue o **Princípio da Responsabilidade Única (SRP)** e deixa o código muito fácil de testar.

**Exemplo — CriarAgendamentoUseCase:**

```java
public class CriarAgendamentoUseCase {

    private final AgendamentoGateway agendamentoGateway;

    public CriarAgendamentoUseCase(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    public Agendamento execute(Agendamento agendamento) {
        // Regra de negócio: se não informar status, assume CONFIRMADO
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(AgendamentoStatus.CONFIRMADO);
        }
        return agendamentoGateway.criar(agendamento);
    }
}
```

**Pontos importantes:**
- O caso de uso recebe uma interface (`AgendamentoGateway`), não uma implementação concreta
- Não há `@Service`, `@Component` ou qualquer anotação do Spring — é Java puro
- As instâncias são criadas manualmente nas classes `XConfig.java`
- A lógica de negócio fica concentrada aqui, separada da infraestrutura

---

## Padrão Gateway

O **Gateway** é a ponte entre o domínio e o banco de dados.

**No domínio** — definimos apenas a interface (o contrato):

```java
// domain/gateways/agendamento/AgendamentoGateway.java
public interface AgendamentoGateway {
    Agendamento criar(Agendamento agendamento);
    Agendamento buscarPorId(Long id);
    List<Agendamento> listarPorCliente(Long clienteId);
    List<Agendamento> listarTodos();
    Agendamento cancelar(Long id);
}
```

**Na infraestrutura** — implementamos com Spring Data JPA:

```java
// infra/gateways/agendamento/AgendamentoGatewayImp.java
@Component
public class AgendamentoGatewayImp implements AgendamentoGateway {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoMapper agendamentoMapper;

    @Override
    @Transactional
    public Agendamento cancelar(Long id) {
        AgendamentoEntity entity = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(...));

        // Regra de negócio defensiva na camada de gateway
        if (entity.getStatus() == AgendamentoStatus.CANCELADO) {
            throw new RegraDeNegocioException("Agendamento já está cancelado.");
        }

        entity.setStatus(AgendamentoStatus.CANCELADO);
        return agendamentoMapper.toDomain(agendamentoRepository.save(entity));
    }
}
```

**Benefício:** O domínio nunca importa JPA, Spring ou MySQL — se precisar trocar o banco, só muda a implementação do gateway.

---

## Configuração de Dependências

Como os casos de uso são Java puro (sem `@Service`), o Spring não os detecta automaticamente. As classes `XConfig.java` fazem a injeção de dependência manualmente usando `@Bean`:

```java
// infra/config/AgendamentoConfig.java
@Configuration
public class AgendamentoConfig {

    @Bean
    public CriarAgendamentoUseCase criarAgendamentoUseCase(AgendamentoGateway agendamentoGateway) {
        return new CriarAgendamentoUseCase(agendamentoGateway);
    }

    @Bean
    public CancelarAgendamentoUseCase cancelarAgendamentoUseCase(AgendamentoGateway agendamentoGateway) {
        return new CancelarAgendamentoUseCase(agendamentoGateway);
    }
    // ... demais use cases
}
```

O Spring injeta `AgendamentoGateway` automaticamente porque a implementação `AgendamentoGatewayImp` está anotada com `@Component`.

---

## Camada de Infraestrutura

### Entities JPA

São as classes que mapeiam para as tabelas do banco. Diferem das entidades do domínio por terem anotações JPA:

```java
@Entity
@Table(name = "agendamentos")
public class AgendamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AgendamentoStatus status;
    // ...
}
```

### Mappers

Convertem entre as três representações: **Domain ↔ Entity ↔ DTO**.

```
Controller recebe:  AgendamentoRequestDto
     ↓ mapper.toDomain()
UseCase opera com: Agendamento  (domínio puro)
     ↓ mapper.toEntity()
Gateway persiste:  AgendamentoEntity  (JPA)
     ↓ mapper.toDomain()
UseCase retorna:   Agendamento
     ↓ mapper.toResponse()
Controller retorna: AgendamentoResponseDto
```

Os mappers usam **ModelMapper** para copiar campos automaticamente por nome:

```java
@Component
public class AgendamentoMapper {
    private final ModelMapper modelMapper;

    public Agendamento toDomain(AgendamentoRequestDto dto) {
        return modelMapper.map(dto, Agendamento.class);
    }

    public AgendamentoResponseDto toResponse(Agendamento agendamento) {
        return modelMapper.map(agendamento, AgendamentoResponseDto.class);
    }
}
```

### Repositories

Interfaces que estendem `JpaRepository` — o Spring Data gera a implementação em tempo de execução:

```java
public interface AgendamentoRepository extends JpaRepository<AgendamentoEntity, Long> {
    List<AgendamentoEntity> findByClienteId(Long clienteId); // gerado pelo nome do método
}
```

---

## Segurança e JWT

### Fluxo Completo

```
1. POST /login  { email, senha }
        ↓
2. AuthController → authenticationManager.authenticate()
        ↓
3. Spring Security chama UsuarioDetailService.loadUserByUsername(email)
        ↓
4. Carrega UsuarioEntity do banco via UsuarioRepository
        ↓
5. BCrypt verifica a senha
        ↓
6. Se autenticado: TokenService.gerarToken(usuario) → JWT assinado (HMAC256, 2h)
        ↓
7. Retorna: { token: "eyJ...", role: "ADMIN" }

---

Em requisições subsequentes:
        ↓
8. SecurityFilter.doFilterInternal() → lê header Authorization: Bearer <token>
        ↓
9. TokenService.validarToken(token) → retorna o email se válido, "" se inválido
        ↓
10. Se email válido: carrega UsuarioDetails e seta no SecurityContextHolder
        ↓
11. Spring Security verifica as permissões da requisição
```

### TokenService

```java
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;   // lido do application.properties

    public String gerarToken(UsuarioEntity usuario) {
        Algorithm algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("fahcortes-api")
                .withSubject(usuario.getEmail())
                .withExpiresAt(LocalDateTime.now().plusHours(2)
                        .toInstant(ZoneOffset.of("-03:00")))
                .sign(algoritmo);
    }

    public String validarToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("fahcortes-api")
                    .build()
                    .verify(token)
                    .getSubject(); // retorna o email
        } catch (Exception e) {
            return ""; // token inválido
        }
    }
}
```

### Regras de Acesso (SecurityConfig)

| Método | Path | Acesso |
|---|---|---|
| POST | `/login` | Público |
| POST | `/usuarios` | Público (cadastro) |
| GET | `/servico/**` | Público |
| GET | `/barbeiros/**` | Público |
| GET | `/produtos/**` | Público |
| GET | `/unidades/**` | Público |
| GET | `/planos/**` | Público |
| * | Qualquer outro | Autenticado |

Além das regras globais, endpoints de escrita usam `@PreAuthorize("hasRole('ADMIN')")`:

- POST/DELETE em `/barbeiros`, `/servico`, `/produtos`, `/unidades`, `/planos`
- PUT em `/barbeiros`, `/produtos`, `/unidades`, `/planos`

**Nota sobre respostas de erro de segurança:**  
O projeto usa sessão stateless sem `AuthenticationEntryPoint` configurado. Por isso:
- Requisição sem token para endpoint autenticado → **HTTP 403** (não 401)
- Requisição com token válido mas role insuficiente → **HTTP 403**

---

## Endpoints da API

### Autenticação

| Método | Path | Auth | Body | Resposta |
|---|---|---|---|---|
| POST | `/login` | Não | `{ "email": "...", "senha": "..." }` | `{ "token": "...", "role": "..." }` |
| POST | `/usuarios` | Não | UsuarioRequestDto | UsuarioResponseDto (201) |

### Usuários

| Método | Path | Auth | Descrição |
|---|---|---|---|
| GET | `/usuarios` | Token | Lista todos |
| GET | `/usuarios/{id}` | Token | Busca por ID |
| GET | `/usuarios/email/{email}` | Token | Busca por email |
| PUT | `/usuarios/{id}` | Token | Atualiza |
| DELETE | `/usuarios/{id}` | Token | Remove |

### Barbeiros

| Método | Path | Auth | Descrição |
|---|---|---|---|
| GET | `/barbeiros` | Não | Lista todos |
| GET | `/barbeiros/{id}` | Não | Busca por ID |
| POST | `/barbeiros` | ADMIN | Cadastra |
| PUT | `/barbeiros/{id}` | ADMIN | Atualiza |
| DELETE | `/barbeiros/{id}` | ADMIN | Remove |

### Serviços

| Método | Path | Auth | Descrição |
|---|---|---|---|
| GET | `/servico` | Não | Lista todos |
| GET | `/servico/{id}` | Não | Busca por ID |
| POST | `/servico` | ADMIN | Cadastra |
| DELETE | `/servico/{id}` | ADMIN | Remove |

### Produtos

| Método | Path | Auth | Descrição |
|---|---|---|---|
| GET | `/produtos` | Não | Lista todos |
| GET | `/produtos/{id}` | Não | Busca por ID |
| POST | `/produtos` | ADMIN | Cadastra |
| PUT | `/produtos/{id}` | ADMIN | Atualiza |
| DELETE | `/produtos/{id}` | ADMIN | Remove |

### Unidades

| Método | Path | Auth | Descrição |
|---|---|---|---|
| GET | `/unidades` | Não | Lista todas |
| GET | `/unidades/{id}` | Não | Busca por ID |
| POST | `/unidades` | ADMIN | Cadastra |
| PUT | `/unidades/{id}` | ADMIN | Atualiza |
| DELETE | `/unidades/{id}` | ADMIN | Remove |

### Planos

| Método | Path | Auth | Descrição |
|---|---|---|---|
| GET | `/planos` | Não | Lista todos |
| GET | `/planos/{id}` | Não | Busca por ID |
| POST | `/planos` | ADMIN | Cadastra |
| PUT | `/planos/{id}` | ADMIN | Atualiza |
| DELETE | `/planos/{id}` | ADMIN | Remove |

### Agendamentos

| Método | Path | Auth | Descrição |
|---|---|---|---|
| GET | `/agendamentos` | Token | Lista todos (ou filtrado por `?clienteId=X`) |
| GET | `/agendamentos/{id}` | Token | Busca por ID |
| POST | `/agendamentos` | Token | Cria agendamento |
| PATCH | `/agendamentos/{id}/cancelar` | Token | Cancela agendamento |

---

## Tratamento de Erros

Centralizado em `GlobalExceptionHandler` com `@RestControllerAdvice`. Todas as exceções retornam um JSON padronizado:

```json
{
  "codigo": "NOT_FOUND",
  "mensagem": "Servico com id 99 não encontrado"
}
```

Para erros de validação, inclui detalhes por campo:

```json
{
  "codigo": "VALIDATION_ERROR",
  "mensagem": "Dados inválidos na requisição",
  "detalhes": [
    { "campo": "email", "mensagem": "não deve estar em branco" }
  ]
}
```

| Exceção | Status HTTP | Código |
|---|---|---|
| `EntidadeNaoEncontradaException` | 404 | `NOT_FOUND` |
| `RegraDeNegocioException` | 409 | `CONFLICT` |
| `MethodArgumentNotValidException` | 400 | `VALIDATION_ERROR` |
| `AccessDeniedException` | 403 | `FORBIDDEN` |
| `AuthenticationException` | 401 | `UNAUTHORIZED` |
| `Exception` (genérico) | 500 | `INTERNAL_SERVER_ERROR` |

**Exceções do domínio:**

```java
// Lançada quando o registro não existe no banco
public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade, Long id) {
        super(entidade + " com id " + id + " não encontrado.");
    }
}

// Lançada para violações de regra de negócio (ex: cancelar agendamento já cancelado)
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
```

---

## Testes

### Tipos de Teste

O projeto tem **90 testes** divididos em:

#### 1. Testes de Controller (`@WebMvcTest`)

Testam apenas a camada HTTP: rotas, status codes, JSON de resposta e segurança.  
Usam `MockMvc` para simular requisições sem subir o servidor completo.

```java
@WebMvcTest(ServicoController.class)
@Import({SecurityConfig.class, CorsConfig.class})  // carrega as regras de segurança reais
class ServicoControllerTest {

    @Autowired private MockMvc mockMvc;

    // Mocks obrigatórios para o contexto de segurança funcionar
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioDetailService usuarioDetailService;
    @MockBean private UsuarioRepository usuarioRepository;

    // Mock dos casos de uso (não queremos testar lógica de negócio aqui)
    @MockBean private CadastrarServicoUseCase cadastrarServicoUseCase;

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrar_comoAdmin_deveRetornar201() throws Exception {
        when(cadastrarServicoUseCase.execute(any())).thenReturn(servicoSalvo);

        mockMvc.perform(post("/servico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"Corte\",\"tempo\":30,\"valor\":25.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Corte"));
    }
}
```

**Por que `@Import({SecurityConfig.class, CorsConfig.class})`?**  
O `@WebMvcTest` carrega apenas o controller isolado. Para que `@PreAuthorize` e as regras de acesso funcionem nos testes, precisamos importar explicitamente as configurações de segurança.

**Por que mockar `TokenService`, `UsuarioDetailService` e `UsuarioRepository`?**  
A `SecurityConfig` e o `SecurityFilter` dependem dessas classes. Mesmo que não os usemos diretamente no teste, o Spring precisa encontrá-las para montar o contexto.

#### 2. Testes de Integração (`@SpringBootTest`)

Sobem o contexto completo da aplicação com banco H2 em memória.

```java
@SpringBootTest
class FahcortesApplicationTests {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CommandLineRunner commandLineRunner;

    @Test
    void adminDeveSerCriadoNaInicializacao() {
        assertTrue(usuarioRepository.findByEmail("admin@fahcortes.com").isPresent());
    }

    @Test
    void initNaoCriaDuplicataDeAdmin() throws Exception {
        commandLineRunner.run(); // roda o init de novo
        long count = usuarioRepository.findAll().stream()
                .filter(u -> "admin@fahcortes.com".equals(u.getEmail()))
                .count();
        assertEquals(1, count); // não deve duplicar
    }
}
```

### Comportamento de Segurança nos Testes

| Cenário | Status esperado |
|---|---|
| Requisição sem token para rota pública | 200 |
| Requisição sem token para rota protegida | **403** (não 401!) |
| `@WithMockUser(roles = "USER")` em rota ADMIN | 403 |
| `@WithMockUser(roles = "ADMIN")` | 200/201/204 conforme operação |

> O Spring Security em modo stateless retorna **403** para requisições não autenticadas porque não há `AuthenticationEntryPoint` configurado — o `Http403ForbiddenEntryPoint` é o padrão.

---

## Inicialização do Projeto

### FahcortesApplication

O ponto de entrada registra um `CommandLineRunner` que cria o usuário admin automaticamente na primeira execução:

```java
@SpringBootApplication
public class FahcortesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FahcortesApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByEmail("admin@fahcortes.com").isEmpty()) {
                UsuarioEntity admin = new UsuarioEntity();
                admin.setNome("Admin");
                admin.setEmail("admin@fahcortes.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setRole(UsuarioEnum.ADMIN);
                usuarioRepository.save(admin);
            }
        };
    }
}
```

---

## Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Docker (para subir o MySQL via Docker Compose)
- Maven

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd fahcortes

# 2. Suba o banco de dados (Docker Compose detectado automaticamente pelo Spring Boot)
docker compose up -d

# 3. Execute a aplicação
./mvnw spring-boot:run

# A API estará disponível em: http://localhost:8080
```

### Primeiros Passos na API

```bash
# 1. Fazer login com o admin criado automaticamente
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@fahcortes.com","senha":"admin123"}'

# Resposta: { "token": "eyJ...", "role": "ADMIN" }

# 2. Usar o token em requisições protegidas
curl http://localhost:8080/agendamentos \
  -H "Authorization: Bearer eyJ..."
```

### Executar os Testes

```bash
./mvnw test
```

---

## Padrões e Boas Práticas Aplicadas

| Padrão | Onde | Benefício |
|---|---|---|
| Clean Architecture | Estrutura geral | Domínio isolado, fácil de testar |
| Use Case por operação | `domain/usecases/` | SRP, testabilidade |
| Gateway (Port & Adapter) | `domain/gateways/` | Abstração do banco |
| DTO (Request/Response) | Controllers | Desacopla API do domínio |
| Global Exception Handler | `GlobalExceptionHandler` | Respostas de erro padronizadas |
| JWT Stateless | `SecurityFilter` | Sem sessão no servidor |
| BCrypt | `PasswordEncoder` | Senhas nunca em texto puro |
| `@PreAuthorize` | Controllers | Controle de acesso declarativo |
| `@Transactional` | Gateways | Consistência em operações de escrita |
