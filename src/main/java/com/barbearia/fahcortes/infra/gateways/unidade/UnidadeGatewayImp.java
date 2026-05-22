package com.barbearia.fahcortes.infra.gateways.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;
import com.barbearia.fahcortes.domain.gateways.unidade.UnidadeGateway;
import com.barbearia.fahcortes.infra.entities.UnidadeEntity;
import com.barbearia.fahcortes.infra.mapper.unidade.UnidadeMapper;
import com.barbearia.fahcortes.infra.persistence.UnidadeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UnidadeGatewayImp implements UnidadeGateway {

    private final UnidadeRepository unidadeRepository;
    private final UnidadeMapper unidadeMapper;

    public UnidadeGatewayImp(UnidadeRepository unidadeRepository, UnidadeMapper unidadeMapper) {
        this.unidadeRepository = unidadeRepository;
        this.unidadeMapper = unidadeMapper;
    }

    @Override
    public Unidade cadastrar(Unidade unidade) {
        UnidadeEntity entity = unidadeMapper.toEntity(unidade);
        return unidadeMapper.toDomain(unidadeRepository.save(entity));
    }

    @Override
    public Unidade buscarPorId(Long id) {
        return unidadeRepository.findById(id)
                .map(unidadeMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Unidade com id: " + id + " não encontrada"));
    }

    @Override
    public List<Unidade> listarTodas() {
        return unidadeRepository.findAll().stream()
                .map(unidadeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Unidade atualizar(Unidade unidade, Long id) {
        UnidadeEntity entity = unidadeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade com id: " + id + " não encontrada"));

        entity.setNome(unidade.getNome());
        entity.setEndereco(unidade.getEndereco());
        entity.setTelefone(unidade.getTelefone());
        entity.setLatitude(unidade.getLatitude());
        entity.setLongitude(unidade.getLongitude());
        entity.setHorarioAbertura(unidade.getHorarioAbertura());
        entity.setHorarioFechamento(unidade.getHorarioFechamento());

        return unidadeMapper.toDomain(unidadeRepository.save(entity));
    }

    @Override
    public void deletar(Long id) {
        if (!unidadeRepository.existsById(id)) {
            throw new IllegalArgumentException("Unidade com id: " + id + " não encontrada");
        }
        unidadeRepository.deleteById(id);
    }
}
