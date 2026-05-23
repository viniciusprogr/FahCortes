package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.usecases.produto.*;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.produto.ProdutoController;
import com.barbearia.fahcortes.infra.controller.produto.dtos.ProdutoRequestDto;
import com.barbearia.fahcortes.infra.controller.produto.dtos.ProdutoResponseDto;
import com.barbearia.fahcortes.infra.mapper.produto.ProdutoMapper;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import com.barbearia.fahcortes.infra.security.TokenService;
import com.barbearia.fahcortes.infra.security.UsuarioDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class ProdutoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ProdutoMapper produtoMapper;
    @MockBean private CadastrarProdutoUseCase cadastrarProdutoUseCase;
    @MockBean private BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    @MockBean private ListarTodosProdutosUseCase listarTodosProdutosUseCase;
    @MockBean private AtualizarProdutoPorIdUseCase atualizarProdutoPorIdUseCase;
    @MockBean private DeletarProdutoPorIdUseCase deletarProdutoPorIdUseCase;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioDetailService usuarioDetailService;
    @MockBean private UsuarioRepository usuarioRepository;

    private ProdutoResponseDto mockResponse(Long id, String nome, BigDecimal preco) {
        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(id);
        dto.setNome(nome);
        dto.setPreco(preco);
        dto.setEstoque(10);
        return dto;
    }

    @Test
    void listarTodos_deveRetornar200() throws Exception {
        List<Produto> produtos = List.of(new Produto(1L, "Pomada", "Desc", new BigDecimal("29.90"), null, 10));
        List<ProdutoResponseDto> resposta = List.of(mockResponse(1L, "Pomada", new BigDecimal("29.90")));

        when(listarTodosProdutosUseCase.execute()).thenReturn(produtos);
        when(produtoMapper.toResponseList(produtos)).thenReturn(resposta);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Pomada"));
    }

    @Test
    void buscarPorId_deveRetornar200() throws Exception {
        Produto produto = new Produto(1L, "Pomada", "Desc", new BigDecimal("29.90"), null, 10);
        ProdutoResponseDto resposta = mockResponse(1L, "Pomada", new BigDecimal("29.90"));

        when(buscarProdutoPorIdUseCase.execute(1L)).thenReturn(produto);
        when(produtoMapper.toResponse(produto)).thenReturn(resposta);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pomada"));
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveRetornar404() throws Exception {
        when(buscarProdutoPorIdUseCase.execute(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Produto", 99L));

        mockMvc.perform(get("/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrar_comoAdmin_deveRetornar201() throws Exception {
        String body = """
                {"nome":"Pomada","descricao":"Fixa\\u00e7\\u00e3o forte","preco":29.90,"estoque":10}
                """;
        Produto domain = new Produto(null, "Pomada", "Fixação forte", new BigDecimal("29.90"), null, 10);
        Produto salvo = new Produto(1L, "Pomada", "Fixação forte", new BigDecimal("29.90"), null, 10);
        ProdutoResponseDto resposta = mockResponse(1L, "Pomada", new BigDecimal("29.90"));

        when(produtoMapper.toDomain(any(ProdutoRequestDto.class))).thenReturn(domain);
        when(cadastrarProdutoUseCase.execute(domain)).thenReturn(salvo);
        when(produtoMapper.toResponse(salvo)).thenReturn(resposta);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Pomada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizar_comoAdmin_deveRetornar200() throws Exception {
        String body = """
                {"nome":"Pomada Premium","preco":39.90,"estoque":5}
                """;
        Produto domain = new Produto(null, "Pomada Premium", null, new BigDecimal("39.90"), null, 5);
        Produto atualizado = new Produto(1L, "Pomada Premium", null, new BigDecimal("39.90"), null, 5);
        ProdutoResponseDto resposta = mockResponse(1L, "Pomada Premium", new BigDecimal("39.90"));

        when(produtoMapper.toDomain(any(ProdutoRequestDto.class))).thenReturn(domain);
        when(atualizarProdutoPorIdUseCase.execute(domain, 1L)).thenReturn(atualizado);
        when(produtoMapper.toResponse(atualizado)).thenReturn(resposta);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pomada Premium"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletar_comoAdmin_deveRetornar204() throws Exception {
        doNothing().when(deletarProdutoPorIdUseCase).execute(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void cadastrar_comoUser_deveRetornar403() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"X\",\"preco\":10}"))
                .andExpect(status().isForbidden());
    }
}
