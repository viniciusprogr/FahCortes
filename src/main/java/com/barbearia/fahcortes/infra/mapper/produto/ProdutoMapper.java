package com.barbearia.fahcortes.infra.mapper.produto;

import com.barbearia.fahcortes.domain.entities.produto.Produto;
import com.barbearia.fahcortes.infra.controller.produto.dtos.ProdutoRequestDto;
import com.barbearia.fahcortes.infra.controller.produto.dtos.ProdutoResponseDto;
import com.barbearia.fahcortes.infra.entities.ProdutoEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoMapper {
    private final ModelMapper mapper;
    public ProdutoMapper(ModelMapper mapper) { this.mapper = mapper; }

    public Produto toDomain(ProdutoRequestDto dto) { return mapper.map(dto, Produto.class); }
    public Produto toDomain(ProdutoEntity entity) { return mapper.map(entity, Produto.class); }
    public ProdutoEntity toEntity(Produto produto) { return mapper.map(produto, ProdutoEntity.class); }
    public ProdutoResponseDto toResponse(Produto produto) { return mapper.map(produto, ProdutoResponseDto.class); }
    public List<ProdutoResponseDto> toResponseList(List<Produto> produtos) {
        return produtos.stream().map(this::toResponse).toList();
    }
}
