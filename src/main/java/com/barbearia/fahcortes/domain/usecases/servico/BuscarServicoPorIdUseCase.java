package com.barbearia.fahcortes.domain.usecases.servico;

import com.barbearia.fahcortes.domain.entities.servico.Servico;
import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;

public class BuscarServicoPorIdUseCase {

    private final ServicoGateway servicoGateway;

    public BuscarServicoPorIdUseCase(ServicoGateway servicoGateway) {
        this.servicoGateway = servicoGateway;
    }

    public Servico execute(Long id) {
        return servicoGateway.buscarServicoPorId(id);
    }

}
