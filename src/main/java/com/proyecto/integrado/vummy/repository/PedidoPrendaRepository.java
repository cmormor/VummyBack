package com.proyecto.integrado.vummy.repository;

import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoPrendaRepository extends JpaRepository<PedidoPrenda, Long> {
    List<PedidoPrenda> findByPedidoId(Long pedidoId); // Obtener prendas de un pedido
}
