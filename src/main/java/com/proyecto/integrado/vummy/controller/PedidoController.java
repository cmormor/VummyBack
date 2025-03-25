package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.entity.Pedido;
import com.proyecto.integrado.vummy.entity.EstadoPedido;
import com.proyecto.integrado.vummy.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPedidoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Pedido> agregarPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.agregarPedido(pedido));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        return pedidoService.actualizarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/update-status")
    public ResponseEntity<String> actualizarEstadosAutomáticamente() {
        pedidoService.actualizarEstadosAutomáticamente();
        return ResponseEntity.ok("Estados actualizados correctamente.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
