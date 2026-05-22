package com.barbearia.fahcortes.infra.persistence;

import com.barbearia.fahcortes.infra.entities.BarbeiroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbeiroRepository extends JpaRepository<BarbeiroEntity, Long> {
}