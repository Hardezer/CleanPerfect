package com.example.cleanperfectback.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaSalidasDTO {
    private Long empresaId;
    private String nombreEmpresa;
    private String estadoEmpresa;
    private List<SalidaDTO> salidas;
    private double costoTotal;
}