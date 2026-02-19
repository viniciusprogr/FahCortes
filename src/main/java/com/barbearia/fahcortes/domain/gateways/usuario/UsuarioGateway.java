package com.barbearia.fahcortes.domain.gateways.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;

import java.util.Optional;

public interface UsuarioGateway {

    Usuario cadastrarUsuario(Usuario usuario);

    void removerUsuario(Long id);

    UsuarioResponseDto buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);
}
