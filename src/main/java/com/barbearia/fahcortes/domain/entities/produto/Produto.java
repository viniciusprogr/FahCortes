package com.barbearia.fahcortes.domain.entities.produto;

import java.math.BigDecimal;

public class Produto {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String imagem;
    private Integer estoque;

    public Produto() {}

    public Produto(Long id, String nome, String descricao, BigDecimal preco, String imagem, Integer estoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
        this.estoque = estoque;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public String getImagem() { return imagem; }
    public Integer getEstoque() { return estoque; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setImagem(String imagem) { this.imagem = imagem; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
}
