package com.proyecto.integrado.vummy.dto;

import lombok.Data;

@Data
public class PedidoPrendaDTO {
    private Long id;
    private PrendaInfo prenda;
    private TallaInfo talla;
    private PedidoInfo pedido;
    private Integer cantidad;

    @Data
    public static class PrendaInfo {
        private Long id;
        private String nombre;
        private String descripcion;
        private Double precio;
        private String tiendaNombre;
    }

    @Data
    public static class TallaInfo {
        private Long id;
        private String nombre;
    }

    @Data
    public static class PedidoInfo {
        private Long id;
        private String usuario;
    }
}