package com.barbearia.fahcortes.domain.usecases.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.domain.gateways.unidade.UnidadeGateway;

public class BuscarUnidadePorIdUseCase {
    private final UnidadeGateway unidadeGateway;
    public BuscarUnidadePorIdUseCase(UnidadeGateway unidadeGateway) { this.unidadeGateway = unidadeGateway; }
    public Unidade execute(Long id) { return unidadeGateway.buscarPorId(id); }
}
