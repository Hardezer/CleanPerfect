package com.example.cleanperfectback;

import com.example.cleanperfectback.Repositories.CategoriaRepository;
import com.example.cleanperfectback.Entities.Categoria;
import com.example.cleanperfectback.Services.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCategoria() {
        Categoria categoria = new Categoria();
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria result = categoriaService.saveCategoria(categoria);

        assertNotNull(result);
        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    void testGetAllCategorias() {
        Categoria categoria1 = new Categoria();
        Categoria categoria2 = new Categoria();
        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> result = categoriaService.getAllCategorias();

        assertEquals(2, result.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testDeleteCategoria() {
        Long id = 1L;
        when(categoriaRepository.existsById(id)).thenReturn(true);

        boolean result = categoriaService.deleteCategoria(id);

        assertTrue(result);
        verify(categoriaRepository, times(1)).existsById(id);
        verify(categoriaRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCategoriaNotFound() {
        Long id = 1L;
        when(categoriaRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            categoriaService.deleteCategoria(id);
        });

        assertEquals("La categoria con la id " + id + " no existe", exception.getMessage());
        verify(categoriaRepository, times(1)).existsById(id);
        verify(categoriaRepository, never()).deleteById(id);
    }
}
