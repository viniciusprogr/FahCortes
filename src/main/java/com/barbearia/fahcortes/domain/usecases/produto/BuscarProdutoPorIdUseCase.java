package com.barbearia.fahcortes.domain.usecases.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;

public class BuscarProdutoPorIdUseCase {
    private final ProdutoGateway produtoGateway;
    public BuscarProdutoPorIdUseCase(ProdutoGateway produtoGateway) { this.produtoGateway = produtoGateway; }
    public Produto execute(Long id) { return produtoGateway.buscarPorId(id); }
}
