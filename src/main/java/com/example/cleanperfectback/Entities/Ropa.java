package com.example.cleanperfectback.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ropa")
public class Ropa extends ItemSalida {
    private String nombre;
    private String talla;
    private Long precio;
    private Long cantidad = 0L;

    @Override
    public String getNombre() {
        return nombre;
    }
}