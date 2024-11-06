package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.Categoria;
import com.example.cleanperfectback.Services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addCategoria(@RequestParam("nombreCategoria") String nombre) {
        System.out.println(nombre);
        //falta el tamaño
        Categoria cat = new Categoria();
        cat.setNombre(nombre);
        //boolean result = categoriaService.saveCategoria(cat);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }




}
