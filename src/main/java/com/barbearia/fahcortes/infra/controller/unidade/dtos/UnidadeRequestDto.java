package com.barbearia.fahcortes.infra.controller.unidade.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeRequestDto {

    @NotBlank(message = "nome não pode ser vazio")
    private String nome;

    private String endereco;

    private String telefone;

    private Double latitude;

    private Double longitude;

    private String horarioAbertura;

    private String horarioFechamento;
}
