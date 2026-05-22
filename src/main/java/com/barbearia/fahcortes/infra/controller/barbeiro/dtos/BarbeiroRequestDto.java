package com.barbearia.fahcortes.infra.controller.barbeiro.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarbeiroRequestDto {

    @NotBlank(message = "nome não pode ser vazio")
    private String nome;

    private String especialidade;

    private String foto;

    private Integer curtidas;

    private Boolean ativo;
}