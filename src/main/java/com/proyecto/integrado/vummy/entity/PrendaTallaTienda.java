package com.proyecto.integrado.vummy.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prenda_talla_tienda", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"prenda_id", "tienda_id", "talla_id"})
})
public class PrendaTallaTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prenda_id", nullable = false)
    private Prenda prenda;

    @ManyToOne
    @JoinColumn(name = "tienda_id", nullable = false)
    private Tienda tienda;

    @ManyToOne
    @JoinColumn(name = "talla_id", nullable = false)
    private Talla talla;

    @Column(nullable = false)
    private Integer cantidad;
}