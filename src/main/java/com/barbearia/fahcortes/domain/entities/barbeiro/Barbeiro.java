package com.barbearia.fahcortes.domain.entities.barbeiro;

public class Barbeiro {

    private Long id;
    private String nome;
    private String especialidade;
    private String foto;
    private Integer curtidas;
    private Boolean ativo;

    public Barbeiro() {}

    public Barbeiro(Long id, String nome, String especialidade, String foto, Integer curtidas, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.foto = foto;
        this.curtidas = curtidas;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecialidade() { return especialidade; }
    public String getFoto() { return foto; }
    public Integer getCurtidas() { return curtidas; }
    public Boolean getAtivo() { return ativo; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setCurtidas(Integer curtidas) { this.curtidas = curtidas; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}