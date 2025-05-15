package com.proyecto.integrado.vummy.repository;

import com.proyecto.integrado.vummy.entity.PrendaTallaTienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrendaTallaTiendaRepository extends JpaRepository<PrendaTallaTienda, Long> {
    List<PrendaTallaTienda> findByPrendaIdAndTiendaId(Long prendaId, Long tiendaId);
    
    @Query("SELECT p.tienda.id FROM Prenda p WHERE p.id = :prendaId")
    Long findTiendaIdByPrendaId(@Param("prendaId") Long prendaId);
}
