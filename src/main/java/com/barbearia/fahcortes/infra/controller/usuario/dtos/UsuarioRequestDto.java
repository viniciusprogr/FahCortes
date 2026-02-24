package com.barbearia.fahcortes.infra.controller.usuario.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDto {

    @NotNull(message = "nome não pode ser nulo")
    private String nome;

    @NotNull(message = "email não pode ser nulo")
    private String email;

    @NotNull(message = "senha não pode ser nulo")
    private String senha;
}
