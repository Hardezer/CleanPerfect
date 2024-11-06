package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.Entry;
import com.example.cleanperfectback.Services.EntradaService;
import lombok.Data;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/entradas")
public class EntradasController {

    @Autowired
    EntradaService entradaService;

    @Data
    public static class DTO {
        private String nombre;
        private String cantidad;
        private String idTalla;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> addEntrada(@RequestBody Map<String, List<DTO>> payload) {
        System.out.println(payload);
        List<DTO> entradas = payload.get("productos");
        entradaService.registrarEntrada(entradas);
        return ResponseEntity.ok(true);
    }

}
