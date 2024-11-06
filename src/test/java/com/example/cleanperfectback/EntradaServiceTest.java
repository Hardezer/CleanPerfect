package com.example.cleanperfectback;

import com.example.cleanperfectback.Controllers.EntradasController;
import com.example.cleanperfectback.Entities.Entrada;
import com.example.cleanperfectback.Entities.Entry;
import com.example.cleanperfectback.Entities.Ropa;
import com.example.cleanperfectback.Repositories.EntradaRepository;
import com.example.cleanperfectback.Services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EntradaServiceTest {

    @InjectMocks
    private EntradaService entradaService;

    @Mock
    private EntradaRepository entradaRepository;

    @Mock
    private EntryService entryService;

    @Mock
    private RopaService ropaService;

    @Mock
    private ProductoService productoService;

    @Mock
    private MaquinariaService maquinariaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarEntrada() {
        // Crear entrada
        Entrada entrada = new Entrada();
        entrada.setId(1L);

        // DTO de prueba
        List<EntradasController.DTO> entradas = new ArrayList<>();
        EntradasController.DTO dto1 = new EntradasController.DTO();
        dto1.setNombre("Insumo");
        dto1.setIdTalla("1");
        dto1.setCantidad("5");
        entradas.add(dto1);

        EntradasController.DTO dto2 = new EntradasController.DTO();
        dto2.setNombre("Camisa");
        dto2.setIdTalla("2");
        dto2.setCantidad("10");
        entradas.add(dto2);

        // Ropa de prueba
        Ropa ropa = new Ropa();
        ropa.setId(2L);
        ropa.setNombre("Camisa");

        // Mock de métodos
        when(entradaRepository.save(any(Entrada.class))).thenReturn(entrada);
        when(ropaService.findByNombreAndTalla("Camisa", "2")).thenReturn(ropa);

        // Ejecutar el método
        entradaService.registrarEntrada(entradas);

        // Verificar resultados
        verify(entryService, times(3)).guardarEntries(anyList()); // Cambiado a 3 veces
        verify(productoService, times(1)).actualizarStocks(anyList());
        verify(ropaService, times(1)).actualizarStocks(anyList());
        verify(maquinariaService, times(1)).actualizarStocks(anyList());
    }

    @Test
    void testCrearEntrada() {
        Entrada entradaMock = new Entrada();
        entradaMock.setId(1L);
        entradaMock.setFecha(LocalDate.now());

        when(entradaRepository.save(any(Entrada.class))).thenReturn(entradaMock);

        Entrada result = entradaService.crearEntrada();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDate.now(), result.getFecha());
        verify(entradaRepository, times(1)).save(any(Entrada.class));
    }
}
