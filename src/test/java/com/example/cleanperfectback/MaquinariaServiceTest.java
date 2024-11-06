package com.example.cleanperfectback;

import com.example.cleanperfectback.Entities.Maquinaria;
import com.example.cleanperfectback.Repositories.MaquinariaRepository;
import com.example.cleanperfectback.Services.MaquinariaService;
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

public class MaquinariaServiceTest {

    @InjectMocks
    private MaquinariaService maquinariaService;

    @Mock
    private MaquinariaRepository maquinariaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Maquinaria maquinaria1 = new Maquinaria();
        maquinaria1.setId(1L);

        Maquinaria maquinaria2 = new Maquinaria();
        maquinaria2.setId(2L);

        List<Maquinaria> maquinarias = Arrays.asList(maquinaria1, maquinaria2);

        when(maquinariaRepository.findAll()).thenReturn(maquinarias);

        List<Maquinaria> result = maquinariaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(maquinariaRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Maquinaria maquinaria = new Maquinaria();
        maquinaria.setId(id);

        when(maquinariaRepository.findById(id)).thenReturn(Optional.of(maquinaria));

        Optional<Maquinaria> result = maquinariaService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(maquinariaRepository, times(1)).findById(id);
    }

    @Test
    void testSave() {
        Maquinaria maquinaria = new Maquinaria();
        maquinaria.setId(1L);
        maquinaria.setNombre("Maquinaria 1");

        when(maquinariaRepository.save(maquinaria)).thenReturn(maquinaria);

        Maquinaria result = maquinariaService.save(maquinaria);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Maquinaria 1", result.getNombre());
        verify(maquinariaRepository, times(1)).save(maquinaria);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;

        doNothing().when(maquinariaRepository).deleteById(id);

        maquinariaService.deleteById(id);

        verify(maquinariaRepository, times(1)).deleteById(id);
    }
}
