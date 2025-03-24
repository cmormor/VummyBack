package com.proyecto.integrado.vummy.repository;

import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.entity.TallaNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Long> {
    List<Talla> findByTiendaId(Long tiendaId);
    List<Talla> findByTiendaIdAndNombre(Long tiendaId, TallaNombre nombre);
}
