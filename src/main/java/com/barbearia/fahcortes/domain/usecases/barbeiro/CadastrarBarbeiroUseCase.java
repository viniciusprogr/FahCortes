package com.barbearia.fahcortes.domain.usecases.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;

public class CadastrarBarbeiroUseCase {

    private final BarbeiroGateway barbeiroGateway;

    public CadastrarBarbeiroUseCase(BarbeiroGateway barbeiroGateway) {
        this.barbeiroGateway = barbeiroGateway;
    }

    public Barbeiro execute(Barbeiro barbeiro) {
        return barbeiroGateway.cadastrar(barbeiro);
    }
}