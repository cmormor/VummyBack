package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.dto.ItemCarritoDTO;
import com.proyecto.integrado.vummy.entity.PrendaCarrito;
import com.proyecto.integrado.vummy.entity.Usuario;
import com.proyecto.integrado.vummy.repository.UsuarioRepository;
import com.proyecto.integrado.vummy.security.jwt.JwtService;
import com.proyecto.integrado.vummy.service.PrendaCarritoService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/cart")
public class PrendaCarritoController {

    @Autowired
    private PrendaCarritoService servicio;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> agregarAlCarrito(@RequestBody ItemCarritoDTO dto,
                                              @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token no válido");
        }

        String token = authHeader.substring(7);
        String correo;
        try {
            correo = jwtService.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        Usuario usuario = usuarioRepository.findByEmail(correo).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        PrendaCarrito prendaCarrito = new PrendaCarrito();
        prendaCarrito.setCorreo(correo);
        prendaCarrito.setPrenda(dto.getPrenda());
        prendaCarrito.setTalla(dto.getTalla());
        prendaCarrito.setCantidad(dto.getCantidad());

        servicio.guardarPedidoCarrito(prendaCarrito);
        return ResponseEntity.ok().build();
    }

  @GetMapping
  public ResponseEntity<?> obtenerCarrito(@RequestHeader("Authorization") String authHeader) {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          return ResponseEntity.status(401).body("Token no válido");
      }

      String token = authHeader.substring(7);
      String correo;
      try {
          correo = jwtService.extractEmail(token);
      } catch (Exception e) {
          return ResponseEntity.status(401).body("Token inválido o expirado");
      }

      Usuario usuario = usuarioRepository.findByEmail(correo).orElse(null);
      if (usuario == null) {
          return ResponseEntity.status(404).body("Usuario no encontrado");
      }

      List<Object> carritoFiltrado = servicio.obtenerPorCorreo(correo).stream().map(item -> {
          return new java.util.LinkedHashMap<String, Object>() {{
              put("id", item.getId());
              put("correo", item.getCorreo());
              
              put("prenda", new java.util.LinkedHashMap<String, Object>() {{
                put("id", item.getPrenda().getId());
                put("nombre", item.getPrenda().getNombre());
                put("precio", item.getPrenda().getPrecio());
              }});
              
              put("talla", new java.util.LinkedHashMap<String, Object>() {{
                put("id", item.getTalla().getId());
                put("nombre", item.getTalla().getNombre().name());
              }});

              put("cantidad", item.getCantidad());
            }};
      }).collect(Collectors.toList());

      return ResponseEntity.ok(carritoFiltrado);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> actualizarCantidad(@PathVariable Long id,
                                              @RequestBody ItemCarritoDTO dto,
                                              @RequestHeader("Authorization") String authHeader) {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          return ResponseEntity.status(401).body("Token no válido");
      }

      String token = authHeader.substring(7);
      String correo;
      try {
          correo = jwtService.extractEmail(token);
      } catch (Exception e) {
          return ResponseEntity.status(401).body("Token inválido o expirado");
      }

      PrendaCarrito prendaCarrito = servicio.obtenerPorId(id);
      if (prendaCarrito == null || !prendaCarrito.getCorreo().equals(correo)) {
          return ResponseEntity.status(404).body("Elemento no encontrado o no pertenece al usuario");
      }

      prendaCarrito.setCantidad(dto.getCantidad());
      servicio.guardarPedidoCarrito(prendaCarrito);

      return ResponseEntity.ok().build();
  }

  @Transactional
  @DeleteMapping
  public ResponseEntity<?> eliminarTodoElCarrito(@RequestHeader("Authorization") String authHeader) {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          return ResponseEntity.status(401).body("Token no válido");
      }

      String token = authHeader.substring(7);
      String correo;
      try {
          correo = jwtService.extractEmail(token);
      } catch (Exception e) {
          return ResponseEntity.status(401).body("Token inválido o expirado");
      }

      Usuario usuario = usuarioRepository.findByEmail(correo).orElse(null);
      if (usuario == null) {
          return ResponseEntity.status(404).body("Usuario no encontrado");
      }

      servicio.vaciarCarritoPorCorreo(correo);
      return ResponseEntity.ok("Carrito eliminado correctamente");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
      PrendaCarrito prenda = servicio.obtenerPorId(id);
      if (prenda == null) {
          return ResponseEntity.notFound().build();
      }

      servicio.eliminarPorId(id);
      return ResponseEntity.noContent().build();
  }
}
