package com.example.cleanperfectback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleSalidaDTO {
    private Long itemSalidaId;
    private String itemSalidaNombre;
    private Long cantidad;
    private int costoTotal;

    // Getters y setters
}