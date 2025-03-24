package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.service.PedidoPrendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class PedidoPrendaController {

    private final PedidoPrendaService pedidoPrendaService;

    public PedidoPrendaController(PedidoPrendaService pedidoPrendaService) {
        this.pedidoPrendaService = pedidoPrendaService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoPrenda>> obtenerTodos() {
        return ResponseEntity.ok(pedidoPrendaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoPrenda> obtenerPorId(@PathVariable Long id) {
        return pedidoPrendaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{pedidoId}")
    public ResponseEntity<List<PedidoPrenda>> obtenerPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoPrendaService.obtenerPorPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<PedidoPrenda> agregarPedidoPrenda(@RequestBody PedidoPrenda pedidoPrenda) {
        return ResponseEntity.ok(pedidoPrendaService.guardarPedidoPrenda(pedidoPrenda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoPrenda> actualizarCantidad(@PathVariable Long id, @RequestParam int cantidad) {
        return pedidoPrendaService.actualizarCantidad(id, cantidad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedidoPrenda(@PathVariable Long id) {
        if (pedidoPrendaService.eliminarPedidoPrenda(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
