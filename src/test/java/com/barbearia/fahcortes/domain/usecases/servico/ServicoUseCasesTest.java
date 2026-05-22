package com.barbearia.fahcortes.domain.usecases.servico;

import com.barbearia.fahcortes.domain.entities.servico.Servico;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoUseCasesTest {

    @Mock
    private ServicoGateway servicoGateway;

    private CadastrarServicoUseCase cadastrar;
    private BuscarServicoPorIdUseCase buscarPorId;
    private ListarTodosServicosUseCase listarTodos;
    private RemoverServicoPorIdUseCase remover;

    private static final Servico SERVICO_MOCK =
            new Servico(1L, "Corte simples", 30L, new BigDecimal("25.00"));

    @BeforeEach
    void setUp() {
        cadastrar = new CadastrarServicoUseCase(servicoGateway);
        buscarPorId = new BuscarServicoPorIdUseCase(servicoGateway);
        listarTodos = new ListarTodosServicosUseCase(servicoGateway);
        remover = new RemoverServicoPorIdUseCase(servicoGateway);
    }

    @Test
    void cadastrar_deveRetornarServicoCadastrado() {
        Servico input = new Servico(null, "Corte simples", 30L, new BigDecimal("25.00"));
        when(servicoGateway.cadastrarServico(input)).thenReturn(SERVICO_MOCK);

        Servico resultado = cadastrar.execute(input);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getDescricao()).isEqualTo("Corte simples");
        verify(servicoGateway).cadastrarServico(input);
    }

    @Test
    void buscarPorId_deveRetornarServico() {
        when(servicoGateway.buscarServicoPorId(1L)).thenReturn(SERVICO_MOCK);

        Servico resultado = buscarPorId.execute(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getValor()).isEqualByComparingTo("25.00");
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarExcecao() {
        when(servicoGateway.buscarServicoPorId(99L))
                .thenThrow(new EntidadeNaoEncontradaException("Serviço", 99L));

        assertThatThrownBy(() -> buscarPorId.execute(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("não encontrado");
    }

    @Test
    void listarTodos_deveRetornarListaCompleta() {
        List<Servico> lista = List.of(
                SERVICO_MOCK,
                new Servico(2L, "Barba", 20L, new BigDecimal("15.00"))
        );
        when(servicoGateway.listarTodosServicos()).thenReturn(lista);

        List<Servico> resultado = listarTodos.execute();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(1).getDescricao()).isEqualTo("Barba");
    }

    @Test
    void remover_deveChamarGateway() {
        doNothing().when(servicoGateway).removerServicoPorId(1L);

        remover.execute(1L);

        verify(servicoGateway).removerServicoPorId(1L);
    }

    @Test
    void remover_quandoNaoExiste_deveLancarExcecao() {
        doThrow(new EntidadeNaoEncontradaException("Serviço", 99L))
                .when(servicoGateway).removerServicoPorId(99L);

        assertThatThrownBy(() -> remover.execute(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("não encontrado");
    }
}
