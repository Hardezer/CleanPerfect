package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.Formato;
import com.example.cleanperfectback.Services.FormatoService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/formato")
public class FormatoController {

    @Autowired
    FormatoService formatoService;
    @Data
    public static class FormatoDto {
        private String tamano;
        private String unidadDeMedida;
        private String categoria;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFormato(@RequestBody FormatoDto formatoDto) {
        Formato formato = new Formato();
        if(formatoDto.getTamano().isEmpty() || formatoDto.getUnidadDeMedida().isEmpty() || formatoDto.getCategoria().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        formato.setTamano(formatoDto.getTamano());
        formato.setUnidadDeMedida(formatoDto.getUnidadDeMedida());
        formato.setCategoria(Long.parseLong(formatoDto.getCategoria()));
        formatoService.saveFormato(formato);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byCategoria/{categoria}")
    public ResponseEntity<?> getFormatoByCategoria(@PathVariable Long categoria) {
        return ResponseEntity.ok(formatoService.findFormatoByCategoria(categoria));
    }


}
