package com.barbearia.fahcortes.domain.usecases.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.domain.gateways.unidade.UnidadeGateway;

public class AtualizarUnidadePorIdUseCase {
    private final UnidadeGateway unidadeGateway;
    public AtualizarUnidadePorIdUseCase(UnidadeGateway unidadeGateway) { this.unidadeGateway = unidadeGateway; }
    public Unidade execute(Unidade unidade, Long id) { return unidadeGateway.atualizar(unidade, id); }
}
