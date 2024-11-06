package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.Empresa;
import com.example.cleanperfectback.Entities.Maquinaria;
import com.example.cleanperfectback.Entities.Producto;
import com.example.cleanperfectback.Services.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {
    @Autowired
    EmpresaService empresaService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addEmpresa(@RequestParam("nombreEmpresa") String nombre) {
        Empresa empresa = new Empresa();
        empresa.setNombre(nombre);
        boolean result = empresaService.saveEmpresa(empresa);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresas = empresaService.getAllEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        return empresaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        if (empresa.getEstado() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Empresa updatedEmpresa = empresaService.updateEmpresa(id, empresa);
        return ResponseEntity.ok(updatedEmpresa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        empresaService.deleteEmpresa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clientes")
    public ModelAndView clientesVista() {
        return new ModelAndView("clientes");
    }

    @PutMapping("/{id}/cambioEstado")
    public ResponseEntity<Void> cambioEstado(@PathVariable Long id) {
        boolean result = empresaService.cambioEstado(id);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Empresa>> getEmpresasActivas() {
        List<Empresa> empresasActivas = empresaService.getEmpresasActivas();
        return ResponseEntity.ok(empresasActivas);
    }
}
