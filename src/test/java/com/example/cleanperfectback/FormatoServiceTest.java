package com.example.cleanperfectback;

import com.example.cleanperfectback.Entities.Formato;
import com.example.cleanperfectback.Repositories.FormatoRepository;
import com.example.cleanperfectback.Services.FormatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FormatoServiceTest {

    @InjectMocks
    private FormatoService formatoService;

    @Mock
    private FormatoRepository formatoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveFormato() {
        Formato formato = new Formato();
        formato.setId(1L);
        formato.setCategoria(1L);
        formato.setTamano("10L");
        formato.setUnidadDeMedida("cm");


        when(formatoRepository.save(formato)).thenReturn(formato);

        Formato result = formatoService.saveFormato(formato);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("cm", result.getUnidadDeMedida());
        assertEquals("10L", result.getTamano());
        assertEquals(1L, result.getCategoria());
        verify(formatoRepository, times(1)).save(formato);
    }

    @Test
    void testFindAllFormatos() {
        Formato formato1 = new Formato();
        formato1.setId(1L);

        Formato formato2 = new Formato();
        formato2.setId(2L);

        List<Formato> formatos = Arrays.asList(formato1, formato2);

        when(formatoRepository.findAll()).thenReturn(formatos);

        List<Formato> result = formatoService.findAllFormatos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(formatoRepository, times(1)).findAll();
    }

    @Test
    void testFindFormatoByCategoria() {
        Long categoriaId = 1L;

        Formato formato1 = new Formato();
        formato1.setId(1L);
        formato1.setCategoria(categoriaId);

        Formato formato2 = new Formato();
        formato2.setId(2L);
        formato2.setCategoria(categoriaId);

        List<Formato> formatos = Arrays.asList(formato1, formato2);

        when(formatoRepository.findByCategoriaOrderByTamano(categoriaId)).thenReturn(formatos);

        List<Formato> result = formatoService.findFormatoByCategoria(categoriaId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(categoriaId, result.get(0).getCategoria());
        assertEquals(categoriaId, result.get(1).getCategoria());
        verify(formatoRepository, times(1)).findByCategoriaOrderByTamano(categoriaId);
    }
}
