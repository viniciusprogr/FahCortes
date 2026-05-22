package com.barbearia.fahcortes.domain.gateways.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;

import java.util.List;

public interface AgendamentoGateway {

    Agendamento criar(Agendamento agendamento);

    Agendamento buscarPorId(Long id);

    List<Agendamento> listarPorCliente(Long clienteId);

    List<Agendamento> listarTodos();

    Agendamento cancelar(Long id);
}
