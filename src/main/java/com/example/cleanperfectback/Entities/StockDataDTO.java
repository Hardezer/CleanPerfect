package com.example.cleanperfectback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class StockDataDTO {
    private String tipo;
    private int totalStock;
    private int stockSalido;
    private int totalSinSalida;
    private String nombreEmpresa;
}
