package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.PedidoPrendaDTO;
import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.service.PedidoPrendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
public class PedidoPrendaController {

    private final PedidoPrendaService pedidoPrendaService;

    public PedidoPrendaController(PedidoPrendaService pedidoPrendaService) {
        this.pedidoPrendaService = pedidoPrendaService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoPrendaDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                pedidoPrendaService.obtenerTodos()
                        .stream()
                        .map(pedidoPrendaService::toDTO)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoPrendaDTO> obtenerPorId(@PathVariable Long id) {
        return pedidoPrendaService.obtenerPorId(id)
                .map(pedidoPrendaService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{pedidoId}")
    public ResponseEntity<List<PedidoPrendaDTO>> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(
                pedidoPrendaService.obtenerPorPedido(pedidoId)
                        .stream()
                        .map(pedidoPrendaService::toDTO)
                        .toList());
    }

    @PostMapping
    public ResponseEntity<PedidoPrendaDTO> agregarPedidoPrenda(@RequestBody PedidoPrenda pedidoPrenda) {
        PedidoPrenda saved = pedidoPrendaService.guardarPedidoPrenda(pedidoPrenda);
        return ResponseEntity.ok(pedidoPrendaService.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoPrendaDTO> actualizarCantidad(@PathVariable Long id, @RequestParam int cantidad) {
        return pedidoPrendaService.actualizarCantidad(id, cantidad)
                .map(pedidoPrendaService::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // TODO: NO ME RECALCULA EL TOTAL AL ACTUALIZAR LA CANTIDAD

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedidoPrenda(@PathVariable Long id) {
        if (pedidoPrendaService.eliminarPedidoPrenda(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // TODO: NO ME RECALCULA EL TOTAL AL ELIMINAR UN ITEM
}
