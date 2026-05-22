package com.barbearia.fahcortes.infra.controller.barbeiro.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BarbeiroResponseDto {

    private Long id;
    private String nome;
    private String especialidade;
    private String foto;
    private Integer curtidas;
    private Boolean ativo;
}