package com.barbearia.fahcortes.infra.controller;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.entities.agendamento.AgendamentoStatus;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.usecases.agendamento.*;
import com.barbearia.fahcortes.infra.config.CorsConfig;
import com.barbearia.fahcortes.infra.config.SecurityConfig;
import com.barbearia.fahcortes.infra.controller.agendamento.AgendamentoController;
import com.barbearia.fahcortes.infra.controller.agendamento.dtos.AgendamentoRequestDto;
import com.barbearia.fahcortes.infra.controller.agendamento.dtos.AgendamentoResponseDto;
import com.barbearia.fahcortes.infra.mapper.agendamento.AgendamentoMapper;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendamentoController.class)
@Import({SecurityConfig.class, CorsConfig.class})
class AgendamentoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private AgendamentoMapper agendamentoMapper;
    @MockBean private CriarAgendamentoUseCase criarAgendamentoUseCase;
    @MockBean private BuscarAgendamentoPorIdUseCase buscarAgendamentoPorIdUseCase;
    @MockBean private ListarAgendamentosUseCase listarAgendamentosUseCase;
    @MockBean private CancelarAgendamentoUseCase cancelarAgendamentoUseCase;
    @MockBean private TokenService tokenService;
    @MockBean private UsuarioDetailService usuarioDetailService;
    @MockBean private UsuarioRepository usuarioRepository;

    private static final LocalDateTime DATA_HORA = LocalDateTime.of(2025, 6, 15, 10, 0);

    private Agendamento agendamento(Long id, AgendamentoStatus status) {
        return new Agendamento(id, 1L, 2L, 3L, 1L, DATA_HORA, status, null);
    }

    private AgendamentoResponseDto responseDto(Long id, AgendamentoStatus status) {
        AgendamentoResponseDto dto = new AgendamentoResponseDto();
        dto.setId(id);
        dto.setClienteId(1L);
        dto.setBarbeiroId(2L);
        dto.setServicoId(3L);
        dto.setUnidadeId(1L);
        dto.setDataHora(DATA_HORA);
        dto.setStatus(status);
        return dto;
    }

    @Test
    @WithMockUser
    void listar_deveRetornar200() throws Exception {
        List<Agendamento> lista = List.of(agendamento(1L, AgendamentoStatus.CONFIRMADO));
        List<AgendamentoResponseDto> resposta = List.of(responseDto(1L, AgendamentoStatus.CONFIRMADO));

        when(listarAgendamentosUseCase.execute(null)).thenReturn(lista);
        when(agendamentoMapper.toResponseList(lista)).thenReturn(resposta);

        mockMvc.perform(get("/agendamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CONFIRMADO"));
    }

    @Test
    @WithMockUser
    void buscarPorId_deveRetornar200() throws Exception {
        Agendamento ag = agendamento(1L, AgendamentoStatus.CONFIRMADO);
        AgendamentoResponseDto resposta = responseDto(1L, AgendamentoStatus.CONFIRMADO);

        when(buscarAgendamentoPorIdUseCase.execute(1L)).thenReturn(ag);
        when(agendamentoMapper.toResponse(ag)).thenReturn(resposta);

        mockMvc.perform(get("/agendamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(1));
    }

    @Test
    @WithMockUser
    void buscarPorId_quandoNaoExiste_deveRetornar404() throws Exception {
        when(buscarAgendamentoPorIdUseCase.execute(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Agendamento", 99L));

        mockMvc.perform(get("/agendamentos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void criar_deveRetornar201() throws Exception {
        String body = """
                {"clienteId":1,"barbeiroId":2,"servicoId":3,"unidadeId":1,"dataHora":"2025-06-15T10:00:00"}
                """;
        Agendamento domain = agendamento(null, AgendamentoStatus.PENDENTE);
        Agendamento salvo = agendamento(1L, AgendamentoStatus.CONFIRMADO);
        AgendamentoResponseDto resposta = responseDto(1L, AgendamentoStatus.CONFIRMADO);

        when(agendamentoMapper.toDomain(any(AgendamentoRequestDto.class))).thenReturn(domain);
        when(criarAgendamentoUseCase.execute(domain)).thenReturn(salvo);
        when(agendamentoMapper.toResponse(salvo)).thenReturn(resposta);

        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMADO"));
    }

    @Test
    @WithMockUser
    void cancelar_deveRetornar200() throws Exception {
        Agendamento cancelado = agendamento(1L, AgendamentoStatus.CANCELADO);
        AgendamentoResponseDto resposta = responseDto(1L, AgendamentoStatus.CANCELADO);

        when(cancelarAgendamentoUseCase.execute(1L)).thenReturn(cancelado);
        when(agendamentoMapper.toResponse(cancelado)).thenReturn(resposta);

        mockMvc.perform(patch("/agendamentos/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADO"));
    }

    @Test
    void listar_semAutenticacao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/agendamentos"))
                .andExpect(status().isForbidden());
    }
}
