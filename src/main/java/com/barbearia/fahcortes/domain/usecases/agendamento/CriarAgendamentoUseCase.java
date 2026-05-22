package com.barbearia.fahcortes.domain.usecases.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.entities.agendamento.AgendamentoStatus;
import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;

public class CriarAgendamentoUseCase {

    private final AgendamentoGateway agendamentoGateway;

    public CriarAgendamentoUseCase(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    public Agendamento execute(Agendamento agendamento) {
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(AgendamentoStatus.CONFIRMADO);
        }
        return agendamentoGateway.criar(agendamento);
    }
}
