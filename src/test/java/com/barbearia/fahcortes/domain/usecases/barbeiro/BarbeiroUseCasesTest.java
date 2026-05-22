package com.barbearia.fahcortes.domain.usecases.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarbeiroUseCasesTest {

    @Mock
    private BarbeiroGateway barbeiroGateway;

    private CadastrarBarbeiroUseCase cadastrar;
    private BuscarBarbeiroPorIdUseCase buscarPorId;
    private ListarTodosBarbeirosUseCase listarTodos;
    private AtualizarBarbeiroPorIdUseCase atualizar;
    private DeletarBarbeiroPorIdUseCase deletar;

    @BeforeEach
    void setUp() {
        cadastrar = new CadastrarBarbeiroUseCase(barbeiroGateway);
        buscarPorId = new BuscarBarbeiroPorIdUseCase(barbeiroGateway);
        listarTodos = new ListarTodosBarbeirosUseCase(barbeiroGateway);
        atualizar = new AtualizarBarbeiroPorIdUseCase(barbeiroGateway);
        deletar = new DeletarBarbeiroPorIdUseCase(barbeiroGateway);
    }

    @Test
    void cadastrar_deveRetornarBarbeiroSalvo() {
        Barbeiro input = new Barbeiro(null, "João", "Corte clássico", null, 0, true);
        Barbeiro salvo = new Barbeiro(1L, "João", "Corte clássico", null, 0, true);
        when(barbeiroGateway.cadastrar(input)).thenReturn(salvo);

        Barbeiro resultado = cadastrar.execute(input);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("João");
        verify(barbeiroGateway).cadastrar(input);
    }

    @Test
    void buscarPorId_deveRetornarBarbeiro() {
        Barbeiro barbeiro = new Barbeiro(1L, "João", "Fade", null, 5, true);
        when(barbeiroGateway.buscarPorId(1L)).thenReturn(barbeiro);

        Barbeiro resultado = buscarPorId.execute(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("João");
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarExcecao() {
        when(barbeiroGateway.buscarPorId(99L))
                .thenThrow(new IllegalArgumentException("Barbeiro com id: 99 não encontrado"));

        assertThatThrownBy(() -> buscarPorId.execute(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não encontrado");
    }

    @Test
    void listarTodos_deveRetornarLista() {
        List<Barbeiro> lista = List.of(
                new Barbeiro(1L, "João", "Fade", null, 5, true),
                new Barbeiro(2L, "Pedro", "Navalhado", null, 3, true)
        );
        when(barbeiroGateway.listarTodos()).thenReturn(lista);

        List<Barbeiro> resultado = listarTodos.execute();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("João");
    }

    @Test
    void listarTodos_quandoVazio_deveRetornarListaVazia() {
        when(barbeiroGateway.listarTodos()).thenReturn(List.of());

        List<Barbeiro> resultado = listarTodos.execute();

        assertThat(resultado).isEmpty();
    }

    @Test
    void atualizar_deveRetornarBarbeiroAtualizado() {
        Barbeiro input = new Barbeiro(null, "João Silva", "Fade", null, 10, true);
        Barbeiro atualizado = new Barbeiro(1L, "João Silva", "Fade", null, 10, true);
        when(barbeiroGateway.atualizar(input, 1L)).thenReturn(atualizado);

        Barbeiro resultado = atualizar.execute(input, 1L);

        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getCurtidas()).isEqualTo(10);
        verify(barbeiroGateway).atualizar(input, 1L);
    }

    @Test
    void deletar_deveChamarGateway() {
        doNothing().when(barbeiroGateway).deletar(1L);

        deletar.execute(1L);

        verify(barbeiroGateway).deletar(1L);
    }

    @Test
    void deletar_quandoNaoExiste_deveLancarExcecao() {
        doThrow(new IllegalArgumentException("Barbeiro com id: 99 não encontrado"))
                .when(barbeiroGateway).deletar(99L);

        assertThatThrownBy(() -> deletar.execute(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não encontrado");
    }
}
