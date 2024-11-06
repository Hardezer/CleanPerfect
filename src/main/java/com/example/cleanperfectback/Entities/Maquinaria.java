package com.example.cleanperfectback.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maquinaria")
public class Maquinaria extends ItemSalida {
    private String nombre;
    private int costoUso;
    private String estado;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaSalida;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaReingreso;
    private Long empresa;

    @Override
    public String getNombre() {
        return nombre;
    }
}

