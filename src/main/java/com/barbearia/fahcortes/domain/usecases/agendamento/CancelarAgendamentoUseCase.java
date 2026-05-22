package com.barbearia.fahcortes.domain.usecases.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;

public class CancelarAgendamentoUseCase {

    private final AgendamentoGateway agendamentoGateway;

    public CancelarAgendamentoUseCase(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    public Agendamento execute(Long id) {
        return agendamentoGateway.cancelar(id);
    }
}
