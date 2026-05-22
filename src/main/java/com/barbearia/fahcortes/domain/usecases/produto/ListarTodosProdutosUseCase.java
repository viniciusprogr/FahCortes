package com.barbearia.fahcortes.domain.usecases.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;

import java.util.List;

public class ListarTodosProdutosUseCase {
    private final ProdutoGateway produtoGateway;
    public ListarTodosProdutosUseCase(ProdutoGateway produtoGateway) { this.produtoGateway = produtoGateway; }
    public List<Produto> execute() { return produtoGateway.listarTodos(); }
}
