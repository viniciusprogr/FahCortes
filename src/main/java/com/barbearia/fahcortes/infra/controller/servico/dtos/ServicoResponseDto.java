package com.barbearia.fahcortes.infra.controller.servico.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoResponseDto {

    private String descricao;

    private Long tempo;

    private BigDecimal valor;
}
