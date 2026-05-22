package com.barbearia.fahcortes.infra.persistence;

import com.barbearia.fahcortes.infra.entities.PlanoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanoRepository extends JpaRepository<PlanoEntity, Long> {
}
