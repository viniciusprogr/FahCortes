package com.barbearia.fahcortes.infra.controller.usuario.dtos;

import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;

public record LoginResponseDto(String token, UsuarioEnum role) {
}
