package com.proyecto.integrado.vummy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrendaTallaTiendaDTO {
    private Long tallaId;
    private String tallaNombre;
    private Long tiendaId;
    private String tiendaNombre;
    private Integer cantidad;

}