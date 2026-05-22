package com.barbearia.fahcortes.domain.entities.unidade;

public class Unidade {

    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private Double latitude;
    private Double longitude;
    private String horarioAbertura;
    private String horarioFechamento;

    public Unidade() {}

    public Unidade(Long id, String nome, String endereco, String telefone,
                   Double latitude, Double longitude, String horarioAbertura, String horarioFechamento) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getHorarioAbertura() { return horarioAbertura; }
    public String getHorarioFechamento() { return horarioFechamento; }

    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setHorarioAbertura(String horarioAbertura) { this.horarioAbertura = horarioAbertura; }
    public void setHorarioFechamento(String horarioFechamento) { this.horarioFechamento = horarioFechamento; }
}
