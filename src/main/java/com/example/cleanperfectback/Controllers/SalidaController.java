package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.ItemSalida;
import com.example.cleanperfectback.Entities.Salida;
import com.example.cleanperfectback.Entities.StockDataDTO;
import com.example.cleanperfectback.Services.DetalleSalidaService;
import com.example.cleanperfectback.Services.SalidaService;
import com.example.cleanperfectback.Repositories.DetalleSalidaRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/salida")
public class SalidaController {

    @Autowired
    private SalidaService salidaService;
    @Autowired
    private DetalleSalidaService detalleSalidaService;
    @Autowired
    private DetalleSalidaRepository detalleSalidaRepository;

    @Data
    public static class SalidaDTO {
        private String nombreEmpresa;
        private List<DTO> detalle;
    }

    @Data
    public static class DTO {
        private String nombre;
        private String cantidad;
        private String idTalla;
        private LocalDate fechaSalida;
        private LocalDate fechaVuelta;
        private String tipoItem; // Añadido para identificar el tipo de item (Producto, Maquinaria, Ropa)
    }
    @Data
    public static class DTORespuesta{
        private Salida salida;
        private String msg;
    }

    @PostMapping("/")
    public ResponseEntity<DTORespuesta> createSalida(@RequestBody SalidaDTO salidaDTO) {
        String itemSalida = detalleSalidaService.revisarStock(salidaDTO.getDetalle());
        DTORespuesta dtoRespuesta = new DTORespuesta();
        if(itemSalida != null){
            dtoRespuesta.setMsg(itemSalida);
            return ResponseEntity.ok(dtoRespuesta);
        }
        Salida newSalida = salidaService.createSalida(salidaDTO.getNombreEmpresa(), 0);
        int totalCost = detalleSalidaService.createDetalleSalida(newSalida, salidaDTO.getDetalle());
        newSalida.setCosto(totalCost);
        salidaService.save(newSalida); // Asegurarse de que este método guarda la salida actualizada en la base de datos
        dtoRespuesta.setSalida(newSalida);
        return ResponseEntity.ok(dtoRespuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salida> getSalida(@PathVariable Long id) {
        Salida salida = salidaService.getSalida(id);
        return ResponseEntity.ok(salida);
    }

    @GetMapping("/{id}/costoTotal")
    public ResponseEntity<Integer> getTotalCostBySalidaId(@PathVariable Long id) {
        int totalCost = detalleSalidaService.getTotalCostBySalidaId(id);
        return ResponseEntity.ok(totalCost);
    }

    @GetMapping("/stockDataInsumos")
    public ResponseEntity<List<StockDataDTO>> getStockDataInsumos() {
        List<StockDataDTO> stockData = salidaService.getStockDataInsumos();
        return ResponseEntity.ok(stockData);
    }

    @GetMapping("/stockDataRopa")
    public ResponseEntity<List<StockDataDTO>> getStockDataRopa() {
        List<StockDataDTO> stockData = salidaService.getStockDataRopa();
        return ResponseEntity.ok(stockData);
    }

}
