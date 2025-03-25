package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.service.TiendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
public class TiendaController {

    private final TiendaService tiendaService;

    public TiendaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    public ResponseEntity<List<Tienda>> obtenerTodas() {
        return ResponseEntity.ok(tiendaService.listarTiendas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tienda> obtenerPorId(@PathVariable Long id) {
        return tiendaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    public ResponseEntity<Tienda> obtenerPorNombre(@RequestParam String nombre) {
        return tiendaService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tienda> agregarTienda(@RequestBody Tienda tienda) {
        return ResponseEntity.ok(tiendaService.agregarTienda(tienda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tienda> actualizarTienda(@PathVariable Long id, @RequestBody Tienda tienda) {
        return tiendaService.actualizarTienda(id, tienda)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTienda(@PathVariable Long id) {
        if (tiendaService.eliminarTienda(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
