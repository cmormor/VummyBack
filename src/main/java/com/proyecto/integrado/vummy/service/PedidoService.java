package com.proyecto.integrado.vummy.service;

import com.proyecto.integrado.vummy.entity.Pedido;
import com.proyecto.integrado.vummy.entity.EstadoPedido;
import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.repository.PedidoRepository;
import com.proyecto.integrado.vummy.repository.PrendaRepository;
import com.proyecto.integrado.vummy.dto.PedidoDTO;
import com.proyecto.integrado.vummy.dto.PedidoPrendaDTO;
import com.proyecto.integrado.vummy.repository.TallaRepository;

import jakarta.transaction.Transactional;

import com.proyecto.integrado.vummy.repository.PrendaTallaTiendaRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PrendaRepository prendaRepository;
    private final TallaRepository tallaRepository;
    private final PrendaTallaTiendaRepository prendaTallaTiendaRepository;

    public PedidoService(PedidoRepository pedidoRepository, PrendaRepository prendaRepository,
            TallaRepository tallaRepository, PrendaTallaTiendaRepository prendaTallaTiendaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.prendaRepository = prendaRepository;
        this.tallaRepository = tallaRepository;
        this.prendaTallaTiendaRepository = prendaTallaTiendaRepository;
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
        if (pedido.getPrendas() != null) {
            for (PedidoPrenda pp : pedido.getPrendas()) {
                Long prendaId = pp.getPrenda().getId();
                Long tallaId = pp.getTalla().getId();

                pp.setPrenda(prendaRepository.findById(prendaId)
                        .orElseThrow(() -> new RuntimeException("Prenda no encontrada: " + prendaId)));
                pp.setTalla(tallaRepository.findById(tallaId)
                        .orElseThrow(() -> new RuntimeException("Talla no encontrada: " + tallaId)));

                Long tiendaId = prendaTallaTiendaRepository.findTiendaIdByPrendaId(prendaId);
                boolean existe = prendaTallaTiendaRepository
                        .findByPrendaIdAndTiendaId(prendaId, tiendaId)
                        .stream()
                        .anyMatch(ptt -> ptt.getTalla().getId().equals(tallaId));
                if (!existe) {
                    throw new RuntimeException("La talla seleccionada no está asignada a la prenda");
                }

                pp.setPedido(pedido);
            }
        }
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

    @Transactional
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

    public PedidoDTO toPedidoDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());

        PedidoDTO.UsuarioInfo usuarioInfo = new PedidoDTO.UsuarioInfo();
        usuarioInfo.setId(pedido.getUsuario().getId());
        usuarioInfo.setNombre(pedido.getUsuario().getNombre());
        dto.setUsuario(usuarioInfo);

        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado().name());
        dto.setTotal(pedido.getTotal());

        List<PedidoPrendaDTO> prendasDTO = pedido.getPrendas().stream().map(pp -> {
            PedidoPrendaDTO ppDTO = new PedidoPrendaDTO();

            ppDTO.setId(pp.getId());

            PedidoPrendaDTO.PrendaInfo prendaInfo = new PedidoPrendaDTO.PrendaInfo();
            prendaInfo.setId(pp.getPrenda().getId());
            prendaInfo.setNombre(pp.getPrenda().getNombre());
            prendaInfo.setDescripcion(pp.getPrenda().getDescripcion());
            prendaInfo.setPrecio(pp.getPrenda().getPrecio());
            prendaInfo.setTiendaNombre(pp.getPrenda().getTienda().getNombre());
            ppDTO.setPrenda(prendaInfo);

            PedidoPrendaDTO.TallaInfo tallaInfo = new PedidoPrendaDTO.TallaInfo();
            tallaInfo.setId(pp.getTalla().getId());
            tallaInfo.setNombre(pp.getTalla().getNombre().name());
            ppDTO.setTalla(tallaInfo);

            PedidoPrendaDTO.PedidoInfo pedidoInfo = new PedidoPrendaDTO.PedidoInfo();
            pedidoInfo.setId(pedido.getId());
            pedidoInfo.setUsuario(pedido.getUsuario().getNombre());
            ppDTO.setPedido(pedidoInfo);

            ppDTO.setCantidad(pp.getCantidad());
            return ppDTO;
        }).toList();

        dto.setPrendas(prendasDTO);
        return dto;
    }
}
