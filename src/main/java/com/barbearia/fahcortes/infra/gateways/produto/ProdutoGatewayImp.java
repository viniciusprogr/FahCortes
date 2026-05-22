package com.barbearia.fahcortes.infra.gateways.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.domain.gateways.produto.ProdutoGateway;
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
                .orElseThrow(() -> new IllegalArgumentException("Produto com id: " + id + " não encontrado"));
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
                .orElseThrow(() -> new IllegalArgumentException("Produto com id: " + id + " não encontrado"));

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
            throw new IllegalArgumentException("Produto com id: " + id + " não encontrado");
        }
        produtoRepository.deleteById(id);
    }
}
