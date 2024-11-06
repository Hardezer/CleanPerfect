package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.DetalleSalida;
import com.example.cleanperfectback.Entities.Salida;
import com.example.cleanperfectback.Services.DetalleSalidaService;
import com.example.cleanperfectback.Services.PDFService;
import com.example.cleanperfectback.Services.SalidaService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    @Autowired
    private PDFService pdfService;

    @Autowired
    private SalidaService salidaService;

    @Autowired
    private DetalleSalidaService detalleSalidaService;

    @GetMapping("/generate/{salidaId}")
    public ResponseEntity<InputStreamResource> generatePDF(@PathVariable Long salidaId) {
        try {
            Salida salida = salidaService.getSalidaById(salidaId);
            List<DetalleSalida> detalles = detalleSalidaService.getDetallesBySalidaId(salidaId);

            ByteArrayInputStream bis = pdfService.generateSalidaPDF(salida, detalles);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=Salida_para_" + salida.getEmpresa().getNombre() + "_" + salida.getFecha() + ".pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(null);
        }
    }
}
