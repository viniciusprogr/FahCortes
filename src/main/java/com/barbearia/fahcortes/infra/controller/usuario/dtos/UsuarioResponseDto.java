package com.barbearia.fahcortes.infra.controller.usuario.dtos;

import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {
    private Long id;
    private String nome;
    private String email;
    private UsuarioEnum role;
}
