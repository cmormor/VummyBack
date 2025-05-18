package com.proyecto.integrado.vummy.dto;

import lombok.Data;

@Data
public class PedidoPrendaDTO {
    private PrendaInfo prenda;
    private TallaInfo talla;
    private Integer cantidad;

    @Data
    public static class PrendaInfo {
        private Long id;
        private String nombre;
        private Double precio;
    }

    @Data
    public static class TallaInfo {
        private Long id;
        private String nombre;
    }
}