package com.barbearia.fahcortes.domain.entities.servico;

import java.math.BigDecimal;

public class Servico {

    private Long id;
    private String descricao;
    private Long tempo;
    private BigDecimal valor;

    public Servico() {}

    public Servico(Long id, String descricao, Long tempo, BigDecimal valor) {
        this.id = id;
        this.descricao = descricao;
        this.tempo = tempo;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public Long getTempo() { return tempo; }
    public BigDecimal getValor() { return valor; }

    public void setId(Long id) { this.id = id; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setTempo(Long tempo) { this.tempo = tempo; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
