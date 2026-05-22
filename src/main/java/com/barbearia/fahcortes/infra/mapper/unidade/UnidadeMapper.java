package com.barbearia.fahcortes.infra.mapper.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.infra.controller.unidade.dtos.UnidadeRequestDto;
import com.barbearia.fahcortes.infra.controller.unidade.dtos.UnidadeResponseDto;
import com.barbearia.fahcortes.infra.entities.UnidadeEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnidadeMapper {
    private final ModelMapper mapper;
    public UnidadeMapper(ModelMapper mapper) { this.mapper = mapper; }

    public Unidade toDomain(UnidadeRequestDto dto) { return mapper.map(dto, Unidade.class); }
    public Unidade toDomain(UnidadeEntity entity) { return mapper.map(entity, Unidade.class); }
    public UnidadeEntity toEntity(Unidade unidade) { return mapper.map(unidade, UnidadeEntity.class); }
    public UnidadeResponseDto toResponse(Unidade unidade) { return mapper.map(unidade, UnidadeResponseDto.class); }
    public List<UnidadeResponseDto> toResponseList(List<Unidade> unidades) {
        return unidades.stream().map(this::toResponse).toList();
    }
}
