package com.barbearia.fahcortes.domain.gateways.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;

import java.util.List;

public interface UsuarioGateway {

    Usuario cadastrarUsuario(Usuario usuario);

    void removerUsuario(Long id);

    Usuario buscarPorId(Long id);

    Usuario buscarPorEmail(String email);

    List<Usuario> ListarTodos();

    Usuario atualizarUsuario(Usuario usuario, Long id);


}
