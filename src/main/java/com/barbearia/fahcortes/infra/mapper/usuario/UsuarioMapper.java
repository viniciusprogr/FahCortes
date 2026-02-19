package com.barbearia.fahcortes.infra.mapper.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UsuarioEntity toEntity(Usuario usuario){
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha()
        );
    }

    public Usuario toDomain(UsuarioEntity usuarioEntity){
        return new Usuario(
                usuarioEntity.getId(),
                usuarioEntity.getNome(),
                usuarioEntity.getEmail(),
                usuarioEntity.getSenha()
        );
    }

    public Usuario requestToDomain(UsuarioRequestDto usuarioResquestDto) {
        return modelMapper.map(usuarioResquestDto, Usuario.class);
    }

    public UsuarioResponseDto DomainToResponse(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioResponseDto.class);
    }


    // entity

    public UsuarioResponseDto EntityToResponse(Optional<UsuarioEntity> usuarioEntity) {
        return modelMapper.map(usuarioEntity, UsuarioResponseDto.class);
    }
}
