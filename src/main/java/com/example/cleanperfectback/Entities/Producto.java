package com.example.cleanperfectback.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producto")
public class Producto extends ItemSalida {
    private int costoDeCompra;
    private int cantidad;
    private String tipo;
    private Long codigoBarra;
    private Long categoria;
    private Long formato;

    @Override
    public String getNombre() {
        return tipo;
    }

}