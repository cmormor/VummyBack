package com.proyecto.integrado.vummy.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDTO {
    private Long prenda;
    private Long talla;
    private int cantidad;
}
