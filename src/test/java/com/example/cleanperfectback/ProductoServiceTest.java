package com.example.cleanperfectback;

import com.example.cleanperfectback.Entities.*;
import com.example.cleanperfectback.Repositories.CategoriaRepository;
import com.example.cleanperfectback.Repositories.FormatoRepository;
import com.example.cleanperfectback.Repositories.ProductoRepository;
import com.example.cleanperfectback.Services.CategoriaService;
import com.example.cleanperfectback.Services.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    @InjectMocks
    private ProductoService productoService;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private FormatoRepository formatoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProducto() {
        Producto producto = new Producto();
        producto.setId(1L);

        when(productoRepository.save(producto)).thenReturn(producto);

        boolean result = productoService.saveProducto(producto);

        assertTrue(result);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testUpdateProducto() {
        Producto producto = new Producto();
        producto.setId(1L);

        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto result = productoService.updateProducto(1L, producto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productoRepository, times(1)).existsById(1L);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testDeleteProducto() {
        Long id = 1L;

        when(productoRepository.existsById(id)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(id);

        productoService.deleteProducto(id);

        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindProductoById() {
        Long id = 1L;
        Producto producto = new Producto();
        producto.setId(id);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoService.findProductoById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void testFindAllProductos() {
        Producto producto1 = new Producto();
        producto1.setId(1L);

        Producto producto2 = new Producto();
        producto2.setId(2L);

        List<Producto> productos = List.of(producto1, producto2);

        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> result = productoService.findAllProductos();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGetProductoDetails() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCategoria(1L);
        producto.setFormato(1L);
        producto.setCostoDeCompra(5000);
        producto.setCantidad(10);
        producto.setTipo("Tipo 1");
        producto.setCodigoBarra(123456789L);

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Categoria 1");

        Formato formato = new Formato();
        formato.setId(1L);
        formato.setTamano("500");
        formato.setUnidadDeMedida("ml");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(formatoRepository.findById(1L)).thenReturn(Optional.of(formato));

        ProductoDTO result = productoService.getProductoDetails(producto);

        assertNotNull(result);
        assertEquals("Categoria 1", result.getCategoria());
        assertEquals("500 ml", result.getFormato());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(formatoRepository, times(1)).findById(1L);
    }

    @Test
    void testRegistrarProductoConCategoria() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCodigoBarra(123456789L);
        producto.setCategoria(1L);

        Categoria categoria = new Categoria();
        categoria.setId(1L);

        when(productoRepository.findFirstByCodigoBarra(123456789L)).thenReturn(null);

        boolean result = productoService.registrarProductoConCategoria(producto, categoria);

        assertTrue(result);
        verify(productoRepository, times(1)).findFirstByCodigoBarra(123456789L);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testExistsByCodigoBarra() {
        Long codigoBarra = 123456789L;

        when(productoRepository.findFirstByCodigoBarra(codigoBarra)).thenReturn(new Producto());

        boolean result = productoService.existsByCodigoBarra(codigoBarra);

        assertTrue(result);
        verify(productoRepository, times(1)).findFirstByCodigoBarra(codigoBarra);
    }

    @Test
    void testIngresarProducto() {
        Long codigoBarra = 123456789L;
        Integer cantidad = 10;

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setCodigoBarra(codigoBarra);
        producto.setCantidad(20);

        when(productoRepository.findFirstByCodigoBarra(codigoBarra)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);

        boolean result = productoService.ingresarProducto(codigoBarra, cantidad);

        assertTrue(result);
        assertEquals(30, producto.getCantidad());
        verify(productoRepository, times(2)).findFirstByCodigoBarra(codigoBarra);
        verify(productoRepository, times(1)).save(producto);
    }


    @Test
    void actualizarStocks1() {
        Entry entry1 = new Entry();
        entry1.setIdProducto(1L);
        entry1.setCantidad(10L);

        Entry entry2 = new Entry();
        entry2.setIdProducto(2L);
        entry2.setCantidad(20L);

        List<Entry> entries = List.of(entry1, entry2);

        Producto producto1 = new Producto();
        producto1.setId(1L);
        producto1.setCantidad(100);

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setCantidad(200);

        when(productoRepository.findFirstByCodigoBarra(1L)).thenReturn(producto1);
        when(productoRepository.findFirstByCodigoBarra(2L)).thenReturn(producto2);
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);

        productoService.actualizarStocks(entries);

        assertEquals(110, producto1.getCantidad());
        assertEquals(220, producto2.getCantidad());
        verify(productoRepository, times(1)).findFirstByCodigoBarra(1L);
        verify(productoRepository, times(1)).findFirstByCodigoBarra(2L);
        verify(productoRepository, times(2)).save(any(Producto.class));
    }

    @Test
    void actualizar2() {
        Entry entry1 = new Entry();
        entry1.setIdProducto(1L);
        entry1.setCantidad(10L);

        Entry entry2 = new Entry();
        entry2.setIdProducto(2L);
        entry2.setCantidad(20L);

        List<Entry> entries = List.of(entry1, entry2);

        when(productoRepository.findFirstByCodigoBarra(anyLong())).thenReturn(null);

        productoService.actualizarStocks(entries);

        verify(productoRepository, times(2)).findFirstByCodigoBarra(anyLong());
        verify(productoRepository, times(0)).save(any(Producto.class));
    }
}
