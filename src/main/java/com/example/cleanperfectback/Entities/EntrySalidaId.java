package com.example.cleanperfectback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrySalidaId implements Serializable {
    private Long idSalida;
    private Long idProducto;
    private int tipo;
}
