package com.barbearia.fahcortes.domain.usecases.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;

public class BuscarBarbeiroPorIdUseCase {

    private final BarbeiroGateway barbeiroGateway;

    public BuscarBarbeiroPorIdUseCase(BarbeiroGateway barbeiroGateway) {
        this.barbeiroGateway = barbeiroGateway;
    }

    public Barbeiro execute(Long id) {
        return barbeiroGateway.buscarPorId(id);
    }
}