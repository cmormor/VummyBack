package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.service.PrendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clothes")
public class PrendaController {

    private final PrendaService prendaService;

    public PrendaController(PrendaService prendaService) {
        this.prendaService = prendaService;
    }

    @GetMapping
    public ResponseEntity<List<Prenda>> obtenerTodas() {
        return ResponseEntity.ok(prendaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prenda> obtenerPorId(@PathVariable Long id) {
        return prendaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{tiendaId}")
    public ResponseEntity<List<Prenda>> obtenerPorTienda(@PathVariable Long tiendaId) {
        return ResponseEntity.ok(prendaService.obtenerPorTienda(tiendaId));
    }

    @PostMapping
    public ResponseEntity<Prenda> agregarPrenda(@RequestBody Prenda prenda) {
        return ResponseEntity.ok(prendaService.guardarPrenda(prenda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prenda> actualizarPrenda(@PathVariable Long id, @RequestBody Prenda prenda) {
        return prendaService.actualizarPrenda(id, prenda)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrenda(@PathVariable Long id) {
        if (prendaService.eliminarPrenda(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}