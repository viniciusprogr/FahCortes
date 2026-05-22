package com.barbearia.fahcortes.domain.gateways.unidade;

import com.barbearia.fahcortes.domain.entities.unidade.Unidade;

import java.util.List;

public interface UnidadeGateway {
    Unidade cadastrar(Unidade unidade);
    Unidade buscarPorId(Long id);
    List<Unidade> listarTodas();
    Unidade atualizar(Unidade unidade, Long id);
    void deletar(Long id);
}
