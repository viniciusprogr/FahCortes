package com.barbearia.fahcortes.domain.entities.plano;

import java.math.BigDecimal;
import java.util.List;

public class Plano {

    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private List<String> beneficios;
    private Integer validade;

    public Plano() {}

    public Plano(Long id, String nome, String descricao, BigDecimal preco, List<String> beneficios, Integer validade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.beneficios = beneficios;
        this.validade = validade;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public List<String> getBeneficios() { return beneficios; }
    public Integer getValidade() { return validade; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setBeneficios(List<String> beneficios) { this.beneficios = beneficios; }
    public void setValidade(Integer validade) { this.validade = validade; }
}
