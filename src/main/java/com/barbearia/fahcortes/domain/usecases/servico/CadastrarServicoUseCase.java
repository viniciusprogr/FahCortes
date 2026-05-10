package com.barbearia.fahcortes.domain.usecases.servico;

import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;

public class CadastrarServicoUseCase {

    private final ServicoGateway servicoGateway;


    public CadastrarServicoUseCase(ServicoGateway servicoGateway) {
        this.servicoGateway = servicoGateway;
    }

    public Servico execute(Servico servico) {
        return servicoGateway.cadastrarServico(servico);
    }
}
