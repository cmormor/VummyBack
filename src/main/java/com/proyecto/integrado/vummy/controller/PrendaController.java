package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.PrendaDTO;
import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.service.PrendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5173", "https://www.vummyapp.com"})
@RestController
@RequestMapping("/api/v1/clothes")
public class PrendaController {

    private final PrendaService prendaService;

    public PrendaController(PrendaService prendaService) {
        this.prendaService = prendaService;
    }

    @GetMapping
    public ResponseEntity<List<PrendaDTO>> obtenerTodas() {
        return ResponseEntity.ok(prendaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrendaDTO> obtenerPorId(@PathVariable Long id) {
        return prendaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{tiendaId}")
    public ResponseEntity<List<PrendaDTO>> obtenerPorTienda(@PathVariable Long tiendaId) {
        return ResponseEntity.ok(prendaService.obtenerPorTienda(tiendaId));
    }

    @PostMapping
    public ResponseEntity<PrendaDTO> agregarPrenda(@RequestBody PrendaDTO prendaDTO) {
        Prenda prenda = new Prenda();
        prenda.setNombre(prendaDTO.getNombre());
        prenda.setPrecio(prendaDTO.getPrecio());

        if (prendaDTO.getTiendaId() != null) {
            Tienda tienda = prendaService.obtenerTiendaPorId(prendaDTO.getTiendaId())
                    .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
            prenda.setTienda(tienda);
        }

        if (prendaDTO.getTallaId() != null) {
            Talla talla = prendaService.obtenerTallaPorId(prendaDTO.getTallaId())
                    .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
            prenda.setTalla(talla);
        }

        PrendaDTO prendaGuardadaDTO = prendaService.guardarPrenda(prenda);
        return ResponseEntity.status(201).body(prendaGuardadaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrendaDTO> actualizarPrenda(@PathVariable Long id, @RequestBody PrendaDTO prendaDTO) {
        Prenda prendaActualizada = new Prenda();
        prendaActualizada.setId(id);
        prendaActualizada.setNombre(prendaDTO.getNombre());
        prendaActualizada.setPrecio(prendaDTO.getPrecio());

        if (prendaDTO.getTiendaId() != null) {
            Tienda tienda = new Tienda();
            tienda.setId(prendaDTO.getTiendaId());
            prendaActualizada.setTienda(tienda);
        } else {
            prendaActualizada.setTienda(null);
        }

        if (prendaDTO.getTallaId() != null) {
            Talla talla = new Talla();
            talla.setId(prendaDTO.getTallaId());
            prendaActualizada.setTalla(talla);
        } else {
            prendaActualizada.setTalla(null);
        }

        return prendaService.actualizarPrenda(id, prendaActualizada)
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
