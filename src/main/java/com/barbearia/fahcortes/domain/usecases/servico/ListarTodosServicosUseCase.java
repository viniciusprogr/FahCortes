package com.barbearia.fahcortes.domain.usecases.servico;

import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;

import java.util.List;

public class ListarTodosServicosUseCase {

    private final ServicoGateway servicoGateway;

    public ListarTodosServicosUseCase(ServicoGateway servicoGateway) {
        this.servicoGateway = servicoGateway;
    }

    public List<Servico> execute(){
        return servicoGateway.listarTodosServicos();
    }
}
