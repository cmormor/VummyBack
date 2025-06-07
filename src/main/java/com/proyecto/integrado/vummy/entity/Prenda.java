package com.proyecto.integrado.vummy.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prendas", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "nombre", "tienda_id" })
})
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tienda_id", nullable = false)
    @JsonBackReference
    private Tienda tienda;

    @OneToMany(mappedBy = "prenda", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PedidoPrenda> pedidoPrendas = new ArrayList<>();

    @Column(nullable = false)
    private Double precio;

    private String descripcion;

    @Column(nullable = false)
    private Long stock = 0L;

    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;
}
