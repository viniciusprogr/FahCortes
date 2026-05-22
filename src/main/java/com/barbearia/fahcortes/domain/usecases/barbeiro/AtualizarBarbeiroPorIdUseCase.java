package com.barbearia.fahcortes.domain.usecases.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;

public class AtualizarBarbeiroPorIdUseCase {

    private final BarbeiroGateway barbeiroGateway;

    public AtualizarBarbeiroPorIdUseCase(BarbeiroGateway barbeiroGateway) {
        this.barbeiroGateway = barbeiroGateway;
    }

    public Barbeiro execute(Barbeiro barbeiro, Long id) {
        return barbeiroGateway.atualizar(barbeiro, id);
    }
}