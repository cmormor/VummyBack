package com.proyecto.integrado.vummy.dto;

import com.proyecto.integrado.vummy.entity.Talla;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TallaDTO {

    private Long id;
    private String nombre;
    private Double altura;
    private Double cuelloManga;
    private Double pecho;
    private Double cintura;
    private Double cadera;
    private Double entrepierna;
    private TiendaInfo tienda;

    public TallaDTO(Talla talla) {
        this.id = talla.getId();
        this.nombre = talla.getNombre().name();
        this.altura = talla.getAltura();
        this.cuelloManga = talla.getCuelloManga();
        this.pecho = talla.getPecho();
        this.cintura = talla.getCintura();
        this.cadera = talla.getCadera();
        this.entrepierna = talla.getEntrepierna();
        if (talla.getTienda() != null) {
            this.tienda = new TiendaInfo(talla.getTienda().getId(), talla.getTienda().getNombre());
        }
    }

    @Data
    @NoArgsConstructor
    public static class TiendaInfo {
        private Long id;
        private String nombre;

        public TiendaInfo(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }
}