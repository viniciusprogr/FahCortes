package com.barbearia.fahcortes.infra.persistence;

import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
