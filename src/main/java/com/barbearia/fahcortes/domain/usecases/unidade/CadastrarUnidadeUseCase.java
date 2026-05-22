package com.barbearia.fahcortes.domain.usecases.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.domain.gateways.unidade.UnidadeGateway;

public class CadastrarUnidadeUseCase {
    private final UnidadeGateway unidadeGateway;
    public CadastrarUnidadeUseCase(UnidadeGateway unidadeGateway) { this.unidadeGateway = unidadeGateway; }
    public Unidade execute(Unidade unidade) { return unidadeGateway.cadastrar(unidade); }
}
