package com.proyecto.integrado.vummy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrendaDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private Long stock;
    private Long tiendaId;
    private String tiendaNombre;
    private String imagen;

    public PrendaDTO(Long id, String nombre, Double precio, String descripcion, Long stock, Long tiendaId, String tiendaNombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = (stock != null) ? stock : 0L;         
        this.tiendaId = tiendaId;
        this.tiendaNombre = tiendaNombre;
        this.imagen = imagen;
    }
}
