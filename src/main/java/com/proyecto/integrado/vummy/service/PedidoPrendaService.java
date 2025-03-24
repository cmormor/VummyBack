package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.repository.PedidoPrendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoPrendaService {

    private final PedidoPrendaRepository pedidoPrendaRepository;

    public PedidoPrendaService(PedidoPrendaRepository pedidoPrendaRepository) {
        this.pedidoPrendaRepository = pedidoPrendaRepository;
    }

    public List<PedidoPrenda> obtenerTodos() {
        return pedidoPrendaRepository.findAll();
    }

    public Optional<PedidoPrenda> obtenerPorId(Long id) {
        return pedidoPrendaRepository.findById(id);
    }

    public List<PedidoPrenda> obtenerPorPedido(Long pedidoId) {
        return pedidoPrendaRepository.findByPedidoId(pedidoId);
    }

    public PedidoPrenda guardarPedidoPrenda(PedidoPrenda pedidoPrenda) {
        return pedidoPrendaRepository.save(pedidoPrenda);
    }

    public Optional<PedidoPrenda> actualizarCantidad(Long id, int cantidad) {
        Optional<PedidoPrenda> pedidoPrendaOptional = pedidoPrendaRepository.findById(id);
        if (pedidoPrendaOptional.isPresent()) {
            PedidoPrenda pedidoPrenda = pedidoPrendaOptional.get();
            pedidoPrenda.setCantidad(cantidad);
            return Optional.of(pedidoPrendaRepository.save(pedidoPrenda));
        }
        return Optional.empty();
    }

    public boolean eliminarPedidoPrenda(Long id) {
        if (pedidoPrendaRepository.existsById(id)) {
            pedidoPrendaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}