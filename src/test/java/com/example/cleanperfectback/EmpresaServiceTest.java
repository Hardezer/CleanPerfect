package com.example.cleanperfectback;

import com.example.cleanperfectback.Entities.Empresa;
import com.example.cleanperfectback.Repositories.EmpresaRepository;
import com.example.cleanperfectback.Services.EmpresaService;
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

public class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService empresaService;

    @Mock
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa 1");

        when(empresaRepository.findByNombre(empresa.getNombre())).thenReturn(null);
        when(empresaRepository.save(empresa)).thenReturn(empresa);

        boolean result = empresaService.saveEmpresa(empresa);

        assertTrue(result);
        verify(empresaRepository, times(1)).findByNombre(empresa.getNombre());
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    void testSaveEmpresaAlreadyExists() {
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa 1");

        when(empresaRepository.findByNombre(empresa.getNombre())).thenReturn(empresa);

        boolean result = empresaService.saveEmpresa(empresa);

        assertFalse(result);
        verify(empresaRepository, times(1)).findByNombre(empresa.getNombre());
        verify(empresaRepository, never()).save(any(Empresa.class));
    }

    @Test
    void testGetEmpresaByNombre() {
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa 1");

        when(empresaRepository.findByNombre("Empresa 1")).thenReturn(empresa);

        Empresa result = empresaService.getEmpresaByNombre("Empresa 1");

        assertNotNull(result);
        assertEquals("Empresa 1", result.getNombre());
        verify(empresaRepository, times(1)).findByNombre("Empresa 1");
    }

    @Test
    void testGetAllEmpresas() {
        Empresa empresa1 = new Empresa();
        Empresa empresa2 = new Empresa();
        List<Empresa> empresas = Arrays.asList(empresa1, empresa2);

        when(empresaRepository.findAll()).thenReturn(empresas);

        List<Empresa> result = empresaService.getAllEmpresas();

        assertEquals(2, result.size());
        verify(empresaRepository, times(1)).findAll();
    }

    @Test
    void testDeleteEmpresa() {
        Long id = 1L;

        when(empresaRepository.existsById(id)).thenReturn(true);

        boolean result = empresaService.deleteEmpresa(id);

        assertTrue(result);
        verify(empresaRepository, times(1)).existsById(id);
        verify(empresaRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteEmpresaNotFound() {
        Long id = 1L;

        when(empresaRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            empresaService.deleteEmpresa(id);
        });

        assertEquals("La empresa con la id " + id + " no existe", exception.getMessage());
        verify(empresaRepository, times(1)).existsById(id);
        verify(empresaRepository, never()).deleteById(id);
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Empresa empresa = new Empresa();
        empresa.setId(id);

        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresa));

        Optional<Empresa> result = empresaService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(empresaRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateEmpresa() {
        Long id = 1L;
        Empresa empresa = new Empresa();
        empresa.setId(id);
        empresa.setNombre("Updated Empresa");
        empresa.setEstado("Activo");

        Empresa existingEmpresa = new Empresa();
        existingEmpresa.setId(id);
        existingEmpresa.setNombre("Old Empresa");
        existingEmpresa.setEstado("Activo");

        // Configurar el mock para devolver un Optional con la empresa existente cuando findById es llamado
        when(empresaRepository.findById(id)).thenReturn(Optional.of(existingEmpresa));
        // Configurar el mock para devolver la empresa actualizada cuando save es llamado
        when(empresaRepository.save(any(Empresa.class))).thenReturn(existingEmpresa);

        // Llamar al método bajo prueba
        Empresa result = empresaService.updateEmpresa(id, empresa);

        // Verificaciones
        assertNotNull(result); // Asegurar que el resultado no es nulo
        assertEquals(id, result.getId()); // Asegurar que el id es el esperado
        assertEquals("Updated Empresa", result.getNombre()); // Asegurar que el nombre es el esperado
        verify(empresaRepository, times(1)).findById(id); // Verificar que findById fue llamado una vez
        verify(empresaRepository, times(1)).save(existingEmpresa); // Verificar que save fue llamado una vez
    }

    @Test
    void testUpdateEmpresaNotFound() {
        Long id = 1L;
        Empresa empresa = new Empresa();
        empresa.setNombre("Updated Empresa");
        empresa.setEstado("Activo");

        // Configurar el mock para devolver un Optional vacío cuando findById es llamado
        when(empresaRepository.findById(id)).thenReturn(Optional.empty());

        // Llamar al método bajo prueba y capturar la excepción
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            empresaService.updateEmpresa(id, empresa);
        });

        // Verificaciones
        assertEquals("El producto con la id " + id + " no existe", exception.getMessage()); // Verificar el mensaje de la excepción
        verify(empresaRepository, times(1)).findById(id); // Verificar que findById fue llamado una vez
        verify(empresaRepository, never()).save(any(Empresa.class)); // Verificar que save nunca fue llamado
    }
}