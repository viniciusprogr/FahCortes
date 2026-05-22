package com.barbearia.fahcortes.infra.mapper.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.infra.controller.agendamento.dtos.AgendamentoRequestDto;
import com.barbearia.fahcortes.infra.controller.agendamento.dtos.AgendamentoResponseDto;
import com.barbearia.fahcortes.infra.entities.AgendamentoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgendamentoMapper {

    private final ModelMapper mapper;

    public AgendamentoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Agendamento toDomain(AgendamentoRequestDto dto) {
        return mapper.map(dto, Agendamento.class);
    }

    public Agendamento toDomain(AgendamentoEntity entity) {
        return mapper.map(entity, Agendamento.class);
    }

    public AgendamentoEntity toEntity(Agendamento agendamento) {
        return mapper.map(agendamento, AgendamentoEntity.class);
    }

    public AgendamentoResponseDto toResponse(Agendamento agendamento) {
        return mapper.map(agendamento, AgendamentoResponseDto.class);
    }

    public List<AgendamentoResponseDto> toResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream().map(this::toResponse).toList();
    }
}
