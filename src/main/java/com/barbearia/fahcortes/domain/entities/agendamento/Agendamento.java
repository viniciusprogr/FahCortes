package com.barbearia.fahcortes.domain.entities.agendamento;

import java.time.LocalDateTime;

public class Agendamento {

    private Long id;
    private Long clienteId;
    private Long barbeiroId;
    private Long servicoId;
    private Long unidadeId;
    private LocalDateTime dataHora;
    private AgendamentoStatus status;
    private String observacoes;

    public Agendamento() {}

    public Agendamento(Long id, Long clienteId, Long barbeiroId, Long servicoId,
                       Long unidadeId, LocalDateTime dataHora, AgendamentoStatus status, String observacoes) {
        this.id = id;
        this.clienteId = clienteId;
        this.barbeiroId = barbeiroId;
        this.servicoId = servicoId;
        this.unidadeId = unidadeId;
        this.dataHora = dataHora;
        this.status = status;
        this.observacoes = observacoes;
    }

    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public Long getBarbeiroId() { return barbeiroId; }
    public Long getServicoId() { return servicoId; }
    public Long getUnidadeId() { return unidadeId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public AgendamentoStatus getStatus() { return status; }
    public String getObservacoes() { return observacoes; }

    public void setId(Long id) { this.id = id; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setBarbeiroId(Long barbeiroId) { this.barbeiroId = barbeiroId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public void setUnidadeId(Long unidadeId) { this.unidadeId = unidadeId; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setStatus(AgendamentoStatus status) { this.status = status; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
