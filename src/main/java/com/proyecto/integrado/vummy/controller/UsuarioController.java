package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.UsuarioDTO;
import com.proyecto.integrado.vummy.entity.Rol;
import com.proyecto.integrado.vummy.entity.Usuario;
import com.proyecto.integrado.vummy.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5173", "https://www.vummyapp.com"})
@RestController
@RequestMapping("/api/v1/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .filter(usuario -> usuario.getRol() != Rol.VISITANTE)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/email")
    public ResponseEntity<UsuarioDTO> obtenerPorEmail(@RequestParam String email) {
        return usuarioService.obtenerPorEmail(email)
                .filter(usuario -> usuario.getRol() != Rol.VISITANTE)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody Usuario usuario) {
        UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> iniciarSesion(@RequestParam String email, @RequestParam String password) {
        return usuarioService.iniciarSesion(email, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(id, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UsuarioDTO> actualizarRol(@PathVariable Long id, @RequestParam String rol) {
        try {
            Rol nuevoRol = Rol.valueOf(rol.toUpperCase());
            return usuarioService.actualizarRol(id, nuevoRol)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioService.eliminarUsuario(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
