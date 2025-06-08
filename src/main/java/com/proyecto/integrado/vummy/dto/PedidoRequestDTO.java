package com.proyecto.integrado.vummy.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {
    private List<ItemPedidoDTO> prendas;

    @Data
    public static class ItemPedidoDTO {
        private PrendaRef prenda;
        private TallaRef talla;
        private int cantidad;

        @Data
        public static class PrendaRef {
            private Long id;
        }

        @Data
        public static class TallaRef {
            private Long id;
        }
    }
}
