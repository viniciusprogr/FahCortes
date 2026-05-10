package com.barbearia.fahcortes.infra.gateways.servico;

import com.barbearia.fahcortes.domain.entities.serviço.Servico;
import com.barbearia.fahcortes.domain.gateways.servico.ServicoGateway;
import com.barbearia.fahcortes.infra.entities.ServicoEntity;
import com.barbearia.fahcortes.infra.mapper.servico.ServicoMapper;
import com.barbearia.fahcortes.infra.persistence.ServicoRepository;
import org.springframework.stereotype.Component;

@Component
public class ServicoGatewayImp implements ServicoGateway {

    private final ServicoRepository servicoRepository;
    private final ServicoMapper servicoMapper;

    public ServicoGatewayImp(ServicoRepository servicoRepository, ServicoMapper servicoMapper) {
        this.servicoRepository = servicoRepository;
        this.servicoMapper = servicoMapper;
    }

    @Override
    public Servico cadastrarServico(Servico servico) {
        ServicoEntity servicoEntity = servicoMapper.toEntity(servico);
        servicoRepository.save(servicoEntity);
        return servicoMapper.toDomain(servicoEntity);
    }
}
