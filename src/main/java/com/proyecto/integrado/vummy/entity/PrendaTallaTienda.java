package com.proyecto.integrado.vummy.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prenda_talla_tienda", uniqueConstraints = @UniqueConstraint(columnNames = { "prenda_id", "tienda_id",
        "talla_id" }))
public class PrendaTallaTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prenda_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ptt_prenda", foreignKeyDefinition = "FOREIGN KEY (prenda_id) REFERENCES prendas(id) ON DELETE CASCADE"))
    private Prenda prenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tienda_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ptt_tienda", foreignKeyDefinition = "FOREIGN KEY (tienda_id) REFERENCES tiendas(id) ON DELETE CASCADE"))
    private Tienda tienda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talla_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ptt_talla", foreignKeyDefinition = "FOREIGN KEY (talla_id) REFERENCES tallas(id) ON DELETE CASCADE"))
    private Talla talla;

    @Column(nullable = false)
    private Integer cantidad;
}