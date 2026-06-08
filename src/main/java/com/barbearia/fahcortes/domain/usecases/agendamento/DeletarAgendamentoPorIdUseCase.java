package com.barbearia.fahcortes.domain.usecases.agendamento;

import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;

public class DeletarAgendamentoPorIdUseCase {

    private final AgendamentoGateway agendamentoGateway;

    public DeletarAgendamentoPorIdUseCase(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    public void execute(Long id) {
        agendamentoGateway.deletar(id);
    }
}
