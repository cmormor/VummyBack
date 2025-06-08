package com.proyecto.integrado.vummy.controller;

import com.proyecto.integrado.vummy.entity.Pedido;
import com.proyecto.integrado.vummy.entity.PedidoPrenda;
import com.proyecto.integrado.vummy.entity.Usuario;
import com.proyecto.integrado.vummy.repository.PrendaRepository;
import com.proyecto.integrado.vummy.repository.TallaRepository;
import com.proyecto.integrado.vummy.repository.UsuarioRepository;
import com.proyecto.integrado.vummy.security.jwt.JwtService;
import com.proyecto.integrado.vummy.entity.EstadoPedido;
import com.proyecto.integrado.vummy.service.PedidoService;
import com.proyecto.integrado.vummy.dto.PedidoDTO;
import com.proyecto.integrado.vummy.dto.PedidoRequestDTO;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;
    private final PrendaRepository prendaRepository;
    private final TallaRepository tallaRepository;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    public PedidoController(PedidoService pedidoService, UsuarioRepository usuarioRepository,
            PrendaRepository prendaRepository, TallaRepository tallaRepository,
            JwtService jwtService, HttpServletRequest request) {
        this.pedidoService = pedidoService;
        this.usuarioRepository = usuarioRepository;
        this.prendaRepository = prendaRepository;
        this.tallaRepository = tallaRepository;
        this.jwtService = jwtService;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> obtenerTodos() {
        return ResponseEntity.ok(
                pedidoService.obtenerTodos().stream()
                        .map(pedidoService::toPedidoDTO)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPedidoPorId(id)
                .map(pedidoService::toPedidoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosUsuarioActual() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(
                pedidoService.obtenerPedidosPorUsuario(usuario.getId())
                        .stream()
                        .map(pedidoService::toPedidoDTO)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<PedidoDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(
                pedidoService.obtenerPedidosPorUsuario(usuarioId)
                        .stream()
                        .map(pedidoService::toPedidoDTO)
                        .collect(Collectors.toList()));
    }

    @PostMapping()
    public ResponseEntity<PedidoDTO> agregarPedido(@RequestBody PedidoRequestDTO dto,
            HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.CONFIRMADO);

        List<PedidoPrenda> prendas = dto.getPrendas().stream().map(item -> {
            PedidoPrenda pedidoPrenda = new PedidoPrenda();
            pedidoPrenda.setPedido(pedido);
            pedidoPrenda.setPrenda(
                    prendaRepository.findById(item.getPrenda().getId()).orElseThrow());
            pedidoPrenda.setTalla(
                    tallaRepository.findById(item.getTalla().getId()).orElseThrow());
            pedidoPrenda.setCantidad(item.getCantidad());
            return pedidoPrenda;
        }).toList();

        pedido.setPrendas(prendas);
        pedido.setTotal(
                prendas.stream()
                        .mapToDouble(p -> p.getPrenda().getPrecio() * p.getCantidad())
                        .sum());

        Pedido nuevoPedido = pedidoService.agregarPedido(pedido);
        return ResponseEntity.ok(pedidoService.toPedidoDTO(nuevoPedido));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> actualizarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        return pedidoService.actualizarEstado(id, estado)
                .map(pedidoService::toPedidoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/update-status")
    public ResponseEntity<String> actualizarEstadosAutomáticamente() {
        pedidoService.actualizarEstadosAutomáticamente();
        return ResponseEntity.ok("Estados actualizados correctamente.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
