package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.PrendaTallaTiendaDTO;
import com.proyecto.integrado.vummy.entity.PrendaTallaTienda;
import com.proyecto.integrado.vummy.service.PrendaTallaTiendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clothes/sizes")
@RequiredArgsConstructor
public class PrendaTallaTiendaController {

    private final PrendaTallaTiendaService prendaTallaTiendaService;

    @PostMapping("/{prendaId}")
    public ResponseEntity<String> asociarTallas(
            @PathVariable Long prendaId,
            @RequestBody PrendaTallaTienda talla) {
        prendaTallaTiendaService.asociarTallas(prendaId, List.of(talla));
        return ResponseEntity.ok("Talla asociada correctamente a la prenda.");
    }

    @GetMapping("/{prendaId}")
    public ResponseEntity<List<PrendaTallaTiendaDTO>> obtenerTallasDisponibles(
            @PathVariable Long prendaId) {
        return ResponseEntity.ok(prendaTallaTiendaService.obtenerTallasDisponibles(prendaId));
    }
}