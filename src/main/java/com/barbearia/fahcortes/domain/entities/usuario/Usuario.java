package com.barbearia.fahcortes.domain.entities.usuario;

import java.util.regex.Pattern;

public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String novaSenha;
    private UsuarioEnum role;

    public Usuario() {}

    public Usuario(Long id, String nome, String email, String senha, UsuarioEnum role) {
        validarEmail(email);
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    public Usuario(Long id, String nome, String email, String senha, String novaSenha, UsuarioEnum role) {
        validarEmail(email);
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.novaSenha = novaSenha;
        this.role = role;
    }

    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail não pode ser vazio.");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new IllegalArgumentException("O formato do e-mail está inválido.");
        }
    }

    public UsuarioEnum getRole() { return role; }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getNovaSenha() { return novaSenha; }
}
