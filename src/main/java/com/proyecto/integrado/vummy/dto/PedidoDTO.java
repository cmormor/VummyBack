package com.proyecto.integrado.vummy.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private UsuarioInfo usuario;
    private LocalDateTime fecha;
    private String estado;
    private Double total;
    private List<PedidoPrendaDTO> prendas;

    @Data
    public static class UsuarioInfo {
        private Long id;
        private String nombre;
    }
}