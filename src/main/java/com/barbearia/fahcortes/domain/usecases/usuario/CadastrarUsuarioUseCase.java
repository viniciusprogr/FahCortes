package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

public class CadastrarUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    public CadastrarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(Usuario usuario) {

        usuarioGateway.buscarPorEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("E-mail já cadastrado!");
                });
        return usuarioGateway.cadastrarUsuario(usuario);
    }
}
