package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Services.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;

@Controller
@RequestMapping("/file")
public class FileManagerController {
    @Autowired
    private FileManagerService fileManagerService;

    @GetMapping("/respaldo")
    public ResponseEntity<ByteArrayResource> respaldar() throws IOException {
        byte[] data = fileManagerService.respaldar();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=respaldo.xlsx")
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/cargar")
    public ResponseEntity<String> cargarDatosDesdeExcel(@RequestParam("file") MultipartFile file) {
        try {
            fileManagerService.cargarDatosDesdeExcel(file);
            return ResponseEntity.ok("Datos cargados exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al cargar datos, revise el archivo a subir.");
        }
    }
}
