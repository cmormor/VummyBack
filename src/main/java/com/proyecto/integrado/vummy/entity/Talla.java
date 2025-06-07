package com.proyecto.integrado.vummy.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tallas")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Talla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TallaNombre nombre;

    private Double altura;
    private Double cuelloManga;
    private Double pecho;
    private Double cintura;
    private Double cadera;
    private Double entrepierna;

    @ManyToOne
    @JoinColumn(name = "tienda_id", nullable = false)
    private Tienda tienda;

    @OneToMany(mappedBy = "talla", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PedidoPrenda> pedidoPrendas = new ArrayList<>();

    @JsonProperty("tienda")
    public TiendaInfo getTiendaInfo() {
        if (tienda != null) {
            return new TiendaInfo(tienda.getId(), tienda.getNombre());
        }
        return null;
    }

    public static class TiendaInfo {
        private Long id;
        private String nombre;

        public TiendaInfo(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
