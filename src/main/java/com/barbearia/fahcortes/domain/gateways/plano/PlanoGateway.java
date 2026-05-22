package com.barbearia.fahcortes.domain.gateways.plano;

import com.barbearia.fahcortes.domain.entities.plano.Plano;

import java.util.List;

public interface PlanoGateway {
    Plano cadastrar(Plano plano);
    Plano buscarPorId(Long id);
    List<Plano> listarTodos();
    Plano atualizar(Plano plano, Long id);
    void deletar(Long id);
}
