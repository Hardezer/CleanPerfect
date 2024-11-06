package com.example.cleanperfectback.Entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entry_salida")
public class EntrySalida implements Serializable {

    @EmbeddedId
    private EntrySalidaId id;

    private Long cantidad;
    private Double costoTotal;
}
