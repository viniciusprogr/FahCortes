package com.barbearia.fahcortes.infra.controller.plano.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanoResponseDto {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private List<String> beneficios;
    private Integer validade;
}
