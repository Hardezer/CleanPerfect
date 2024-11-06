package com.example.cleanperfectback;

import com.example.cleanperfectback.Entities.Ropa;
import com.example.cleanperfectback.Repositories.RopaRepository;
import com.example.cleanperfectback.Services.RopaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RopaServiceTest {

    @InjectMocks
    private RopaService ropaService;

    @Mock
    private RopaRepository ropaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRopa() {
        Ropa ropa = new Ropa();
        ropa.setNombre("Camisa");
        ropa.setTalla("M");

        when(ropaRepository.existsByNombreAndTalla("Camisa", "M")).thenReturn(false);
        when(ropaRepository.save(ropa)).thenReturn(ropa);

        boolean result = ropaService.saveRopa(ropa);

        assertTrue(result);
        verify(ropaRepository, times(1)).existsByNombreAndTalla("Camisa", "M");
        verify(ropaRepository, times(1)).save(ropa);
    }

    @Test
    void testSaveRopaAlreadyExists() {
        Ropa ropa = new Ropa();
        ropa.setNombre("Camisa");
        ropa.setTalla("M");

        when(ropaRepository.existsByNombreAndTalla("Camisa", "M")).thenReturn(true);

        boolean result = ropaService.saveRopa(ropa);

        assertFalse(result);
        verify(ropaRepository, times(1)).existsByNombreAndTalla("Camisa", "M");
        verify(ropaRepository, times(0)).save(ropa);
    }

    @Test
    void testActualizarCantidad() {
        Ropa ropa = new Ropa();
        ropa.setId(1L);
        ropa.setCantidad(10L);

        when(ropaRepository.findById(1L)).thenReturn(Optional.of(ropa));
        when(ropaRepository.save(ropa)).thenReturn(ropa);

        boolean result = ropaService.actualizarCantidad(1L, 5L);

        assertTrue(result);
        assertEquals(15L, ropa.getCantidad());
        verify(ropaRepository, times(1)).findById(1L);
        verify(ropaRepository, times(1)).save(ropa);
    }

    @Test
    void testActualizarCantidadRopaNotFound() {
        when(ropaRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = ropaService.actualizarCantidad(1L, 5L);

        assertFalse(result);
        verify(ropaRepository, times(1)).findById(1L);
        verify(ropaRepository, times(0)).save(any(Ropa.class));
    }

    @Test
    void testUpdateRopa() {
        Ropa ropa = new Ropa();
        ropa.setId(1L);
        ropa.setNombre("Camisa");

        when(ropaRepository.existsById(1L)).thenReturn(true);
        when(ropaRepository.save(ropa)).thenReturn(ropa);

        Ropa result = ropaService.updateRopa(1L, ropa);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ropaRepository, times(1)).existsById(1L);
        verify(ropaRepository, times(1)).save(ropa);
    }

    @Test
    void testUpdateRopaNotFound() {
        Ropa ropa = new Ropa();
        ropa.setId(1L);
        ropa.setNombre("Camisa");

        when(ropaRepository.existsById(1L)).thenReturn(false);

        Ropa result = ropaService.updateRopa(1L, ropa);

        assertNull(result);
        verify(ropaRepository, times(1)).existsById(1L);
        verify(ropaRepository, times(0)).save(ropa);
    }

    @Test
    void testDeleteRopa() {
        when(ropaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ropaRepository).deleteById(1L);

        ropaService.deleteRopa(1L);

        verify(ropaRepository, times(1)).existsById(1L);
        verify(ropaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRopaNotFound() {
        when(ropaRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            ropaService.deleteRopa(1L);
        });

        assertEquals("La ropa con la id 1 no existe", exception.getMessage());
        verify(ropaRepository, times(1)).existsById(1L);
        verify(ropaRepository, times(0)).deleteById(1L);
    }

    @Test
    void testFindAllRopas() {
        Ropa ropa1 = new Ropa();
        ropa1.setId(1L);

        Ropa ropa2 = new Ropa();
        ropa2.setId(2L);

        List<Ropa> ropas = List.of(ropa1, ropa2);

        when(ropaRepository.findAll()).thenReturn(ropas);

        List<Ropa> result = ropaService.findAllRopas();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ropaRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Ropa ropa = new Ropa();
        ropa.setId(1L);

        when(ropaRepository.findById(1L)).thenReturn(Optional.of(ropa));

        Ropa result = ropaService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ropaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(ropaRepository.findById(1L)).thenReturn(Optional.empty());

        Ropa result = ropaService.findById(1L);

        assertNull(result);
        verify(ropaRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorNombre() {
        Ropa ropa1 = new Ropa();
        ropa1.setNombre("Camisa");

        Ropa ropa2 = new Ropa();
        ropa2.setNombre("Camiseta");

        List<Ropa> ropas = List.of(ropa1, ropa2);

        when(ropaRepository.findByNombreContainingIgnoreCase("Cam")).thenReturn(ropas);

        List<Ropa> result = ropaService.buscarPorNombre("Cam");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ropaRepository, times(1)).findByNombreContainingIgnoreCase("Cam");
    }
}
