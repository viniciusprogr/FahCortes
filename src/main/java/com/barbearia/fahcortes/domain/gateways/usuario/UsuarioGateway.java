package com.barbearia.fahcortes.domain.gateways.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.usecases.usuario.CadastrarUsuarioUseCase;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;

import java.util.List;
import java.util.Optional;

public interface UsuarioGateway {

    Usuario cadastrarUsuario(Usuario usuario);

    void removerUsuario(Long id);

    Usuario buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> ListarTodos();
}
