package com.barbearia.fahcortes.infra.gateways;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioGatewayImp implements UsuarioGateway {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioGatewayImp(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }


    @Override
    public Usuario cadastrarUsuario(Usuario usuario) {
        UsuarioEntity entity = usuarioMapper.toEntity(usuario);
        usuarioRepository.save(entity);
        return usuarioMapper.toDomain(entity);
    }

    @Override
    public void removerUsuario(Long id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).get();
        usuarioRepository.delete(usuarioEntity);
    }

    @Override
    public UsuarioResponseDto buscarPorId(Long id) {
       Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(id);
        return usuarioMapper.EntityToResponse(usuarioEntity);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        // 1. Busca a Entity no banco
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByEmail(email);

        // 2. Se encontrar, converte para Domínio (Usuario). Se não, retorna Optional vazio.
        return usuarioEntity.map(entity -> usuarioMapper.toDomain(entity));
    }

    @Override
    public List<Usuario> ListarTodos() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDomain).toList();
    }

}
