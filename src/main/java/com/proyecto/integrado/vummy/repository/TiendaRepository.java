package com.proyecto.integrado.vummy.repository;

import com.proyecto.integrado.vummy.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {
    Optional<Tienda> findByNombre(String nombre);
}
