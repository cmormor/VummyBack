package com.proyecto.integrado.vummy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TiendaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private List<PrendaDTO> prendas;
    private List<TallaDTO> tallas;

    public TiendaDTO(Long id, String nombre, String descripcion, List<PrendaDTO> prendas, List<TallaDTO> tallas) {
      this.id = id;
      this.nombre = nombre;
      this.descripcion = descripcion;
      this.prendas = prendas;
      this.tallas = tallas;
  }

  public TiendaDTO(Long id, String nombre, String descripcion) {
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
}

}
