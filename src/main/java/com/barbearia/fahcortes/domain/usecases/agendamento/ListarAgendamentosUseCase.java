package com.barbearia.fahcortes.domain.usecases.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;

import java.util.List;

public class ListarAgendamentosUseCase {

    private final AgendamentoGateway agendamentoGateway;

    public ListarAgendamentosUseCase(AgendamentoGateway agendamentoGateway) {
        this.agendamentoGateway = agendamentoGateway;
    }

    public List<Agendamento> execute(Long clienteId) {
        if (clienteId != null) {
            return agendamentoGateway.listarPorCliente(clienteId);
        }
        return agendamentoGateway.listarTodos();
    }
}
