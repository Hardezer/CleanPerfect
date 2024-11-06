package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.Maquinaria;
import com.example.cleanperfectback.Services.EmpresaService;
import com.example.cleanperfectback.Services.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequestMapping("/maquinaria")
public class MaquinariaController {

    @Autowired
    private MaquinariaService maquinariaService;
    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<Maquinaria> getAll() {
        return maquinariaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maquinaria> getById(@PathVariable Long id) {
        return maquinariaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Maquinaria save(@RequestBody Maquinaria maquinaria) {
        maquinaria.setEstado("Disponible");
        return maquinariaService.save(maquinaria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Maquinaria> update(@PathVariable Long id, @RequestBody Maquinaria maquinaria) {

        return maquinariaService.findById(id)
                .map(existingMaquinaria -> {
                    if (maquinaria.getEstado() != null) {
                        if (maquinaria.getEstado().equals("Disponible") || maquinaria.getEstado().equals("No disponible")) {
                            maquinaria.setFechaSalida(null);
                            maquinaria.setFechaReingreso(null);
                            // Mantenemos el valor de la empresa existente
                            maquinaria.setEmpresa(existingMaquinaria.getEmpresa());
                        }
                    } else {
                        // Mantener la empresa si el estado no cambia a "Disponible" o "No disponible"
                        maquinaria.setEmpresa(existingMaquinaria.getEmpresa());
                    }

                    if (maquinaria.getEmpresa() == null) {
                        maquinaria.setEmpresa(existingMaquinaria.getEmpresa());
                    }

                    maquinaria.setId(id);
                    Maquinaria updatedMaquinaria = maquinariaService.save(maquinaria);
                    return ResponseEntity.ok(updatedMaquinaria);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maquinariaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stock")
    public ModelAndView maquinariaVista() {
        return new ModelAndView("maquinaria");
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Maquinaria> cambiarEstado(@PathVariable Long id) {
        return maquinariaService.cambiarEstado(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sugerencias")
    public List<Maquinaria> getSugerencias(@RequestParam String nombre) {
        System.out.println(nombre);
        List <Maquinaria> maquina  = maquinariaService.findByNombreAndEstado(nombre,"Disponible");
        System.out.println(maquina);
        return maquina;
    }

    @GetMapping("/sugerenciasE")
    public List<Map<String, Object>> getSugerenciasE(@RequestParam String nombre) {
        List<Maquinaria> maquinariaList = maquinariaService.findByNombreAndEstado(nombre, "En uso");
        List<Map<String, Object>> sugerencias = new ArrayList<>();
        for (Maquinaria maquinaria : maquinariaList) {
            Map<String, Object> sugerencia = new HashMap<>();
            sugerencia.put("id", maquinaria.getId());
            sugerencia.put("nombre", maquinaria.getNombre());
            sugerencia.put("fechaReingreso", maquinaria.getFechaReingreso());

            // Obtener el nombre de la empresa
            Optional<String> nombreEmpresa = empresaService.getNombreById(maquinaria.getEmpresa());
            sugerencia.put("empresa", nombreEmpresa.orElse("Desconocida"));

            sugerencias.add(sugerencia);
        }
        return sugerencias;
    }
}



