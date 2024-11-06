package com.example.cleanperfectback.Controllers;

import com.example.cleanperfectback.Entities.Categoria;
import com.example.cleanperfectback.Entities.Formato;
import com.example.cleanperfectback.Entities.Producto;
import com.example.cleanperfectback.Entities.ProductoDTO;
import com.example.cleanperfectback.Services.CategoriaService;
import com.example.cleanperfectback.Services.FormatoService;
import com.example.cleanperfectback.Services.ProductoService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    //falta conectar a html
    @Autowired
    ProductoService productoService;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    FormatoService formatoService;

    //Caso 1, ambos existen
    @Data
    public static class DtoCaso1 {
        private String codigoBarra;
        private String costo;
        private String tipo;
        private String idCategoria;
        private String idFormato;

    }

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.findAllProductos();
    }

    @GetMapping("/insumo")
    public List<ProductoDTO> getInsumos() {
        return productoService.findAllProductos().stream()
                .filter(producto -> "insumo".equalsIgnoreCase(producto.getTipo()))
                .map(productoService::getProductoDetails)
                .collect(Collectors.toList());
    }

    @GetMapping("/insumos")
    public ModelAndView insumosVista() {
        return new ModelAndView("insumos");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoService.findProductoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        return productoService.findProductoById(id)
                .map(producto -> {
                    // Actualizar solo los campos que se han modificado
                    producto.setCodigoBarra(productoActualizado.getCodigoBarra());
                    producto.setCostoDeCompra(productoActualizado.getCostoDeCompra());
                    producto.setCantidad(productoActualizado.getCantidad());
                    // Si se proporciona un nuevo tipo, actualizarlo
                    if (productoActualizado.getTipo() != null) {
                        producto.setTipo(productoActualizado.getTipo());
                    }
                    // Si se proporciona una nueva categoría, actualizarla
                    if (productoActualizado.getCategoria() != null) {
                        producto.setCategoria(productoActualizado.getCategoria());
                    }
                    // Si se proporciona un nuevo formato, actualizarlo
                    if (productoActualizado.getFormato() != null) {
                        producto.setFormato(productoActualizado.getFormato());
                    }
                    return productoService.updateProducto(id, producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addCaso1")
    public ResponseEntity<?> addProductoCaso1(@RequestBody DtoCaso1 dto) {
        Producto producto = new Producto();
        if (dto.getCodigoBarra().isEmpty() || dto.getCosto().isEmpty() || dto.getTipo().isEmpty() || dto.getIdCategoria().isEmpty() || dto.getIdFormato().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan datos"));
        }
        if(productoService.existsByCodigoBarra(Long.valueOf(dto.getCodigoBarra()))){
            return ResponseEntity.badRequest().body(Map.of("message", "El código de barras ya existe"));
        }
        producto.setCodigoBarra(Long.valueOf(dto.getCodigoBarra()));
        producto.setTipo(dto.getTipo());
        producto.setCostoDeCompra(Integer.parseInt(dto.getCosto()));
        producto.setCategoria(Long.valueOf(dto.getIdCategoria()));
        producto.setCantidad(0);
        producto.setFormato(Long.valueOf(dto.getIdFormato()));
        productoService.saveProducto(producto);
        return ResponseEntity.ok().body(Map.of("message", "Producto creado con éxito"));

    }


    //Caso 2, existe categoria y se crea formato
    @Data
    public static class DtoCaso2 {
        private String codigoBarra;
        private String costo;
        private String tipo;
        private String idCategoria;
        private String unidad;
        private String tamano;
    }

    @PostMapping("/addCaso2")
    public ResponseEntity<?> addProductoCaso2(@RequestBody DtoCaso2 dto) {
        Producto producto = new Producto();
        if (dto.getCodigoBarra().isEmpty() || dto.getCosto().isEmpty() || dto.getTipo().isEmpty() || dto.getIdCategoria().isEmpty() || dto.getUnidad().isEmpty() || dto.getTamano().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan datos"));
        }
        if(productoService.existsByCodigoBarra(Long.valueOf(dto.getCodigoBarra()))){
            return ResponseEntity.badRequest().body(Map.of("message", "El código de barras ya existe"));
        }
        Long idCategoria = Long.valueOf(dto.getIdCategoria());
        producto.setCodigoBarra(Long.valueOf(dto.getCodigoBarra()));
        producto.setTipo(dto.getTipo());
        producto.setCostoDeCompra(Integer.parseInt(dto.getCosto()));
        producto.setCategoria(idCategoria);
        producto.setCantidad(0);
        Formato formato = new Formato();
        formato.setTamano(dto.getTamano());
        formato.setUnidadDeMedida(dto.getUnidad());
        formato.setCategoria(idCategoria);
        formato = formatoService.saveFormato(formato);
        producto.setFormato(formato.getId());
        productoService.saveProducto(producto);
        return ResponseEntity.ok().body(Map.of("message", "Producto creado con éxito"));
    }

    //Caso 3, se crea categoria y formato
    @Data
    public static class DtoCaso3 {
        private String codigoBarra;
        private String costo;
        private String tipo;
        private String nombreCategoria;
        private String unidad;
        private String tamano;
    }

    @PostMapping("/addCaso3")
    public ResponseEntity<?> addProductoCaso3(@RequestBody DtoCaso3 dto) {
        System.out.println(dto);
        Producto producto = new Producto();
        if (dto.getCodigoBarra().isEmpty() || dto.getCosto().isEmpty() || dto.getTipo().isEmpty() || dto.getNombreCategoria().isEmpty() || dto.getUnidad() == null || dto.getTamano().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan datos"));
        }
        if(productoService.existsByCodigoBarra(Long.valueOf(dto.getCodigoBarra()))){
            return ResponseEntity.badRequest().body(Map.of("message", "El código de barras ya existe"));
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombreCategoria());
        categoria = categoriaService.saveCategoria(categoria);
        producto.setCodigoBarra(Long.valueOf(dto.getCodigoBarra()));
        producto.setTipo(dto.getTipo());
        producto.setCostoDeCompra(Integer.parseInt(dto.getCosto()));
        producto.setCategoria(categoria.getId());
        producto.setCantidad(0);
        Formato formato = new Formato();
        formato.setTamano(dto.getTamano());
        formato.setUnidadDeMedida(dto.getUnidad());
        formato.setCategoria(categoria.getId());
        formato = formatoService.saveFormato(formato);
        producto.setFormato(formato.getId());
        productoService.saveProducto(producto);
        return ResponseEntity.ok().body(Map.of("message", "Producto creado con éxito"));
    }

    @PostMapping("/ingresar")
    public ResponseEntity<?> ingresarProducto(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        String cantidad = payload.get("cantidad");

        if (id == null || cantidad == null || id.isEmpty() || cantidad.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Faltan datos"));
        }
        if (!productoService.existsByCodigoBarra(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body(Map.of("message", "El producto no existe"));
        }
        if (!productoService.ingresarProducto(Long.valueOf(id), Integer.parseInt(cantidad))) {
            return ResponseEntity.badRequest().body(Map.of("message", "No se pudo ingresar el producto"));
        }
        return ResponseEntity.ok().body(Map.of("message", "Producto ingresado con éxito"));
    }

    @PutMapping("/{id}/cambiarCantidad")
    public ResponseEntity<?> cambiarCantidad(@PathVariable Long id) {
        boolean success = productoService.cambiarCantidad(id);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "Cantidad del producto cambio a Sin Stock"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "La cantidad del producto ya se encuentra Sin Stock"));
        }
    }

    @GetMapping("/verificarCantidadInsumo")
    public ResponseEntity<Map<String, Object>> verificarCantidadInsumo(
            @RequestParam Long codigoBarra,
            @RequestParam Long cantidad) {

        Optional<Producto> productoOpt = productoService.findByCodigoDeBarra(codigoBarra);
        Map<String, Object> response = new HashMap<>();

        if (productoOpt.isEmpty()) {
            response.put("disponible", false);
            response.put("cantidadDisponible", 0);
            return ResponseEntity.notFound().build();
        }

        Producto producto = productoOpt.get();
        boolean disponible = producto.getCantidad() >= cantidad;
        response.put("disponible", disponible);
        response.put("cantidadDisponible", producto.getCantidad());

        return ResponseEntity.ok(response);
    }






}
