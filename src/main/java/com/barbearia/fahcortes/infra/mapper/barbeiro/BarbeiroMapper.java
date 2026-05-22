package com.barbearia.fahcortes.infra.mapper.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.infra.controller.barbeiro.dtos.BarbeiroRequestDto;
import com.barbearia.fahcortes.infra.controller.barbeiro.dtos.BarbeiroResponseDto;
import com.barbearia.fahcortes.infra.entities.BarbeiroEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BarbeiroMapper {

    private final ModelMapper mapper;

    public BarbeiroMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Barbeiro toDomain(BarbeiroRequestDto dto) {
        return mapper.map(dto, Barbeiro.class);
    }

    public Barbeiro toDomain(BarbeiroEntity entity) {
        return mapper.map(entity, Barbeiro.class);
    }

    public BarbeiroEntity toEntity(Barbeiro barbeiro) {
        return mapper.map(barbeiro, BarbeiroEntity.class);
    }

    public BarbeiroResponseDto toResponse(Barbeiro barbeiro) {
        return mapper.map(barbeiro, BarbeiroResponseDto.class);
    }

    public List<BarbeiroResponseDto> toResponseList(List<Barbeiro> barbeiros) {
        return barbeiros.stream().map(this::toResponse).toList();
    }
}