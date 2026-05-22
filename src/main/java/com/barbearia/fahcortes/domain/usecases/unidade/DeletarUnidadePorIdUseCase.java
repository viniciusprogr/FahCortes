package com.barbearia.fahcortes.domain.usecases.unidade;

import com.barbearia.fahcortes.domain.gateways.unidade.UnidadeGateway;

public class DeletarUnidadePorIdUseCase {
    private final UnidadeGateway unidadeGateway;
    public DeletarUnidadePorIdUseCase(UnidadeGateway unidadeGateway) { this.unidadeGateway = unidadeGateway; }
    public void execute(Long id) { unidadeGateway.deletar(id); }
}
