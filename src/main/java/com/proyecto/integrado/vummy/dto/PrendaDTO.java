package com.proyecto.integrado.vummy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrendaDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private Long tallaId;  
    private Long tiendaId;
    private String tallaNombre;
    private String tiendaNombre;

    public PrendaDTO(Long id, String nombre, Double precio, Long tallaId, Long tiendaId, String tallaNombre, String tiendaNombre) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.tallaId = tallaId;
        this.tiendaId = tiendaId;
        this.tallaNombre = tallaNombre;
        this.tiendaNombre = tiendaNombre;
    }
}
