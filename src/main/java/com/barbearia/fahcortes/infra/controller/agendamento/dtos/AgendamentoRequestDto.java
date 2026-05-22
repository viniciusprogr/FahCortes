package com.barbearia.fahcortes.infra.controller.agendamento.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentoRequestDto {

    @NotNull(message = "clienteId não pode ser nulo")
    private Long clienteId;

    @NotNull(message = "barbeiroId não pode ser nulo")
    private Long barbeiroId;

    @NotNull(message = "servicoId não pode ser nulo")
    private Long servicoId;

    private Long unidadeId;

    @NotNull(message = "dataHora não pode ser nula")
    private LocalDateTime dataHora;

    private String observacoes;
}
