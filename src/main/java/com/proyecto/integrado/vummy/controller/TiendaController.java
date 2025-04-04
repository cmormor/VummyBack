package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.PrendaDTO;
import com.proyecto.integrado.vummy.dto.TallaDTO;
import com.proyecto.integrado.vummy.dto.TiendaDTO;
import com.proyecto.integrado.vummy.entity.Tienda;
import com.proyecto.integrado.vummy.service.TiendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5173", "https://www.vummyapp.com"})
@RestController
@RequestMapping("/api/v1/stores")
public class TiendaController {

    private final TiendaService tiendaService;

    public TiendaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    public ResponseEntity<List<TiendaDTO>> obtenerTodas() {
        return ResponseEntity.ok(tiendaService.listarTiendas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TiendaDTO> obtenerPorId(@PathVariable Long id) {
        return tiendaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    public ResponseEntity<TiendaDTO> obtenerPorNombre(@RequestParam String nombre) {
        return tiendaService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        @GetMapping("/{id}/sizes")
    public ResponseEntity<List<TallaDTO>> obtenerTallasPorTienda(@PathVariable Long id) {
        List<TallaDTO> tallas = tiendaService.listarTallasPorTienda(id);
        if (tallas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tallas);
    }

        @GetMapping("/{id}/clothes")
    public ResponseEntity<List<PrendaDTO>> obtenerPrendasPorTienda(@PathVariable Long id) {
        List<PrendaDTO> prendas = tiendaService.listarPrendasPorTienda(id);
        if (prendas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prendas);
    }


    @PostMapping
    public ResponseEntity<TiendaDTO> agregarTienda(@RequestBody TiendaDTO tiendaDTO) {
      Tienda tienda = new Tienda();
      tienda.setNombre(tiendaDTO.getNombre());
      tienda.setDescripcion(tiendaDTO.getDescripcion());

      TiendaDTO tiendaGuardada = tiendaService.agregarTienda(tiendaDTO);
      return ResponseEntity.status(201).body(tiendaGuardada);
}

    @PutMapping("/{id}")
    public ResponseEntity<TiendaDTO> actualizarTienda(@PathVariable Long id, @RequestBody Tienda tienda) {
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
