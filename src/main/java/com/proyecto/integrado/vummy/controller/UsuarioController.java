package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.*;
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
import java.util.Map;

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
  public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
    return usuarioService.obtenerPorId(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}/role")
  public ResponseEntity<String> obtenerRolPorId(@PathVariable Long id) {
    return usuarioService.obtenerPorId(id)
        .map(usuarioDTO -> usuarioDTO.getRol().toString())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/profile")
  public ResponseEntity<UsuarioDTO> obtenerMiPerfil(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(401).build();
    }
    String token = authHeader.substring(7);
    String email = jwtService.extractEmail(token);

    return usuarioService.obtenerPorEmail(email)
        .map(map -> {
          UsuarioDTO usuarioDTO = new UsuarioDTO(
              (Long) map.get("id"),
              (String) map.get("nombre"),
              (String) map.get("email"),
              null,
              (Double) map.get("altura"),
              (Double) map.get("cuelloManga"),
              (Double) map.get("pecho"),
              (Double) map.get("cintura"),
              (Double) map.get("cadera"),
              (Double) map.get("entrepierna"),
              null);
          return ResponseEntity.ok(usuarioDTO);
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/auth/register")
  public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody RegisterRequestDTO registerRequest) {
    Usuario usuario = mapRegisterRequestToUsuario(registerRequest);
    UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(usuario);
    return ResponseEntity.ok(usuarioDTO);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<UsuarioDTO> iniciarSesion(@RequestBody LoginRequestDTO loginRequest) {
    return usuarioService.iniciarSesion(loginRequest.getEmail(), loginRequest.getPassword())
        .map(ResponseEntity::ok)
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

  @PutMapping("/auth/reset-password")
  public ResponseEntity<String> resetearContrasena(@RequestBody ResetearContrasena resetPasswordDTO) {
    boolean actualizado = usuarioService.resetearContrasena(
        resetPasswordDTO.getEmail(),
        resetPasswordDTO.getNewPassword());
    if (actualizado) {
      return ResponseEntity.ok("Contraseña actualizada correctamente");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Usuario con ese correo no encontrado");
    }
  }

  @PutMapping("/profile")
  public ResponseEntity<UsuarioDTO> actualizarMiPerfil(HttpServletRequest request,
      @RequestBody UpdatePerfilDTO perfilActualizadoDTO) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(401).build();
    }
    String token = authHeader.substring(7);
    String email = jwtService.extractEmail(token);

    return usuarioService.obtenerUsuarioPorEmailByRepository(email)
        .flatMap(usuarioExistente -> {
          // Actualizamos solo los campos de perfil
          actualizarCamposPerfil(usuarioExistente, perfilActualizadoDTO);
          return usuarioService.actualizarUsuario(usuarioExistente.getId(), usuarioExistente);
        })
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> actualizarUsuario(@PathVariable Long id,
      @RequestBody UpdateUsuarioDTO usuarioActualizadoDTO) {
    try {
      Usuario usuarioActualizado = mapUpdateUsuarioDTOToUsuario(usuarioActualizadoDTO);
      return usuarioService.actualizarUsuario(id, usuarioActualizado)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
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
  public ResponseEntity<?> eliminarUsuario(
      @PathVariable Long id,
      @RequestHeader("Authorization") String authHeader) {

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(401).body("No autorizado: token faltante o inválido");
    }
    String token = authHeader.substring(7);
    String emailUsuarioActual = jwtService.extractEmail(token);

    try {
      boolean eliminado = usuarioService.eliminarUsuario(id, emailUsuarioActual);
      if (eliminado) {
        return ResponseEntity.noContent().build();
      } else {
        return ResponseEntity.status(404).body("No se pudo eliminar el usuario: no encontrado");
      }
    } catch (IllegalStateException e) {
      return ResponseEntity.status(403).body(e.getMessage());
    }
  }

  private Usuario mapRegisterRequestToUsuario(RegisterRequestDTO dto) {
    Usuario u = new Usuario();
    u.setEmail(dto.getEmail());
    u.setPassword(dto.getPassword());
    u.setNombre(dto.getNombre());
    u.setRol(dto.getRol());
    u.setAltura(dto.getAltura());
    u.setCuelloManga(dto.getCuelloManga());
    u.setPecho(dto.getPecho());
    u.setCintura(dto.getCintura());
    u.setCadera(dto.getCadera());
    u.setEntrepierna(dto.getEntrepierna());
    return u;
  }

  private void actualizarCamposPerfil(Usuario usuario, UpdatePerfilDTO dto) {
    if (dto.getNombre() != null)
      usuario.setNombre(dto.getNombre());
    if (dto.getAltura() != null)
      usuario.setAltura(dto.getAltura());
    if (dto.getCuelloManga() != null)
      usuario.setCuelloManga(dto.getCuelloManga());
    if (dto.getPecho() != null)
      usuario.setPecho(dto.getPecho());
    if (dto.getCintura() != null)
      usuario.setCintura(dto.getCintura());
    if (dto.getCadera() != null)
      usuario.setCadera(dto.getCadera());
    if (dto.getEntrepierna() != null)
      usuario.setEntrepierna(dto.getEntrepierna());
  }

  private Usuario mapUpdateUsuarioDTOToUsuario(UpdateUsuarioDTO dto) {
    Usuario u = new Usuario();
    u.setNombre(dto.getNombre());
    u.setEmail(dto.getEmail());
    u.setRol(dto.getRol());
    u.setAltura(dto.getAltura());
    u.setCuelloManga(dto.getCuelloManga());
    u.setPecho(dto.getPecho());
    u.setCintura(dto.getCintura());
    u.setCadera(dto.getCadera());
    u.setEntrepierna(dto.getEntrepierna());
    return u;
  }

}
