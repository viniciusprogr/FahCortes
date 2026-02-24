package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

import java.util.Optional;

public class BuscarUsuarioPorEmailUseCase {

    private final UsuarioGateway usuarioGateway;

    public BuscarUsuarioPorEmailUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario execute(String email){
        return  usuarioGateway.buscarPorEmail(email);
    }
}
