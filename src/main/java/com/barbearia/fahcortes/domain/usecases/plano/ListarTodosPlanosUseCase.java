package com.barbearia.fahcortes.domain.usecases.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.domain.gateways.plano.PlanoGateway;

import java.util.List;

public class ListarTodosPlanosUseCase {
    private final PlanoGateway planoGateway;
    public ListarTodosPlanosUseCase(PlanoGateway planoGateway) { this.planoGateway = planoGateway; }
    public List<Plano> execute() { return planoGateway.listarTodos(); }
}
