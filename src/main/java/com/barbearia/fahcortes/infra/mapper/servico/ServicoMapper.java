package com.barbearia.fahcortes.infra.mapper.servico;

import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoRequestDto;
import com.barbearia.fahcortes.infra.controller.servico.dtos.ServicoResponseDto;
import com.barbearia.fahcortes.infra.entities.ServicoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {
    private final ModelMapper mapper ;

    public ServicoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ServicoEntity toEntity(Servico servico) {
        return mapper.map(servico, ServicoEntity.class);
    }

    public Servico toDomain(ServicoEntity servicoEntity) {
        return mapper.map(servicoEntity, Servico.class);
    }


    public ServicoResponseDto toDto(Servico servico) {
        return mapper.map(servico, ServicoResponseDto.class);
    }

    public Servico toDomain(ServicoRequestDto servicoRequestDto) {
        return mapper.map(servicoRequestDto, Servico.class);
    }

}
