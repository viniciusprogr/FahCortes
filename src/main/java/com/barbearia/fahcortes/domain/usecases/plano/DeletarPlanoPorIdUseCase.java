package com.barbearia.fahcortes.domain.usecases.plano;

import com.barbearia.fahcortes.domain.gateways.plano.PlanoGateway;

public class DeletarPlanoPorIdUseCase {
    private final PlanoGateway planoGateway;
    public DeletarPlanoPorIdUseCase(PlanoGateway planoGateway) { this.planoGateway = planoGateway; }
    public void execute(Long id) { planoGateway.deletar(id); }
}
