package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.TallaDTO;
import com.proyecto.integrado.vummy.entity.Talla;
import com.proyecto.integrado.vummy.entity.TallaNombre;
import com.proyecto.integrado.vummy.entity.Tienda;
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
    public ResponseEntity<List<TallaDTO>> obtenerTodas() {
        return ResponseEntity.ok(tallaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TallaDTO> obtenerPorId(@PathVariable Long id) {
        return tallaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{tiendaId}")
    public ResponseEntity<List<TallaDTO>> obtenerPorTienda(@PathVariable Long tiendaId) {
        return ResponseEntity.ok(tallaService.obtenerPorTienda(tiendaId));
    }

    @PostMapping
public ResponseEntity<TallaDTO> agregarTalla(@RequestBody TallaDTO tallaDTO) {
    Talla talla = new Talla();
    talla.setNombre(TallaNombre.valueOf(tallaDTO.getNombre()));
    talla.setAltura(tallaDTO.getAltura());
    talla.setCuelloManga(tallaDTO.getCuelloManga());
    talla.setPecho(tallaDTO.getPecho());
    talla.setCintura(tallaDTO.getCintura());
    talla.setCadera(tallaDTO.getCadera());
    talla.setEntrepierna(tallaDTO.getEntrepierna());
    if (tallaDTO.getTienda() != null) {
        Tienda tienda = new Tienda();
        tienda.setId(tallaDTO.getTienda().getId());
        talla.setTienda(tienda);
    }

    Talla tallaGuardada = tallaService.agregarTalla(talla);

    TallaDTO tallaGuardadaDTO = new TallaDTO(tallaGuardada);
    return ResponseEntity.ok(tallaGuardadaDTO);
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
