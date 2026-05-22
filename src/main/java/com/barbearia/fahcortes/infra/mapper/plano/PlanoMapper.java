package com.barbearia.fahcortes.infra.mapper.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.infra.controller.plano.dtos.PlanoRequestDto;
import com.barbearia.fahcortes.infra.controller.plano.dtos.PlanoResponseDto;
import com.barbearia.fahcortes.infra.entities.PlanoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanoMapper {
    private final ModelMapper mapper;
    public PlanoMapper(ModelMapper mapper) { this.mapper = mapper; }

    public Plano toDomain(PlanoRequestDto dto) { return mapper.map(dto, Plano.class); }
    public Plano toDomain(PlanoEntity entity) { return mapper.map(entity, Plano.class); }
    public PlanoEntity toEntity(Plano plano) { return mapper.map(plano, PlanoEntity.class); }
    public PlanoResponseDto toResponse(Plano plano) { return mapper.map(plano, PlanoResponseDto.class); }
    public List<PlanoResponseDto> toResponseList(List<Plano> planos) {
        return planos.stream().map(this::toResponse).toList();
    }
}
