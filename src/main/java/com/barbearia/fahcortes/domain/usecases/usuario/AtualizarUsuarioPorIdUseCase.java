package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

public class AtualizarUsuarioPorIdUseCase {

    private final UsuarioGateway usuarioGateway;

    public AtualizarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(Usuario usuario, Long id){
        usuarioGateway.buscarPorId(id);
        return usuarioGateway.atualizarUsuario(usuario, id);
    }
}
