package com.barbearia.fahcortes.domain.usecases.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;

public class AtualizarProdutoPorIdUseCase {
    private final ProdutoGateway produtoGateway;
    public AtualizarProdutoPorIdUseCase(ProdutoGateway produtoGateway) { this.produtoGateway = produtoGateway; }
    public Produto execute(Produto produto, Long id) { return produtoGateway.atualizar(produto, id); }
}
