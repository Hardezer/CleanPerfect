package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.Ropa;
import com.example.cleanperfectback.Services.RopaService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/ropa")
public class RopaController {

    @Autowired
    RopaService ropaService;

    @RequestMapping("/get")
    public ResponseEntity<?> getRopa() {
        return ResponseEntity.ok(ropaService.findAllRopas());
    }

    @Data
    public static class DTO {
        // Cambiar el DTO como sea necesario para cuando hagan la conexion con el front
        private String nombre;
        private String talla;
        private String precio;
    }

    @RequestMapping("/add")
    public ResponseEntity<Boolean> addRopa(@RequestBody DTO dto) {
        Ropa ropa = new Ropa();
        ropa.setNombre(dto.getNombre());
        ropa.setTalla(dto.getTalla());
        ropa.setPrecio(Long.parseLong(dto.getPrecio()));
        return ResponseEntity.ok(ropaService.saveRopa(ropa));

    }

    @RequestMapping("/update")
    public ResponseEntity<?> updateRopa(@RequestBody Ropa ropa) {
        return ResponseEntity.ok(ropaService.updateRopa(ropa.getId(), ropa));
    }
    @GetMapping("/update/{id}")
    public ResponseEntity<Ropa> getRopaById(@PathVariable Long id) {
        Ropa ropa = ropaService.findById(id);
        return ropa != null ? ResponseEntity.ok(ropa) : ResponseEntity.notFound().build();
    }

    @RequestMapping("/addStock")
    public ResponseEntity<Boolean> addStock(@RequestBody Map<String, String> body) {
        Long id = Long.parseLong(body.get("id"));
        Long cantidad = Long.parseLong(body.get("cantidad"));
        return ResponseEntity.ok(ropaService.actualizarCantidad(id, cantidad));
    }

    @GetMapping("/stock")
    public ModelAndView ropaVista() {
        return new ModelAndView("ropa");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ropaService.deleteRopa(id);
    }


    @GetMapping("/sugerencias")
    public ResponseEntity<List<Ropa>> obtenerSugerencias(@RequestParam String nombre, @RequestParam(required = false) String talla) {
        List<Ropa> sugerencias;
        if (talla != null && !talla.isEmpty()) {
            sugerencias = ropaService.buscarPorNombreYTalla(nombre, talla);
        } else {
            sugerencias = ropaService.buscarPorNombre(nombre);
        }
        return ResponseEntity.ok(sugerencias);
    }

    @PutMapping("/{id}/cambiaCantidad")
    public ResponseEntity<?> cambiaCantidad(@PathVariable Long id) {
        boolean success = ropaService.cambiaCantidad(id);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "Cantidad del producto cambio a Sin Stock"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "La cantidad del producto ya se encuentra Sin Stock"));
        }
    }

    @GetMapping("/verificarCantidad")
    public ResponseEntity<Map<String, Object>> verificarCantidad(
            @RequestParam String nombre,
            @RequestParam String talla,
            @RequestParam Long cantidad) {

        Ropa ropa = ropaService.findByNombreAndTalla(nombre, talla);
        Map<String, Object> response = new HashMap<>();

        if (ropa == null) {
            response.put("disponible", false);
            response.put("cantidadDisponible", 0);
            return ResponseEntity.notFound().build();
        }

        boolean disponible = ropa.getCantidad() >= cantidad;
        response.put("disponible", disponible);
        response.put("cantidadDisponible", ropa.getCantidad());

        return ResponseEntity.ok(response);
    }


}
