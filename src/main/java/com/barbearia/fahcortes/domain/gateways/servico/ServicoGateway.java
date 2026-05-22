package com.barbearia.fahcortes.domain.gateways.servico;

import com.barbearia.fahcortes.domain.entities.servico.Servico;

import java.util.List;

public interface ServicoGateway {

    Servico cadastrarServico(Servico servico);

    Servico buscarServicoPorId(Long id);

    void removerServicoPorId(Long id);

    List<Servico> listarTodosServicos();
}
