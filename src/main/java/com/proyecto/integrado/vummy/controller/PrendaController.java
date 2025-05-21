package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.PrendaDTO;
import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.service.PrendaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        prenda.setDescripcion(prendaDTO.getDescripcion());
        prenda.setImagen(prendaDTO.getImagen());

        if (prendaDTO.getTiendaId() != null) {
            Tienda tienda = prendaService.obtenerTiendaPorId(prendaDTO.getTiendaId())
                    .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
            prenda.setTienda(tienda);
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
        prendaActualizada.setDescripcion(prendaDTO.getDescripcion());

        if (prendaDTO.getTiendaId() != null) {
            Tienda tienda = new Tienda();
            tienda.setId(prendaDTO.getTiendaId());
            prendaActualizada.setTienda(tienda);
        } else {
            prendaActualizada.setTienda(null);
        }
        return prendaService.actualizarPrenda(id, prendaActualizada)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirImagen(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            prendaService.guardarImagen(id, file);
            return ResponseEntity.ok("Imagen guardada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/imagen")
    public ResponseEntity<?> eliminarImagen(@PathVariable Long id) {
        try {
            prendaService.eliminarImagen(id);
            return ResponseEntity.ok("Imagen eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrenda(@PathVariable Long id) {
        if (prendaService.eliminarPrenda(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
