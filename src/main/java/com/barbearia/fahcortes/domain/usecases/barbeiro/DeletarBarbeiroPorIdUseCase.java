package com.barbearia.fahcortes.domain.usecases.barbeiro;

import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;

public class DeletarBarbeiroPorIdUseCase {

    private final BarbeiroGateway barbeiroGateway;

    public DeletarBarbeiroPorIdUseCase(BarbeiroGateway barbeiroGateway) {
        this.barbeiroGateway = barbeiroGateway;
    }

    public void execute(Long id) {
        barbeiroGateway.deletar(id);
    }
}