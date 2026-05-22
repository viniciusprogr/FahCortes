package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.servico.Servico;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.usecases.servico.*;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.servico.ServicoController;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoRequestDto;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoResponseDto;
import com.barbearia.fahcortes.infra.mapper.servico.ServicoMapper;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServicoController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ServicoMapper servicoMapper;
    @MockBean
    private CadastrarServicoUseCase cadastrarServicoUseCase;
    @MockBean
    private BuscarServicoPorIdUseCase buscarServicoPorIdUseCase;
    @MockBean
    private RemoverServicoPorIdUseCase removerServicoPorIdUseCase;
    @MockBean
    private ListarTodosServicosUseCase listarTodosServicosUseCase;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private UsuarioDetailService usuarioDetailService;
    @MockBean
    private UsuarioRepository usuarioRepository;

    private ServicoResponseDto mockResponse(Long id, String descricao, BigDecimal valor) {
        ServicoResponseDto dto = new ServicoResponseDto();
        dto.setId(id);
        dto.setDescricao(descricao);
        dto.setTempo(30L);
        dto.setValor(valor);
        return dto;
    }

    @Test
    void buscarPorId_deveRetornar200() throws Exception {
        Servico servico = new Servico(1L, "Corte simples", 30L, new BigDecimal("25.00"));
        ServicoResponseDto resposta = mockResponse(1L, "Corte simples", new BigDecimal("25.00"));

        when(buscarServicoPorIdUseCase.execute(1L)).thenReturn(servico);
        when(servicoMapper.toResponseDto(servico)).thenReturn(resposta);

        mockMvc.perform(get("/servico/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Corte simples"))
                .andExpect(jsonPath("$.valor").value(25.00));
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveRetornar404() throws Exception {
        when(buscarServicoPorIdUseCase.execute(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Serviço", 99L));

        mockMvc.perform(get("/servico/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarTodos_comoAdmin_deveRetornar200() throws Exception {
        List<Servico> servicos = List.of(new Servico(1L, "Corte", 30L, new BigDecimal("25.00")));
        List<ServicoResponseDto> resposta = List.of(mockResponse(1L, "Corte", new BigDecimal("25.00")));

        when(listarTodosServicosUseCase.execute()).thenReturn(servicos);
        when(servicoMapper.toResponseDtoToList(servicos)).thenReturn(resposta);

        mockMvc.perform(get("/servico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Corte"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrar_comoAdmin_deveRetornar200() throws Exception {
        String body = """
                {"descricao":"Barba","tempo":20,"valor":15.00}
                """;
        Servico domain = new Servico(null, "Barba", 20L, new BigDecimal("15.00"));
        Servico salvo = new Servico(1L, "Barba", 20L, new BigDecimal("15.00"));
        ServicoResponseDto resposta = mockResponse(1L, "Barba", new BigDecimal("15.00"));

        when(servicoMapper.toDomain(any(ServicoRequestDto.class))).thenReturn(domain);
        when(cadastrarServicoUseCase.execute(domain)).thenReturn(salvo);
        when(servicoMapper.toResponseDto(salvo)).thenReturn(resposta);

        mockMvc.perform(post("/servico")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Barba"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletar_comoAdmin_deveRetornar204() throws Exception {
        doNothing().when(removerServicoPorIdUseCase).execute(1L);

        mockMvc.perform(delete("/servico/1"))
                .andExpect(status().isNoContent());
    }
}
