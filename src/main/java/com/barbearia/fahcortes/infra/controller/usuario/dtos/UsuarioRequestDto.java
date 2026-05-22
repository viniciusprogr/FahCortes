package com.barbearia.fahcortes.infra.controller.usuario.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDto {

    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "O email não pode ser vazio")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia")
    private String senha;

    private String novaSenha;
}
