package com.barbearia.fahcortes.infra.gateways.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;
import com.barbearia.fahcortes.domain.gateways.plano.PlanoGateway;
import com.barbearia.fahcortes.infra.controller.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.infra.entities.PlanoEntity;
import com.barbearia.fahcortes.infra.mapper.plano.PlanoMapper;
import com.barbearia.fahcortes.infra.persistence.PlanoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanoGatewayImp implements PlanoGateway {

    private final PlanoRepository planoRepository;
    private final PlanoMapper planoMapper;

    public PlanoGatewayImp(PlanoRepository planoRepository, PlanoMapper planoMapper) {
        this.planoRepository = planoRepository;
        this.planoMapper = planoMapper;
    }

    @Override
    public Plano cadastrar(Plano plano) {
        PlanoEntity entity = planoMapper.toEntity(plano);
        return planoMapper.toDomain(planoRepository.save(entity));
    }

    @Override
    public Plano buscarPorId(Long id) {
        return planoRepository.findById(id)
                .map(planoMapper::toDomain)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Plano com id " + id + " não encontrado. Verifique se o id informado está correto."));
    }

    @Override
    public List<Plano> listarTodos() {
        return planoRepository.findAll().stream()
                .map(planoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Plano atualizar(Plano plano, Long id) {
        PlanoEntity entity = planoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Não foi possível atualizar. Plano com id " + id + " não encontrado."));

        entity.setNome(plano.getNome());
        entity.setDescricao(plano.getDescricao());
        entity.setPreco(plano.getPreco());
        entity.setBeneficios(plano.getBeneficios());
        entity.setValidade(plano.getValidade());

        return planoMapper.toDomain(planoRepository.save(entity));
    }

    @Override
    public void deletar(Long id) {
        if (!planoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException(
                    "Não foi possível remover o plano com id " + id + ". Nenhum plano encontrado com esse id.");
        }
        planoRepository.deleteById(id);
    }
}
