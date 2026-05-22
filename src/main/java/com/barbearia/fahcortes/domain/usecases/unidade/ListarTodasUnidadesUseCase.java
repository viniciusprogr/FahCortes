package com.barbearia.fahcortes.domain.usecases.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.domain.gateways.unidade.UnidadeGateway;

import java.util.List;

public class ListarTodasUnidadesUseCase {
    private final UnidadeGateway unidadeGateway;
    public ListarTodasUnidadesUseCase(UnidadeGateway unidadeGateway) { this.unidadeGateway = unidadeGateway; }
    public List<Unidade> execute() { return unidadeGateway.listarTodas(); }
}
