package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.service.TallaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sizes")
public class TallaController {

    private final TallaService tallaService;

    public TallaController(TallaService tallaService) {
        this.tallaService = tallaService;
    }

    @GetMapping
    public ResponseEntity<List<Talla>> obtenerTodas() {
        return ResponseEntity.ok(tallaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Talla> obtenerPorId(@PathVariable Long id) {
        return tallaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{tiendaId}")
    public ResponseEntity<List<Talla>> obtenerPorTienda(@PathVariable Long tiendaId) {
        return ResponseEntity.ok(tallaService.obtenerPorTienda(tiendaId));
    }

    @PostMapping
    public ResponseEntity<Talla> agregarTalla(@RequestBody Talla talla) {
        return ResponseEntity.ok(tallaService.agregarTalla(talla));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Talla> actualizarTalla(@PathVariable Long id, @RequestBody Talla talla) {
        return tallaService.actualizarTalla(id, talla)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTalla(@PathVariable Long id) {
        if (tallaService.eliminarTalla(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
