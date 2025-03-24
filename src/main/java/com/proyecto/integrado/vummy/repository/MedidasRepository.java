package com.proyecto.integrado.vummy.repository;

import com.proyecto.integrado.vummy.entity.Medidas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedidasRepository extends JpaRepository<Medidas, Long> {
    Optional<Medidas> findByUsuarioId(Long usuarioId);
}
