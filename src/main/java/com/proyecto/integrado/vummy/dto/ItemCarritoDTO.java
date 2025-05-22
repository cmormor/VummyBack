package com.proyecto.integrado.vummy.dto;


import com.proyecto.integrado.vummy.entity.Prenda;
import com.proyecto.integrado.vummy.entity.Talla;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDTO {
    private String correo;
    private String token;
    private Prenda prenda;
    private Talla talla;
    private int cantidad;
}
