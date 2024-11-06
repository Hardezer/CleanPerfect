package com.example.cleanperfectback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalidaDTO {
    private Long salidaId;
    private LocalDate fecha;
    private int costo;
    private List<DetalleSalidaDTO> detallesSalida;
}