package com.example.cleanperfectback.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detalle_salida")
public class DetalleSalida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "salida2_id")
    private Salida salida;

    @ManyToOne
    @JoinColumn(name = "item_salida_id")
    private ItemSalida itemSalida;

    private Long cantidad;
    private int costoTotal;
}
