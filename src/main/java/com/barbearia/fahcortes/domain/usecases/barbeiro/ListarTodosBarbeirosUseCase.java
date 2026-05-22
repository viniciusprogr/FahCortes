package com.barbearia.fahcortes.domain.usecases.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;

import java.util.List;

public class ListarTodosBarbeirosUseCase {

    private final BarbeiroGateway barbeiroGateway;

    public ListarTodosBarbeirosUseCase(BarbeiroGateway barbeiroGateway) {
        this.barbeiroGateway = barbeiroGateway;
    }

    public List<Barbeiro> execute() {
        return barbeiroGateway.listarTodos();
    }
}