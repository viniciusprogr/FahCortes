package com.barbearia.fahcortes.domain.entities.usuario;

import java.util.regex.Pattern;

public class Usuario {

    private Long id;

    private String nome;

    private String email;

    private String senha;

    private UsuarioEnum tipo;

    public Usuario() {
        super();
    }

    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail não pode ser vazio.");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("O formato do e-mail está inválido.");
        }
    }
    public Usuario(Long id, String nome, String email, String senha, UsuarioEnum tipo) {
        validarEmail(email);
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = (tipo == null) ? UsuarioEnum.USER : tipo;
    }

    public UsuarioEnum getTipo() {
        return tipo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}
