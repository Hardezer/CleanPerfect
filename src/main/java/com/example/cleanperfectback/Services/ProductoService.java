package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Controllers.EntradasController;
import com.example.cleanperfectback.Entities.*;
import com.example.cleanperfectback.Repositories.CategoriaRepository;
import com.example.cleanperfectback.Repositories.FormatoRepository;
import com.example.cleanperfectback.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    FormatoRepository formatoRepository;

    public boolean saveProducto(Producto producto) {
        productoRepository.save(producto);
        return true;
    }

    public String getNombreProductoCompleto(Long productoId) {
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new RuntimeException("Producto not found"));
        System.out.println(producto.getCategoria());
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(producto.getCategoria());
        System.out.println(categoriaOpt.get().getNombre());
        Optional<Formato> formatoOpt = formatoRepository.findById(producto.getFormato());


        String nombreCategoria = categoriaOpt.map(Categoria::getNombre).orElse("Sin categoría");
        String tamanoFormato = formatoOpt.map(Formato::getTamano).orElse("Sin tamaño");
        String unidadMedida = formatoOpt.map(Formato::getUnidadDeMedida).orElse("Sin unidad");

        return nombreCategoria + " " + tamanoFormato + " " + unidadMedida;
    }
    public Producto updateProducto(Long id, Producto producto) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalStateException("El producto con la id " + id + " no existe");
        }
        producto.setId(id);
        return productoRepository.save(producto);
    }

    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalStateException("El producto con la id " + id + " no existe");
        }
        productoRepository.deleteById(id);
    }

    public Optional<Producto> findProductoById(Long id) {
        return productoRepository.findById(id);
    }


    public Producto findProductoByCodigoBarra(Long codigoBarra) {
        return productoRepository.findFirstByCodigoBarra(codigoBarra);
    }

    public Optional<Producto> findByCodigoDeBarra(Long codigoBarra) {
        return productoRepository.findByCodigoBarra(codigoBarra);
    }

    public List<Producto> findAllProductos() {
        return productoRepository.findAll();
    }

    public ProductoDTO getProductoDetails(Producto producto) {
        Categoria categoria = categoriaRepository.findById(producto.getCategoria()).orElse(null);
        Formato formato = formatoRepository.findById(producto.getFormato()).orElse(null);

        return new ProductoDTO(
                producto.getId(),
                producto.getCostoDeCompra(),
                producto.getCantidad(),
                producto.getTipo(),
                producto.getCodigoBarra(),
                categoria != null ? categoria.getNombre() : "N/A",
                formato != null ? formato.getTamano() + " " + formato.getUnidadDeMedida() : "N/A"
        );
    }

    public Producto getProducto(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto not found"));
    }

    public boolean registrarProductoConCategoria(Producto producto, Categoria categoria){
        //Si la categoria tiene ID la categoria ya existe
        //Si la categoria no tiene ID puede o no ya existir
        //El codigo de barra del producto puede ya estar registrado



        producto.setCategoria(categoria.getId());
        //Ver que el codigo no estee ya registrado
        if(existsByCodigoBarra(producto.getCodigoBarra())){
            System.out.println("El codigo de barra ya esta registrado");
            return false;
        }else{
            System.out.println("El codigo de barra no esta registrado");
            productoRepository.save(producto);
            return true;
        }
    }

    public boolean existsByCodigoBarra(Long codigoBarra){
        return productoRepository.findFirstByCodigoBarra(codigoBarra) != null;
    }

    public boolean ingresarProducto(Long id, Integer cantidad) {
        if(existsByCodigoBarra(id)){
            Producto producto = productoRepository.findFirstByCodigoBarra(id);
            producto.setCantidad(producto.getCantidad() + cantidad);
            productoRepository.save(producto);
            return true;
        }
        return false;
    }

    public void actualizarStocks(List<Entry> entradas) {
        entradas.forEach(x -> {
            Producto producto = productoRepository.findFirstByCodigoBarra(x.getIdProducto());
            System.out.println(producto);
            if (producto == null) {
                return;
            }
            producto.setCantidad((int) (producto.getCantidad() + x.getCantidad()));
            productoRepository.save(producto);
        });
    }
    public void actualizarStocksSalida(List<EntrySalida> salidas) {
        salidas.forEach(x -> {
            Producto producto = productoRepository.findById(x.getId().getIdProducto()).orElse(null);
            if (producto != null) {
                producto.setCantidad((int) (producto.getCantidad() - x.getCantidad()));
                productoRepository.save(producto);
            }
        });
    }



    public boolean cambiarCantidad(Long id) {
        Optional<Producto> optionalProducto = findProductoById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();

            // Verificar si la cantidad ya es 0
            if (producto.getCantidad() == 0) {
                return false; // No hace nada si la cantidad ya es 0
            }

            producto.setCantidad(0);
            saveProducto(producto);
            return true;
        }
        return false;
    }

    public void updateStock(Producto producto, int i) {
        producto.setCantidad(producto.getCantidad() + i);
        productoRepository.save(producto);
    }
}
