package com.barbearia.fahcortes.domain.usecases.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.entities.agendamento.AgendamentoStatus;
import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoUseCasesTest {

    @Mock
    private AgendamentoGateway agendamentoGateway;

    private CriarAgendamentoUseCase criar;
    private BuscarAgendamentoPorIdUseCase buscarPorId;
    private ListarAgendamentosUseCase listar;
    private CancelarAgendamentoUseCase cancelar;

    private static final LocalDateTime DATA_HORA = LocalDateTime.of(2026, 6, 1, 10, 0);

    @BeforeEach
    void setUp() {
        criar = new CriarAgendamentoUseCase(agendamentoGateway);
        buscarPorId = new BuscarAgendamentoPorIdUseCase(agendamentoGateway);
        listar = new ListarAgendamentosUseCase(agendamentoGateway);
        cancelar = new CancelarAgendamentoUseCase(agendamentoGateway);
    }

    @Test
    void criar_deveDefinirStatusConfirmadoSeNulo() {
        Agendamento input = new Agendamento(null, 1L, 2L, 3L, 1L, DATA_HORA, null, null);
        Agendamento salvo = new Agendamento(1L, 1L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.CONFIRMADO, null);
        when(agendamentoGateway.criar(any())).thenReturn(salvo);

        Agendamento resultado = criar.execute(input);

        assertThat(input.getStatus()).isEqualTo(AgendamentoStatus.CONFIRMADO);
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(agendamentoGateway).criar(input);
    }

    @Test
    void criar_deveManterStatusSeJaDefinido() {
        Agendamento input = new Agendamento(null, 1L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.PENDENTE, null);
        Agendamento salvo = new Agendamento(1L, 1L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.PENDENTE, null);
        when(agendamentoGateway.criar(input)).thenReturn(salvo);

        Agendamento resultado = criar.execute(input);

        assertThat(resultado.getStatus()).isEqualTo(AgendamentoStatus.PENDENTE);
    }

    @Test
    void buscarPorId_deveRetornarAgendamento() {
        Agendamento ag = new Agendamento(1L, 1L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.CONFIRMADO, "Sem barba");
        when(agendamentoGateway.buscarPorId(1L)).thenReturn(ag);

        Agendamento resultado = buscarPorId.execute(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getObservacoes()).isEqualTo("Sem barba");
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarExcecao() {
        when(agendamentoGateway.buscarPorId(99L))
                .thenThrow(new IllegalArgumentException("Agendamento com id: 99 não encontrado"));

        assertThatThrownBy(() -> buscarPorId.execute(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não encontrado");
    }

    @Test
    void listar_comClienteId_deveFiltrarPorCliente() {
        List<Agendamento> lista = List.of(
                new Agendamento(1L, 5L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.CONFIRMADO, null)
        );
        when(agendamentoGateway.listarPorCliente(5L)).thenReturn(lista);

        List<Agendamento> resultado = listar.execute(5L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClienteId()).isEqualTo(5L);
        verify(agendamentoGateway).listarPorCliente(5L);
        verify(agendamentoGateway, never()).listarTodos();
    }

    @Test
    void listar_semClienteId_deveRetornarTodos() {
        List<Agendamento> lista = List.of(
                new Agendamento(1L, 1L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.CONFIRMADO, null),
                new Agendamento(2L, 2L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.CANCELADO, null)
        );
        when(agendamentoGateway.listarTodos()).thenReturn(lista);

        List<Agendamento> resultado = listar.execute(null);

        assertThat(resultado).hasSize(2);
        verify(agendamentoGateway).listarTodos();
        verify(agendamentoGateway, never()).listarPorCliente(any());
    }

    @Test
    void cancelar_deveRetornarAgendamentoCancelado() {
        Agendamento cancelado = new Agendamento(1L, 1L, 2L, 3L, 1L, DATA_HORA, AgendamentoStatus.CANCELADO, null);
        when(agendamentoGateway.cancelar(1L)).thenReturn(cancelado);

        Agendamento resultado = cancelar.execute(1L);

        assertThat(resultado.getStatus()).isEqualTo(AgendamentoStatus.CANCELADO);
        verify(agendamentoGateway).cancelar(1L);
    }
}
