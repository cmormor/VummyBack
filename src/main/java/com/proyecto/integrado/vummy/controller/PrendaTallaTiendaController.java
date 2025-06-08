package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.PrendaTallaTiendaCreateDTO;
import com.proyecto.integrado.vummy.dto.PrendaTallaTiendaDTO;
import com.proyecto.integrado.vummy.service.PrendaTallaTiendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clothes/sizes")
@RequiredArgsConstructor
public class PrendaTallaTiendaController {

    private final PrendaTallaTiendaService service;

    @PostMapping(path = "/{prendaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> asociarTallas(
            @PathVariable Long prendaId,
            @RequestBody List<PrendaTallaTiendaCreateDTO> tallasDto) {
        service.asociarTallas(prendaId, tallasDto);
        return ResponseEntity.ok("Tallas asociadas correctamente.");
    }

    @GetMapping(path = "/{prendaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrendaTallaTiendaDTO>> obtenerTallas(
            @PathVariable Long prendaId) {
        return ResponseEntity.ok(service.obtenerTallasDisponibles(prendaId));
    }
}