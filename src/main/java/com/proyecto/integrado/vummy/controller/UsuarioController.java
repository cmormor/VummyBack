package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.LoginRequestDTO;
import com.proyecto.integrado.vummy.dto.UsuarioDTO;
import com.proyecto.integrado.vummy.entity.Rol;
import com.proyecto.integrado.vummy.entity.Usuario;
import com.proyecto.integrado.vummy.security.jwt.JwtService;
import com.proyecto.integrado.vummy.security.jwt.TokenBlacklistService;
import com.proyecto.integrado.vummy.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public UsuarioController(UsuarioService usuarioService,
                             JwtService jwtService,
                             TokenBlacklistService tokenBlacklistService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
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

    @PostMapping("/auth/register")
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody Usuario usuario) {
        UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UsuarioDTO> iniciarSesion(@RequestBody LoginRequestDTO loginRequest) {
        return usuarioService.iniciarSesion(loginRequest.getEmail(), loginRequest.getPassword())
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            long expirationTime = jwtService.extractExpiration(token).getTime();
            tokenBlacklistService.addTokenToBlacklist(token, expirationTime);
        }

        return ResponseEntity.ok("Sesión cerrada exitosamente");
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
