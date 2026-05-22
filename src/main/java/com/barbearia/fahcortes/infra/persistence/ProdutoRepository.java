package com.barbearia.fahcortes.infra.persistence;

import com.barbearia.fahcortes.infra.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
}
