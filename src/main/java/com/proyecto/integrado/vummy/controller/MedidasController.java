package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.entity.Medidas;
import com.proyecto.integrado.vummy.service.MedidasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/measurements")
public class MedidasController {

    private final MedidasService medidasService;

    public MedidasController(MedidasService medidasService) {
        this.medidasService = medidasService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medidas> obtenerPorId(@PathVariable Long id) {
        return medidasService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<Medidas> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return medidasService.obtenerPorUsuario(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medidas> agregarMedidas(@RequestBody Medidas medidas) {
        return ResponseEntity.ok(medidasService.guardarMedidas(medidas));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medidas> actualizarMedidas(@PathVariable Long id, @RequestBody Medidas medidas) {
        return medidasService.actualizarMedidas(id, medidas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}