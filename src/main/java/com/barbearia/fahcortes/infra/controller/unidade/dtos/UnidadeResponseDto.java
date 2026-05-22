package com.barbearia.fahcortes.infra.controller.unidade.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeResponseDto {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private Double latitude;
    private Double longitude;
    private String horarioAbertura;
    private String horarioFechamento;
}
