package com.barbearia.fahcortes.domain.usecases.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;

public class BuscarAgendamentoPorIdUseCase {

    private final AgendamentoGateway agendamentoGateway;

    public BuscarAgendamentoPorIdUseCase(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    public Agendamento execute(Long id) {
        return agendamentoGateway.buscarPorId(id);
    }
}
