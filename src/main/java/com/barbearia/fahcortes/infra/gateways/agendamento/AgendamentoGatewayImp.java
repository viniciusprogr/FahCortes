package com.barbearia.fahcortes.infra.gateways.agendamento;

import com.barbearia.fahcortes.domain.entities.agendamento.Agendamento;
import com.barbearia.fahcortes.domain.entities.agendamento.AgendamentoStatus;
import com.barbearia.fahcortes.domain.exception.EntidadeNaoEncontradaException;
import com.barbearia.fahcortes.domain.exception.RegraDeNegocioException;
import com.barbearia.fahcortes.domain.gateways.agendamento.AgendamentoGateway;
import com.barbearia.fahcortes.infra.entities.AgendamentoEntity;
import com.barbearia.fahcortes.infra.mapper.agendamento.AgendamentoMapper;
import com.barbearia.fahcortes.infra.persistence.AgendamentoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgendamentoGatewayImp implements AgendamentoGateway {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoGatewayImp(AgendamentoRepository agendamentoRepository, AgendamentoMapper agendamentoMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoMapper = agendamentoMapper;
    }

    @Override
    @Transactional
    public Agendamento criar(Agendamento agendamento) {
        AgendamentoEntity entity = agendamentoMapper.toEntity(agendamento);
        return agendamentoMapper.toDomain(agendamentoRepository.save(entity));
    }

    @Override
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .map(agendamentoMapper::toDomain)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Agendamento com id " + id + " não encontrado. Verifique se o id informado está correto."));
    }

    @Override
    public List<Agendamento> listarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId).stream()
                .map(agendamentoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll().stream()
                .map(agendamentoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException(
                    "Não foi possível remover o agendamento com id " + id + ". Nenhum agendamento encontrado com esse id.");
        }
        agendamentoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Agendamento cancelar(Long id) {
        AgendamentoEntity entity = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Não foi possível cancelar. Agendamento com id " + id + " não encontrado."));

        if (entity.getStatus() == AgendamentoStatus.CANCELADO) {
            throw new RegraDeNegocioException(
                    "O agendamento com id " + id + " já está cancelado e não pode ser cancelado novamente.");
        }

        entity.setStatus(AgendamentoStatus.CANCELADO);
        return agendamentoMapper.toDomain(agendamentoRepository.save(entity));
    }
}
