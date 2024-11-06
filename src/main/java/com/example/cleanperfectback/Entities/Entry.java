package com.example.cleanperfectback.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entry")
@IdClass(Entry.class)
public class Entry implements Serializable {
    @Id
    private Long idEntrada;

    @Id
    private Long idProducto;
    private Long cantidad;

    @Id
    private int tipo;
    // 0 = producto
    // 1 = ropa
}
