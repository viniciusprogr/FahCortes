# 📋 RELATÓRIO DETALHADO - Backend FahCortes

**Data:** 2026-05-13  
**Repositório:** https://github.com/viniciusprogr/FahCortes  
**Stack:** Spring Boot 3.4.2 + Java 25 + MySQL + JWT (Auth0) + Clean Architecture

---

## 🐛 BUGS CONFIRMADOS

### 1. ⚠️ CRÍTICO - Login não retorna o tipo/perfil do usuário

**Arquivo:** `src/main/java/com/barbearia/fahcortes/infra/controller/usuario/AuthController.java`

**Problema:**
- `DadosTokenJWT` (response DTO) retorna apenas `String token`
- Falta o campo `tipo` que indica o perfil (ADMIN, USER, BARBEIRO)
- Frontend não consegue saber o perfil do usuário após login real

**Impacto:** 🔴 ALTO - Bloqueia funcionalidade de autenticação com perfis

**Solução:**
```java
// Criar record: public record DadosTokenJWT(String token, String tipo) {}
// No AuthController.efetuarLogin():
var tipo = usuarioDetails.getUsuarioEntity().getTipo().name();
return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, tipo));
```

**Status:** ✅ JÁ APLICADO

---

### 2. ⚠️ CRÍTICO - Bug de re-encode de senha ao atualizar usuário

**Arquivo:** Método `atualizarUsuario()` do UsuarioController/UseCase

**Problema:**
- Quando usuário é editado, a senha é re-encriptada mesmo que não tenha sido alterada
- Resultado: Senha antiga se torna inválida, usuário não consegue fazer login
- Teste: Login → Editar nome → Tentar login novamente = FALHA

**Impacto:** 🔴 CRÍTICO - Usuários não conseguem editar perfil

**Solução:**
```java
// Opção A: Comparar senha atual com a que vem no request
if (!form.getSenha().equals(usuarioAtual.getSenha())) {
    usuarioAtual.setSenha(passwordEncoder.encode(form.getSenha()));
}

// Opção B: Usar campo separado "novaSenha" (melhor prática)
if (form.getNovaSenha() != null && !form.getNovaSenha().isEmpty()) {
    usuarioAtual.setSenha(passwordEncoder.encode(form.getNovaSenha()));
}
```

**Status:** ❌ NÃO CORRIGIDO

---

### 3. ⚠️ ALTO - CORS não cobre origem do frontend em produção

**Arquivo:** Configuração de CORS (provavelmente em `SecurityConfig` ou `application.properties`)

**Problema:**
- CORS está configurado para `localhost:5173` ou similar (dev Vite)
- Frontend em produção usa `https://[username].github.io`
- Requisições do frontend em produção serão bloqueadas por CORS

**Impacto:** 🔴 ALTO - App não funciona em produção

**Solução:**
```properties
# application.properties
cors.allowed-origins=http://localhost:3000,http://localhost:8080,https://*.github.io
```

**Ou via código:**
```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String allowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
                if (allowedOrigins == null) {
                    allowedOrigins = "http://localhost:3000,http://localhost:8080";
                }
                registry.addMapping("/**")
                    .allowedOrigins(allowedOrigins.split(","))
                    .allowedMethods("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

**Status:** ❌ NÃO CORRIGIDO

---

### 4. ⚠️ MÉDIO - JWT secret está hardcoded

**Arquivo:** Classe `TokenService` ou configuração de segurança

**Problema:**
- Secret do JWT provavelmente está no código-fonte: `"minha-chave-secreta"` ou similar
- **RISCO CRÍTICO DE SEGURANÇA:** Qualquer pessoa pode forjar tokens
- Será exposto no repositório público do GitHub

**Impacto:** 🔴 CRÍTICO - Segurança comprometida

**Solução:**
```properties
# application.properties
jwt.secret=${JWT_SECRET:chave-padrao-apenas-dev}
jwt.expiration=${JWT_EXPIRATION:86400000}

# application-prod.properties
jwt.secret=${JWT_SECRET}
```

```java
// Usar via @Value
@Value("${jwt.secret}")
private String jwtSecret;
```

**Status:** ❌ NÃO CORRIGIDO

---

## 📦 ENDPOINTS IMPLEMENTADOS

### ✅ Já Existem

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|-------------|-----------|
| POST | `/login` | Público | Login com email/senha |
| POST | `/usuarios` | Público | Registrar novo usuário |
| GET | `/usuarios` | JWT | Listar todos |
| GET | `/usuarios/{id}` | JWT | Buscar por ID |
| GET | `/usuarios/email` | JWT | Buscar por email |
| PUT | `/usuarios/{id}` | JWT | Atualizar usuário |
| DELETE | `/usuarios/{id}` | JWT | Deletar usuário |
| GET | `/servico` | ADMIN | Listar serviços |
| GET | `/servico/{id}` | Público | Buscar serviço por ID |
| POST | `/servico` | ADMIN | Criar serviço |
| DELETE | `/servico/{id}` | ADMIN | Deletar serviço |

---

## ❌ ENDPOINTS FALTANTES (CRÍTICOS PARA O FRONTEND)

### 1. 🔴 BARBEIRO
```
GET    /barbeiros              (listar todos)
GET    /barbeiros/{id}         (buscar por ID)
POST   /barbeiros              (criar - ADMIN)
PUT    /barbeiros/{id}         (atualizar - ADMIN)
DELETE /barbeiros/{id}         (deletar - ADMIN)
```

**Por que é urgente:**
- Frontend tem página de barbeiros (`BarberPage.tsx`)
- Dados estão hardcoded em localStorage
- Curtidas/likes precisam de ID real do banco

**Estrutura esperada:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "especialidade": "Corte clássico",
  "foto": "url ou filename",
  "curtidas": 15,
  "ativo": true
}
```

---

### 2. 🔴 AGENDAMENTO
```
GET    /agendamentos?clienteId={id}    (listar do usuário)
GET    /agendamentos/{id}              (buscar um)
POST   /agendamentos                   (criar)
PATCH  /agendamentos/{id}/cancelar     (cancelar)
GET    /agendamentos/disponibilidades  (horários livres)
```

**Por que é urgente:**
- Frontend tem página de agendamento (`AgendarPage.tsx`)
- Atualmente salva só em localStorage
- Precisa validar conflitos de horário no backend

**Estrutura esperada:**
```json
{
  "id": 1,
  "clienteId": 5,
  "barbeiroId": 3,
  "servicoId": 2,
  "unidadeId": 1,
  "dataHora": "2026-05-20T14:30:00",
  "status": "confirmado",
  "observacoes": "Sem barba"
}
```

---

### 3. 🔴 PRODUTO
```
GET    /produtos              (listar)
GET    /produtos/{id}         (buscar por ID)
POST   /produtos              (criar - ADMIN)
PUT    /produtos/{id}         (atualizar - ADMIN)
DELETE /produtos/{id}         (deletar - ADMIN)
```

**Estrutura esperada:**
```json
{
  "id": 1,
  "nome": "Óleo pós-barba",
  "descricao": "Hidratante premium",
  "preco": 45.90,
  "imagem": "url ou filename",
  "estoque": 50
}
```

---

### 4. 🔴 UNIDADE
```
GET    /unidades              (listar)
GET    /unidades/{id}         (buscar por ID)
POST   /unidades              (criar - ADMIN)
PUT    /unidades/{id}         (atualizar - ADMIN)
DELETE /unidades/{id}         (deletar - ADMIN)
```

**Estrutura esperada:**
```json
{
  "id": 1,
  "nome": "Unidade Centro",
  "endereco": "Rua Principal, 123",
  "telefone": "11 99999-9999",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "horarioAbertura": "09:00",
  "horarioFechamento": "18:00"
}
```

---

### 5. 🔴 PLANO
```
GET    /planos              (listar)
GET    /planos/{id}         (buscar por ID)
POST   /planos              (criar - ADMIN)
PUT    /planos/{id}         (atualizar - ADMIN)
DELETE /planos/{id}         (deletar - ADMIN)
```

**Estrutura esperada:**
```json
{
  "id": 1,
  "nome": "Plano Gold",
  "descricao": "Cortes ilimitados",
  "preco": 199.90,
  "beneficios": ["Corte grátis", "Barba grátis"],
  "validade": 30
}
```

---

## 🔧 MELHORIAS RECOMENDADAS

### 1. Validação de Entrada
**Problema:** Sem validação no backend das requisições  
**Solução:** Adicionar `@Valid` e `@NotBlank`, `@Email`, etc. nas DTOs

```java
public record CriarUsuarioDto(
    @NotBlank @Email String email,
    @NotBlank @Size(min=3) String nome,
    @NotBlank @Size(min=6) String senha
) {}
```

---

### 2. Error Handling Padronizado
**Problema:** Erros retornam diferentes formatos  
**Solução:** Criar GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(404)
            .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
    }
}

record ErrorResponse(String code, String message) {}
```

---

### 3. Paginação para Listagens
**Problema:** GET /servicos retorna todos (pode ser 10k+ registros)  
**Solução:** Implementar PageRequest

```java
@GetMapping
public Page<ServicoResponseDto> listar(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
) {
    return servicoService.listar(PageRequest.of(page, size));
}
```

---

### 4. Filtros nas Listagens
**Problema:** Não há forma de filtrar dados  
**Solução:** Adicionar QueryParams

```java
@GetMapping("/servicos")
public List<ServicoResponseDto> listar(
    @RequestParam(required = false) String nome,
    @RequestParam(required = false) BigDecimal precoMin,
    @RequestParam(required = false) BigDecimal precoMax
) {
    // Filtrar baseado nos parâmetros
}
```

---

### 5. Rate Limiting
**Problema:** Sem proteção contra brute force no login  
**Solução:** Implementar Spring Security Rate Limiting

```java
@RateLimiter(name = "loginAttempts")
@PostMapping("/login")
public ResponseEntity<DadosTokenJWT> login(...) { ... }
```

---

### 6. Logging Estruturado
**Problema:** Sem logs de atividades  
**Solução:** Adicionar logging em operações críticas

```java
log.info("Usuário {} realizou login", email);
log.warn("Tentativa de acesso não autorizado em {}", endpoint);
log.error("Erro ao processar agendamento", exception);
```

---

### 7. Transações Distribuídas
**Problema:** Operações de agendamento não são atômicas  
**Solução:** Adicionar `@Transactional`

```java
@Transactional
public AgendamentoResponseDto criar(CriarAgendamentoDto dto) {
    // Validar disponibilidade
    // Criar agendamento
    // Atualizar barbeiro
    // Enviar notificação (se falhar, tudo se desfaz)
}
```

---

## 🔄 INTEGRAÇÃO COM FRONTEND

### Header Necessário
**Frontend envia:**
```
Authorization: Bearer {token}
```

**Backend deve validar em todos endpoints protegidos** ✅ Já faz

---

### DTOs de Resposta
**Frontend espera formato consistente:**
```json
{
  "id": 1,
  "campo1": "valor",
  "campo2": 123
}
```

**Para listas:**
```json
[
  { "id": 1, ... },
  { "id": 2, ... }
]
```

---

### Codes de Erro HTTP

| Status | Quando usar |
|--------|------------|
| 400 | Requisição inválida (validação falhou) |
| 401 | Não autenticado (sem token ou token inválido) |
| 403 | Não autorizado (ADMIN requerido, mas user é USER) |
| 404 | Recurso não encontrado |
| 409 | Conflito (ex: email já existe, horário já agendado) |
| 500 | Erro do servidor |

---

## 📋 CHECKLIST DE CORREÇÕES

### 🔴 CRÍTICOS (Fazer PRIMEIRO)
- [ ] Bug #1: Login retorna `tipo` 
- [ ] Bug #2: Corrigir re-encode de senha
- [ ] Bug #4: JWT secret em variável de ambiente
- [ ] CORS: Permitir origem do frontend em produção

### 🟠 ALTOS (Fazer SEGUNDA)
- [ ] Endpoint: GET/POST/PUT/DELETE `/barbeiros`
- [ ] Endpoint: GET/POST/PATCH `/agendamentos`
- [ ] Endpoint: GET/POST/PUT/DELETE `/produtos`
- [ ] Endpoint: GET/POST/PUT/DELETE `/unidades`
- [ ] Endpoint: GET/POST/PUT/DELETE `/planos`

### 🟡 MÉDIOS (Fazer TERCEIRA)
- [ ] Validação de entrada em todos DTOs
- [ ] GlobalExceptionHandler para erros consistentes
- [ ] Paginação em listagens
- [ ] Filtros avançados

### 🟢 BAIXOS (Melhorias)
- [ ] Rate limiting
- [ ] Logging estruturado
- [ ] Cache de serviços
- [ ] Documentação Swagger/OpenAPI

---

## 🚀 ORDEM RECOMENDADA DE IMPLEMENTAÇÃO

### Semana 1: Correções Críticas
1. Bug do login (tipo) ✅ JÁ FEITO
2. Bug da senha (re-encode)
3. JWT secret
4. CORS

### Semana 2: Endpoints Faltantes
5. Barbeiro (CRUD completo)
6. Agendamento (CRUD + validação)

### Semana 3: Mais Entidades
7. Produto
8. Unidade
9. Plano

### Semana 4: Melhorias
10. Validação
11. Error handling
12. Paginação
13. Filtros

---

## 📞 IMPACTO NO FRONTEND

| Bug/Falta | Impacto no Frontend | Urgência |
|-----------|-------------------|----------|
| Login não retorna tipo | Perfis não funcionam | 🔴 CRÍTICA |
| Senha re-encode | Usuários não conseguem editar perfil | 🔴 CRÍTICA |
| CORS | App não funciona em produção | 🔴 CRÍTICA |
| Sem barbeiros | BarberPage vazio (só dados hardcoded) | 🟠 ALTA |
| Sem agendamento | AgendarPage só salva em localStorage | 🟠 ALTA |
| Sem produtos | ProductsPage vazio | 🟠 ALTA |
| Sem unidades | UnidadesPage vazio | 🟠 ALTA |
| Sem planos | PlanosPage vazio | 🟠 ALTA |

---

## 📚 Referências

- Repositório: https://github.com/viniciusprogr/FahCortes
- Frontend: C:\Users\User\Documents\projeto-template-barbearia-master\
- Documentação frontend: INTEGRATION_COMPLETE.md

---

**Relatório gerado:** 2026-05-13  
**Por:** Claude Code Analysis
