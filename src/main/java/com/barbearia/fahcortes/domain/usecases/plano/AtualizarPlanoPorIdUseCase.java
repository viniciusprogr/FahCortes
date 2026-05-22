package com.barbearia.fahcortes.domain.usecases.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.domain.gateways.plano.PlanoGateway;

public class AtualizarPlanoPorIdUseCase {
    private final PlanoGateway planoGateway;
    public AtualizarPlanoPorIdUseCase(PlanoGateway planoGateway) { this.planoGateway = planoGateway; }
    public Plano execute(Plano plano, Long id) { return planoGateway.atualizar(plano, id); }
}
