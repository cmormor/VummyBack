package com.proyecto.integrado.vummy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prendas")
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "talla_id", nullable = false)
    @JsonBackReference
    private Talla talla;

    @ManyToOne
    @JoinColumn(name = "tienda_id", nullable = false)
    @JsonBackReference
    private Tienda tienda;

    private Double precio;
}

