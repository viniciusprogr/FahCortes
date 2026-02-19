package com.barbearia.fahcortes.domain.usecases.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

import java.util.List;

public class ListarTodosUsuariosUseCase {
    private final UsuarioGateway usuarioGateway;


    public ListarTodosUsuariosUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> execute(){
       return usuarioGateway.ListarTodos();
    }

}
