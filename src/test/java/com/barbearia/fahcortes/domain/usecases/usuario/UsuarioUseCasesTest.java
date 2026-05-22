package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCasesTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private CadastrarUsuarioUseCase cadastrar;
    private BuscarUsuarioPorIdUseCase buscarPorId;
    private BuscarUsuarioPorEmailUseCase buscarPorEmail;
    private ListarTodosUsuariosUseCase listarTodos;
    private DeletarUsuarioPorIdUseCase deletar;
    private AtualizarUsuarioPorIdUseCase atualizar;

    private static final Usuario USUARIO_MOCK =
            new Usuario(1L, "Maria", "maria@email.com", "hash123", UsuarioEnum.USER);

    @BeforeEach
    void setUp() {
        cadastrar = new CadastrarUsuarioUseCase(usuarioGateway);
        buscarPorId = new BuscarUsuarioPorIdUseCase(usuarioGateway);
        buscarPorEmail = new BuscarUsuarioPorEmailUseCase(usuarioGateway);
        listarTodos = new ListarTodosUsuariosUseCase(usuarioGateway);
        deletar = new DeletarUsuarioPorIdUseCase(usuarioGateway);
        atualizar = new AtualizarUsuarioPorIdUseCase(usuarioGateway);
    }

    @Test
    void cadastrar_deveAtribuirRoleUserESalvar() {
        Usuario input = new Usuario(null, "Maria", "maria@email.com", "senha123", null);
        when(usuarioGateway.cadastrarUsuario(any(Usuario.class))).thenReturn(USUARIO_MOCK);

        Usuario resultado = cadastrar.execute(input);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEmail()).isEqualTo("maria@email.com");
        verify(usuarioGateway).cadastrarUsuario(argThat(u ->
                u.getRole() == UsuarioEnum.USER && u.getNome().equals("Maria")));
    }

    @Test
    void buscarPorId_deveRetornarUsuario() {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(USUARIO_MOCK);

        Usuario resultado = buscarPorId.execute(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Maria");
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarExcecao() {
        when(usuarioGateway.buscarPorId(999L))
                .thenThrow(new EntidadeNaoEncontradaException("Usuário", 999L));

        assertThatThrownBy(() -> buscarPorId.execute(999L))
                .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    void buscarPorEmail_deveRetornarUsuario() {
        when(usuarioGateway.buscarPorEmail("maria@email.com")).thenReturn(USUARIO_MOCK);

        Usuario resultado = buscarPorEmail.execute("maria@email.com");

        assertThat(resultado.getEmail()).isEqualTo("maria@email.com");
    }

    @Test
    void listarTodos_deveRetornarListaDeUsuarios() {
        List<Usuario> lista = List.of(
                USUARIO_MOCK,
                new Usuario(2L, "Pedro", "pedro@email.com", "hash", UsuarioEnum.ADMIN)
        );
        when(usuarioGateway.listarTodos()).thenReturn(lista);

        List<Usuario> resultado = listarTodos.execute();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void deletar_deveChamarGateway() {
        doNothing().when(usuarioGateway).removerUsuario(1L);

        deletar.execute(1L);

        verify(usuarioGateway).removerUsuario(1L);
    }

    @Test
    void atualizar_deveRetornarUsuarioAtualizado() {
        Usuario input = new Usuario(null, "Maria Nova", "maria@email.com", "hash", UsuarioEnum.USER);
        Usuario atualizado = new Usuario(1L, "Maria Nova", "maria@email.com", "hash", UsuarioEnum.USER);
        when(usuarioGateway.atualizarUsuario(input, 1L)).thenReturn(atualizado);

        Usuario resultado = atualizar.execute(input, 1L);

        assertThat(resultado.getNome()).isEqualTo("Maria Nova");
    }
}
