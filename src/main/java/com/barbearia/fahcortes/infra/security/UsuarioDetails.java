package com.barbearia.fahcortes.infra.security;

import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class UsuarioDetails implements UserDetails {
    private final UsuarioEntity usuarioEntity;

    public UsuarioDetails(UsuarioEntity usuarioEntity) {
        this.usuarioEntity = usuarioEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuarioEntity.getRole()));
    }

    @Override
    public String getPassword() {
        return usuarioEntity.getSenha();
    }

    @Override
    public String getUsername() {
        return usuarioEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

   // Metodo auxiliar para acessar o usuário original quando necessário
    public UsuarioEntity getUsuarioEntity() {
        return usuarioEntity;
    }

}
