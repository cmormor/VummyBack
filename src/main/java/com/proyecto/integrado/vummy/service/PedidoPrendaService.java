package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.dto.PedidoPrendaDTO;
import com.proyecto.integrado.vummy.entity.Pedido;
import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.repository.PedidoPrendaRepository;
import com.proyecto.integrado.vummy.repository.PedidoRepository;
import com.proyecto.integrado.vummy.repository.PrendaRepository;
import com.proyecto.integrado.vummy.repository.TallaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoPrendaService {

    private final PedidoPrendaRepository pedidoPrendaRepository;
    private final TallaRepository tallaRepository;
    private final PrendaRepository prendaRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoPrendaService(PedidoPrendaRepository pedidoPrendaRepository, TallaRepository tallaRepository,
            PrendaRepository prendaRepository, PedidoRepository pedidoRepository) {
        this.pedidoPrendaRepository = pedidoPrendaRepository;
        this.tallaRepository = tallaRepository;
        this.prendaRepository = prendaRepository;
        this.pedidoRepository = pedidoRepository;
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
        pedidoPrenda.setTalla(
                tallaRepository.findById(pedidoPrenda.getTalla().getId())
                        .orElseThrow(() -> new RuntimeException("Talla no encontrada")));
        pedidoPrenda.setPrenda(
                prendaRepository.findById(pedidoPrenda.getPrenda().getId())
                        .orElseThrow(() -> new RuntimeException("Prenda no encontrada")));
        pedidoPrenda.setPedido(
                pedidoRepository.findById(pedidoPrenda.getPedido().getId())
                        .orElseThrow(() -> new RuntimeException("Pedido no encontrado")));
        PedidoPrenda guardado = pedidoPrendaRepository.save(pedidoPrenda);

        Pedido pedido = guardado.getPedido();
        List<PedidoPrenda> prendasActualizadas = pedidoPrendaRepository.findByPedidoId(pedido.getId());
        double nuevoTotal = prendasActualizadas.stream()
            .mapToDouble(pp -> pp.getPrenda().getPrecio() * pp.getCantidad())
            .sum();
        pedido.setTotal(nuevoTotal);
        pedidoRepository.save(pedido);

        return guardado;
    }

    public Optional<PedidoPrenda> actualizarCantidad(Long id, int cantidad) {
        Optional<PedidoPrenda> pedidoPrendaOptional = pedidoPrendaRepository.findById(id);
        if (pedidoPrendaOptional.isPresent()) {
            PedidoPrenda pedidoPrenda = pedidoPrendaOptional.get();
            pedidoPrenda.setCantidad(cantidad);
            PedidoPrenda actualizado = pedidoPrendaRepository.save(pedidoPrenda);

            Pedido pedido = pedidoPrenda.getPedido();
            double nuevoTotal = pedido.getPrendas().stream()
                .mapToDouble(pp -> pp.getPrenda().getPrecio() * pp.getCantidad())
                .sum();
            pedido.setTotal(nuevoTotal);
            pedidoRepository.save(pedido);

            return Optional.of(actualizado);
        }
        return Optional.empty();
    }

    public boolean eliminarPedidoPrenda(Long id) {
        Optional<PedidoPrenda> pedidoPrendaOptional = pedidoPrendaRepository.findById(id);
        if (pedidoPrendaOptional.isPresent()) {
            PedidoPrenda pedidoPrenda = pedidoPrendaOptional.get();
            Pedido pedido = pedidoPrenda.getPedido();
            pedidoPrendaRepository.deleteById(id);

            double nuevoTotal = pedido.getPrendas().stream()
                .filter(pp -> !pp.getId().equals(id))
                .mapToDouble(pp -> pp.getPrenda().getPrecio() * pp.getCantidad())
                .sum();
            pedido.setTotal(nuevoTotal);
            pedidoRepository.save(pedido);

            return true;
        }
        return false;
    }

    public PedidoPrendaDTO toPedidoPrendaDTO(PedidoPrenda pedidoPrenda) {
        PedidoPrendaDTO dto = new PedidoPrendaDTO();
        dto.setId(pedidoPrenda.getId());

        PedidoPrendaDTO.PrendaInfo prendaInfo = new PedidoPrendaDTO.PrendaInfo();
        prendaInfo.setId(pedidoPrenda.getPrenda().getId());
        prendaInfo.setNombre(pedidoPrenda.getPrenda().getNombre());
        prendaInfo.setPrecio(pedidoPrenda.getPrenda().getPrecio());
        dto.setPrenda(prendaInfo);

        PedidoPrendaDTO.TallaInfo tallaInfo = new PedidoPrendaDTO.TallaInfo();
        tallaInfo.setId(pedidoPrenda.getTalla().getId());
        tallaInfo.setNombre(pedidoPrenda.getTalla().getNombre().toString());
        dto.setTalla(tallaInfo);

        PedidoPrendaDTO.PedidoInfo pedidoInfo = new PedidoPrendaDTO.PedidoInfo();
        pedidoInfo.setId(pedidoPrenda.getPedido().getId());
        pedidoInfo.setUsuario(pedidoPrenda.getPedido().getUsuario().getNombre());
        dto.setPedido(pedidoInfo);

        dto.setCantidad(pedidoPrenda.getCantidad());
        return dto;
    }
}