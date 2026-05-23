package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.usecases.unidade.*;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.unidade.UnidadeController;
import com.barbearia.fahcortes.infra.controller.unidade.dtos.UnidadeRequestDto;
import com.barbearia.fahcortes.infra.controller.unidade.dtos.UnidadeResponseDto;
import com.barbearia.fahcortes.infra.mapper.unidade.UnidadeMapper;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnidadeController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class UnidadeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UnidadeMapper unidadeMapper;
    @MockBean private CadastrarUnidadeUseCase cadastrarUnidadeUseCase;
    @MockBean private BuscarUnidadePorIdUseCase buscarUnidadePorIdUseCase;
    @MockBean private ListarTodasUnidadesUseCase listarTodasUnidadesUseCase;
    @MockBean private AtualizarUnidadePorIdUseCase atualizarUnidadePorIdUseCase;
    @MockBean private DeletarUnidadePorIdUseCase deletarUnidadePorIdUseCase;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioDetailService usuarioDetailService;
    @MockBean private UsuarioRepository usuarioRepository;

    private Unidade unidade(Long id) {
        return new Unidade(id, "Unidade Centro", "Rua A, 100", "11999999999",
                -23.5, -46.6, "08:00", "18:00");
    }

    private UnidadeResponseDto responseDto(Long id) {
        UnidadeResponseDto dto = new UnidadeResponseDto();
        dto.setId(id);
        dto.setNome("Unidade Centro");
        dto.setEndereco("Rua A, 100");
        dto.setTelefone("11999999999");
        dto.setLatitude(-23.5);
        dto.setLongitude(-46.6);
        dto.setHorarioAbertura("08:00");
        dto.setHorarioFechamento("18:00");
        return dto;
    }

    @Test
    void listarTodas_deveRetornar200() throws Exception {
        List<Unidade> unidades = List.of(unidade(1L));
        List<UnidadeResponseDto> resposta = List.of(responseDto(1L));

        when(listarTodasUnidadesUseCase.execute()).thenReturn(unidades);
        when(unidadeMapper.toResponseList(unidades)).thenReturn(resposta);

        mockMvc.perform(get("/unidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Unidade Centro"));
    }

    @Test
    void buscarPorId_deveRetornar200() throws Exception {
        Unidade u = unidade(1L);
        UnidadeResponseDto resposta = responseDto(1L);

        when(buscarUnidadePorIdUseCase.execute(1L)).thenReturn(u);
        when(unidadeMapper.toResponse(u)).thenReturn(resposta);

        mockMvc.perform(get("/unidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Unidade Centro"));
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveRetornar404() throws Exception {
        when(buscarUnidadePorIdUseCase.execute(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Unidade", 99L));

        mockMvc.perform(get("/unidades/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cadastrar_comoAdmin_deveRetornar201() throws Exception {
        String body = """
                {"nome":"Unidade Centro","endereco":"Rua A, 100","telefone":"11999999999","horarioAbertura":"08:00","horarioFechamento":"18:00"}
                """;
        Unidade domain = unidade(null);
        Unidade salva = unidade(1L);
        UnidadeResponseDto resposta = responseDto(1L);

        when(unidadeMapper.toDomain(any(UnidadeRequestDto.class))).thenReturn(domain);
        when(cadastrarUnidadeUseCase.execute(domain)).thenReturn(salva);
        when(unidadeMapper.toResponse(salva)).thenReturn(resposta);

        mockMvc.perform(post("/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Unidade Centro"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizar_comoAdmin_deveRetornar200() throws Exception {
        String body = """
                {"nome":"Unidade Norte","endereco":"Rua B, 200","telefone":"11988888888","horarioAbertura":"09:00","horarioFechamento":"17:00"}
                """;
        Unidade domain = unidade(null);
        Unidade atualizada = unidade(1L);
        UnidadeResponseDto resposta = responseDto(1L);

        when(unidadeMapper.toDomain(any(UnidadeRequestDto.class))).thenReturn(domain);
        when(atualizarUnidadePorIdUseCase.execute(domain, 1L)).thenReturn(atualizada);
        when(unidadeMapper.toResponse(atualizada)).thenReturn(resposta);

        mockMvc.perform(put("/unidades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Unidade Centro"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletar_comoAdmin_deveRetornar204() throws Exception {
        doNothing().when(deletarUnidadePorIdUseCase).execute(1L);

        mockMvc.perform(delete("/unidades/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void cadastrar_comoUser_deveRetornar403() throws Exception {
        mockMvc.perform(post("/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"X\"}"))
                .andExpect(status().isForbidden());
    }
}
