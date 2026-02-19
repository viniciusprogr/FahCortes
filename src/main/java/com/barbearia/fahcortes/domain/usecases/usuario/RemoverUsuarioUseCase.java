package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

public class RemoverUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public RemoverUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public void execute(Long id){
        usuarioGateway.removerUsuario(id);
    }
}
