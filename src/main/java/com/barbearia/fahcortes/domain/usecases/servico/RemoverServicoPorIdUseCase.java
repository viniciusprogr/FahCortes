package com.barbearia.fahcortes.domain.usecases.servico;

import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;

public class RemoverServicoPorIdUseCase {
    private final ServicoGateway servicoGateway;


    public RemoverServicoPorIdUseCase(ServicoGateway servicoGateway) {
        this.servicoGateway = servicoGateway;
    }

    public void execute(Long id){
        servicoGateway.removerServicoPorId(id);
    }
}
