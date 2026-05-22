package com.barbearia.fahcortes.infra.controller.produto.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRequestDto {

    @NotBlank(message = "nome não pode ser vazio")
    private String nome;

    private String descricao;

    @NotNull(message = "preco não pode ser nulo")
    private BigDecimal preco;

    private String imagem;

    private Integer estoque;
}
