package com.barbearia.fahcortes.infra.entities;

import com.barbearia.fahcortes.domain.entities.agendamento.AgendamentoStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_agendamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private Long barbeiroId;

    private Long servicoId;

    private Long unidadeId;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private AgendamentoStatus status;

    private String observacoes;
}
