package com.barbearia.fahcortes.infra.persistence;

import com.barbearia.fahcortes.infra.entities.UnidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadeRepository extends JpaRepository<UnidadeEntity, Long> {
}
