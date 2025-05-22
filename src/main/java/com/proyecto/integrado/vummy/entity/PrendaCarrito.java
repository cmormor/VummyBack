package com.proyecto.integrado.vummy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prenda_carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrendaCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correo;

    @ManyToOne
    @JoinColumn(name = "prenda_id")
    private Prenda prenda;

    @ManyToOne
    @JoinColumn(name = "talla_id")
    private Talla talla;

    private int cantidad;
}