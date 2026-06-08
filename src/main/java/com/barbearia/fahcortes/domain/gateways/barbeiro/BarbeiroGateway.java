package com.barbearia.fahcortes.domain.gateways.barbeiro;

import com.barbearia.fahcortes.domain.entities.barbeiro.Barbeiro;

import java.util.List;

public interface BarbeiroGateway {

    Barbeiro cadastrar(Barbeiro barbeiro);

    Barbeiro buscarPorId(Long id);

    List<Barbeiro> listarTodos();

    Barbeiro atualizar(Barbeiro barbeiro, Long id);

    void deletar(Long id);

    Barbeiro curtir(Long id);
}