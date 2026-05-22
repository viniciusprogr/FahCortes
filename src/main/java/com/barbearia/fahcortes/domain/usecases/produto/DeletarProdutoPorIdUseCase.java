package com.barbearia.fahcortes.domain.usecases.produto;

import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;

public class DeletarProdutoPorIdUseCase {
    private final ProdutoGateway produtoGateway;
    public DeletarProdutoPorIdUseCase(ProdutoGateway produtoGateway) { this.produtoGateway = produtoGateway; }
    public void execute(Long id) { produtoGateway.deletar(id); }
}
