package com.barbearia.fahcortes.infra.persistence;

import com.barbearia.fahcortes.infra.entities.AgendamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<AgendamentoEntity, Long> {
    List<AgendamentoEntity> findByClienteId(Long clienteId);
}
