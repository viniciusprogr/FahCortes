package com.barbearia.fahcortes.domain.gateways.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioGateway {

    Usuario cadastrarUsuario(Usuario usuario);

    void removerUsuario(Long id);

    Usuario buscarPorId(Long id);

    Usuario buscarPorEmail(String email);

    List<Usuario> ListarTodos();

    Usuario atualizarUsuario(Usuario usuario, Long id);


}
