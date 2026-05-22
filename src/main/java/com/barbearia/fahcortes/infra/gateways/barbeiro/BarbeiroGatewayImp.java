package com.barbearia.fahcortes.infra.gateways.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;
import com.barbearia.fahcortes.domain.gateways.barbeiro.BarbeiroGateway;
import com.barbearia.fahcortes.infra.controller.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.infra.entities.BarbeiroEntity;
import com.barbearia.fahcortes.infra.mapper.barbeiro.BarbeiroMapper;
import com.barbearia.fahcortes.infra.persistence.BarbeiroRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BarbeiroGatewayImp implements BarbeiroGateway {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeiroMapper barbeiroMapper;

    public BarbeiroGatewayImp(BarbeiroRepository barbeiroRepository, BarbeiroMapper barbeiroMapper) {
        this.barbeiroRepository = barbeiroRepository;
        this.barbeiroMapper = barbeiroMapper;
    }

    @Override
    public Barbeiro cadastrar(Barbeiro barbeiro) {
        if (barbeiro.getCurtidas() == null) barbeiro.setCurtidas(0);
        if (barbeiro.getAtivo() == null) barbeiro.setAtivo(true);
        BarbeiroEntity entity = barbeiroMapper.toEntity(barbeiro);
        return barbeiroMapper.toDomain(barbeiroRepository.save(entity));
    }

    @Override
    public Barbeiro buscarPorId(Long id) {
        return barbeiroRepository.findById(id)
                .map(barbeiroMapper::toDomain)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Barbeiro com id " + id + " não encontrado. Verifique se o id informado está correto."));
    }

    @Override
    public List<Barbeiro> listarTodos() {
        return barbeiroRepository.findAll().stream()
                .map(barbeiroMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Barbeiro atualizar(Barbeiro barbeiro, Long id) {
        BarbeiroEntity entity = barbeiroRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Não foi possível atualizar. Barbeiro com id " + id + " não encontrado."));

        entity.setNome(barbeiro.getNome());
        entity.setEspecialidade(barbeiro.getEspecialidade());
        entity.setFoto(barbeiro.getFoto());
        if (barbeiro.getCurtidas() != null) entity.setCurtidas(barbeiro.getCurtidas());
        if (barbeiro.getAtivo() != null) entity.setAtivo(barbeiro.getAtivo());

        return barbeiroMapper.toDomain(barbeiroRepository.save(entity));
    }

    @Override
    public void deletar(Long id) {
        if (!barbeiroRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException(
                    "Não foi possível remover o barbeiro com id " + id + ". Nenhum barbeiro encontrado com esse id.");
        }
        barbeiroRepository.deleteById(id);
    }
}
