package com.barbearia.fahcortes.domain.usecases.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.domain.gateways.plano.PlanoGateway;

public class BuscarPlanoPorIdUseCase {
    private final PlanoGateway planoGateway;
    public BuscarPlanoPorIdUseCase(PlanoGateway planoGateway) { this.planoGateway = planoGateway; }
    public Plano execute(Long id) { return planoGateway.buscarPorId(id); }
}
