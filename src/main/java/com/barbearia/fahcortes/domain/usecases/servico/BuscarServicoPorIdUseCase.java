package com.barbearia.fahcortes.domain.usecases.servico;

import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;

public class BuscarServicoPorIdUseCase {

    private final ServicoGateway servicoGateway;

    public BuscarServicoPorIdUseCase(ServicoGateway servicoGateway) {
        this.servicoGateway = servicoGateway;
    }

    public Servico execute (Long Id){
        return servicoGateway.BuscarServicoPorId(Id);
    }

}
