package com.barbearia.fahcortes.infra.controller.agendamento.dtos;

import com.barbearia.fahcortes.domain.entities.agendamento.AgendamentoStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentoResponseDto {

    private Long id;
    private Long clienteId;
    private Long barbeiroId;
    private Long servicoId;
    private Long unidadeId;
    private LocalDateTime dataHora;
    private AgendamentoStatus status;
    private String observacoes;
}
