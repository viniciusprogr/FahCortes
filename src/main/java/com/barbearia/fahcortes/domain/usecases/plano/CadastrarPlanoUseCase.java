package com.barbearia.fahcortes.domain.usecases.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.domain.gateways.plano.PlanoGateway;

public class CadastrarPlanoUseCase {
    private final PlanoGateway planoGateway;
    public CadastrarPlanoUseCase(PlanoGateway planoGateway) { this.planoGateway = planoGateway; }
    public Plano execute(Plano plano) { return planoGateway.cadastrar(plano); }
}
