package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.repository.PedidoPrendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoPrendaService {

    private final PedidoPrendaRepository pedidoPrendaRepository;

    public PedidoPrendaService(PedidoPrendaRepository pedidoPrendaRepository) {
        this.pedidoPrendaRepository = pedidoPrendaRepository;
    }

    public List<PedidoPrenda> obtenerPrendasPorPedido(Long pedidoId) {
        return pedidoPrendaRepository.findByPedidoId(pedidoId);
    }

    public PedidoPrenda agregarPedidoPrenda(PedidoPrenda pedidoPrenda) {
        return pedidoPrendaRepository.save(pedidoPrenda);
    }

    public boolean eliminarPedidoPrenda(Long id) {
        if (pedidoPrendaRepository.existsById(id)) {
            pedidoPrendaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
