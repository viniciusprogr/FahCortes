package com.barbearia.fahcortes.infra.persistence;

import com.barbearia.fahcortes.infra.entities.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<ServicoEntity, Integer> {
}
