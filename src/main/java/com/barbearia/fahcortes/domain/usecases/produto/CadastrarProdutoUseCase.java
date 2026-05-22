package com.barbearia.fahcortes.domain.usecases.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;

public class CadastrarProdutoUseCase {
    private final ProdutoGateway produtoGateway;
    public CadastrarProdutoUseCase(ProdutoGateway produtoGateway) { this.produtoGateway = produtoGateway; }
    public Produto execute(Produto produto) { return produtoGateway.cadastrar(produto); }
}
