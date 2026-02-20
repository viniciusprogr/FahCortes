package com.barbearia.fahcortes.infra.gateways;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.domain.gateways.usuario.UsuarioGateway;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import com.barbearia.fahcortes.infra.mapper.usuario.UsuarioMapper;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        if(!usuarioRepository.existsById(id)){
            throw new IllegalArgumentException("Não existe usuario com o id: " + id );
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario buscarPorId(Long id) {
       return usuarioRepository.findById(id).map(usuarioMapper::toDomain).orElseThrow(() -> new IllegalArgumentException("Usuario com id não encontrado"));
    }


    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.empty();
    }


    @Override
    public List<Usuario> ListarTodos() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDomain).collect(Collectors.toList());
    }

}
