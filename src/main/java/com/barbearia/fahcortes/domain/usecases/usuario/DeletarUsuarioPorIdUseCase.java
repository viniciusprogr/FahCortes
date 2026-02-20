package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

public class DeletarUsuarioPorIdUseCase {

    private final UsuarioGateway usuarioGateway;

    public DeletarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public void execute(Long id){
        usuarioGateway.removerUsuario(id);
    }

}
