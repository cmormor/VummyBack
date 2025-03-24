package com.proyecto.integrado.vummy.repository;

import com.proyecto.integrado.vummy.entity.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrendaRepository extends JpaRepository<Prenda, Long> {
    List<Prenda> findByTiendaId(Long tiendaId); // Obtener todas las prendas de una tienda
}
