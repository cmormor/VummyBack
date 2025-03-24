package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Pedido;
import com.proyecto.integrado.vummy.entity.EstadoPedido;
import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.repository.PedidoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido agregarPedido(Pedido pedido) {
        pedido.setFecha(java.time.LocalDateTime.now());
        pedido.setEstado(EstadoPedido.CONFIRMADO);
        pedido.setTotal(calcularTotal(pedido.getPrendas()));
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(id);
        if (pedidoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();
            pedido.setEstado(nuevoEstado);
            return Optional.of(pedidoRepository.save(pedido));
        }
        return Optional.empty();
    }

    public boolean eliminarPedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Double calcularTotal(List<PedidoPrenda> prendas) {
        return prendas.stream()
                .mapToDouble(pp -> pp.getPrenda().getPrecio() * pp.getCantidad())
                .sum();
    }

    @Scheduled(fixedRate = 60000)
    public void actualizarEstadosAutomáticamente() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() == EstadoPedido.CONFIRMADO) {
                pedido.setEstado(EstadoPedido.ENVIADO);
            } else if (pedido.getEstado() == EstadoPedido.ENVIADO) {
                pedido.setEstado(EstadoPedido.ENTREGADO);
            }
            pedidoRepository.save(pedido);
        }
    }
}
