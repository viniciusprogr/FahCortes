package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

import java.util.Optional;

public class BuscarUsuarioPorIdUseCase {

    private final UsuarioGateway usuarioGateway;

    public BuscarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(Long id){
        return usuarioGateway.buscarPorId(id);
    }
}
