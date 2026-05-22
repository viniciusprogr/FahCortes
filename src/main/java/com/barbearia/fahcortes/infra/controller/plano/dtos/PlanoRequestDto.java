package com.barbearia.fahcortes.infra.controller.plano.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanoRequestDto {

    @NotBlank(message = "nome não pode ser vazio")
    private String nome;

    private String descricao;

    @NotNull(message = "preco não pode ser nulo")
    private BigDecimal preco;

    private List<String> beneficios;

    private Integer validade;
}
