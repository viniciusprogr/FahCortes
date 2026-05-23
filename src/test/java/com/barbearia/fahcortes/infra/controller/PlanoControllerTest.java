package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.usecases.plano.*;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.plano.PlanoController;
import com.barbearia.fahcortes.infra.controller.plano.dtos.PlanoRequestDto;
import com.barbearia.fahcortes.infra.controller.plano.dtos.PlanoResponseDto;
import com.barbearia.fahcortes.infra.mapper.plano.PlanoMapper;
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

@WebMvcTest(PlanoController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class PlanoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private PlanoMapper planoMapper;
    @MockBean private CadastrarPlanoUseCase cadastrarPlanoUseCase;
    @MockBean private BuscarPlanoPorIdUseCase buscarPlanoPorIdUseCase;
    @MockBean private ListarTodosPlanosUseCase listarTodosPlanosUseCase;
    @MockBean private AtualizarPlanoPorIdUseCase atualizarPlanoPorIdUseCase;
    @MockBean private DeletarPlanoPorIdUseCase deletarPlanoPorIdUseCase;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioDetailService usuarioDetailService;
    @MockBean private UsuarioRepository usuarioRepository;

    private Plano plano(Long id) {
        return new Plano(id, "Plano Básico", "Cortes ilimitados", new BigDecimal("99.90"),
                List.of("Corte", "Barba"), 30);
    }

    private PlanoResponseDto responseDto(Long id) {
        PlanoResponseDto dto = new PlanoResponseDto();
        dto.setId(id);
        dto.setNome("Plano Básico");
        dto.setDescricao("Cortes ilimitados");
        dto.setPreco(new BigDecimal("99.90"));
        dto.setBeneficios(List.of("Corte", "Barba"));
        dto.setValidade(30);
        return dto;
    }

    @Test
    void listarTodos_deveRetornar200() throws Exception {
        List<Plano> planos = List.of(plano(1L));
        List<PlanoResponseDto> resposta = List.of(responseDto(1L));

        when(listarTodosPlanosUseCase.execute()).thenReturn(planos);
        when(planoMapper.toResponseList(planos)).thenReturn(resposta);

        mockMvc.perform(get("/planos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Plano Básico"));
    }

    @Test
    void buscarPorId_deveRetornar200() throws Exception {
        Plano p = plano(1L);
        PlanoResponseDto resposta = responseDto(1L);

        when(buscarPlanoPorIdUseCase.execute(1L)).thenReturn(p);
        when(planoMapper.toResponse(p)).thenReturn(resposta);

        mockMvc.perform(get("/planos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Plano Básico"));
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveRetornar404() throws Exception {
        when(buscarPlanoPorIdUseCase.execute(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Plano", 99L));

        mockMvc.perform(get("/planos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrar_comoAdmin_deveRetornar201() throws Exception {
        String body = """
                {"nome":"Plano Básico","descricao":"Cortes ilimitados","preco":99.90,"beneficios":["Corte","Barba"],"validade":30}
                """;
        Plano domain = plano(null);
        Plano salvo = plano(1L);
        PlanoResponseDto resposta = responseDto(1L);

        when(planoMapper.toDomain(any(PlanoRequestDto.class))).thenReturn(domain);
        when(cadastrarPlanoUseCase.execute(domain)).thenReturn(salvo);
        when(planoMapper.toResponse(salvo)).thenReturn(resposta);

        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Plano Básico"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizar_comoAdmin_deveRetornar200() throws Exception {
        String body = """
                {"nome":"Plano Premium","descricao":"Tudo incluso","preco":199.90,"beneficios":["Corte","Barba","Hidratação"],"validade":30}
                """;
        Plano domain = plano(null);
        Plano atualizado = plano(1L);
        PlanoResponseDto resposta = responseDto(1L);

        when(planoMapper.toDomain(any(PlanoRequestDto.class))).thenReturn(domain);
        when(atualizarPlanoPorIdUseCase.execute(domain, 1L)).thenReturn(atualizado);
        when(planoMapper.toResponse(atualizado)).thenReturn(resposta);

        mockMvc.perform(put("/planos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Plano Básico"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletar_comoAdmin_deveRetornar204() throws Exception {
        doNothing().when(deletarPlanoPorIdUseCase).execute(1L);

        mockMvc.perform(delete("/planos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void cadastrar_comoUser_deveRetornar403() throws Exception {
        mockMvc.perform(post("/planos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Plano X\",\"preco\":10.00}"))
                .andExpect(status().isForbidden());
    }
}
