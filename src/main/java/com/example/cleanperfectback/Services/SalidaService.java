package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.Empresa;
import com.example.cleanperfectback.Entities.Maquinaria;
import com.example.cleanperfectback.Entities.Salida;
import com.example.cleanperfectback.Entities.StockDataDTO;
import com.example.cleanperfectback.Repositories.SalidaRepository;
import com.example.cleanperfectback.Repositories.DetalleSalidaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
@Service
public class SalidaService {

    @Autowired
    private SalidaRepository salidaRepository;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private DetalleSalidaRepository detalleSalidaRepository;

    @Transactional
    public Salida createSalida(String empresa, int total) {
        Empresa empresa1 = empresaService.getEmpresaByNombre(empresa);
        Salida salida = new Salida();
        salida.setEmpresa(empresa1);
        salida.setCosto(total);
        salida.setFecha(LocalDate.now());
        return salidaRepository.save(salida);
    }

    public List<Salida> getSalidasByEmpresaIdAndFechaBetween(Long empresaId, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return salidaRepository.findByEmpresaIdAndFechaBetween(empresaId, start, end);
    }

    public Salida getSalida(Long id) {
        return salidaRepository.findById(id).orElseThrow(() -> new RuntimeException("Salida not found"));
    }

    public List<Salida> getSalidasByEmpresaId(Long empresaId) {
        return salidaRepository.findByEmpresaId(empresaId);
    }

    public Salida save(Salida salida) {
        return salidaRepository.save(salida);
    }

    public Salida getSalidaById(Long salidaId) {
        return salidaRepository.findById(salidaId).orElseThrow(() -> new RuntimeException("Salida not found"));
    }

    public List<StockDataDTO> getStockDataInsumos() {
        List<Object[]> results = detalleSalidaRepository.findStockDataInsumos();
        List<StockDataDTO> stockDataList = new ArrayList<>();

        for (Object[] result : results) {
            StockDataDTO dto = new StockDataDTO();
            dto.setTipo((String) result[0]);
            dto.setTotalStock(((Number) result[1]).intValue());
            dto.setStockSalido(((Number) result[2]).intValue());
            dto.setTotalSinSalida(Math.max(((Number) result[3]).intValue(), 0)); // Ensure non-negative value
            dto.setNombreEmpresa((String) result[4]);
            stockDataList.add(dto);
        }

        return stockDataList;
    }

    public List<StockDataDTO> getStockDataRopa() {
        List<Object[]> results = detalleSalidaRepository.findStockDataRopa();
        List<StockDataDTO> stockDataList = new ArrayList<>();

        for (Object[] result : results) {
            StockDataDTO dto = new StockDataDTO();
            dto.setTipo((String) result[0]);
            dto.setTotalStock(((Number) result[1]).intValue());
            dto.setStockSalido(((Number) result[2]).intValue());
            dto.setTotalSinSalida(Math.max(((Number) result[3]).intValue(), 0)); // Ensure non-negative value
            dto.setNombreEmpresa((String) result[4]);
            stockDataList.add(dto);
        }

        return stockDataList;
    }


}
