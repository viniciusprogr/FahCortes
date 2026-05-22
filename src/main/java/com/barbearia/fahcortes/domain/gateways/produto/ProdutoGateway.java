package com.barbearia.fahcortes.domain.gateways.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;

import java.util.List;

public interface ProdutoGateway {
    Produto cadastrar(Produto produto);
    Produto buscarPorId(Long id);
    List<Produto> listarTodos();
    Produto atualizar(Produto produto, Long id);
    void deletar(Long id);
}
