package com.barbearia.fahcortes.infra.mapper.usuario;

import com.barbearia.fahcortes.domain.entities.usuario.Usuario;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioResponseDto;
import com.barbearia.fahcortes.infra.controller.usuario.dtos.UsuarioRequestDto;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Usuario toDomain(UsuarioEntity usuarioEntity){
        return new Usuario(
                usuarioEntity.getId(),
                usuarioEntity.getNome(),
                usuarioEntity.getEmail(),
                usuarioEntity.getSenha()
        );
    }

    public UsuarioEntity toEntity(Usuario usuario){
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha()
        );
    }

    public Usuario toDomain(UsuarioRequestDto usuarioRequestDto){
        return new Usuario(
                null,
                usuarioRequestDto.getNome(),
                usuarioRequestDto.getEmail(),
                usuarioRequestDto.getSenha()
        );
    }

    public UsuarioResponseDto toResponse(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioResponseDto.class);
    }

    public List<UsuarioResponseDto> toResponseDtoList(List<Usuario> usuarios){
        return usuarios.stream().map(this::toResponse).toList();
    }

    // entity

}

