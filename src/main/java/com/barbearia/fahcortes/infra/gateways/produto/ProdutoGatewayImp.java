package com.barbearia.fahcortes.infra.gateways.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;
import com.barbearia.fahcortes.infra.controller.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.infra.entities.ProdutoEntity;
import com.barbearia.fahcortes.infra.mapper.produto.ProdutoMapper;
import com.barbearia.fahcortes.infra.persistence.ProdutoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoGatewayImp implements ProdutoGateway {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoGatewayImp(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    @Override
    public Produto cadastrar(Produto produto) {
        ProdutoEntity entity = produtoMapper.toEntity(produto);
        return produtoMapper.toDomain(produtoRepository.save(entity));
    }

    @Override
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(produtoMapper::toDomain)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Produto com id " + id + " não encontrado. Verifique se o id informado está correto."));
    }

    @Override
    public List<Produto> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Produto atualizar(Produto produto, Long id) {
        ProdutoEntity entity = produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Não foi possível atualizar. Produto com id " + id + " não encontrado."));

        entity.setNome(produto.getNome());
        entity.setDescricao(produto.getDescricao());
        entity.setPreco(produto.getPreco());
        entity.setImagem(produto.getImagem());
        if (produto.getEstoque() != null) entity.setEstoque(produto.getEstoque());

        return produtoMapper.toDomain(produtoRepository.save(entity));
    }

    @Override
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException(
                    "Não foi possível remover o produto com id " + id + ". Nenhum produto encontrado com esse id.");
        }
        produtoRepository.deleteById(id);
    }
}
