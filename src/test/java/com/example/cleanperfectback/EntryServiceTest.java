package com.example.cleanperfectback;

import com.example.cleanperfectback.Entities.Entry;
import com.example.cleanperfectback.Repositories.EntryRepository;
import com.example.cleanperfectback.Services.EntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

public class EntryServiceTest {

    @InjectMocks
    private EntryService entryService;

    @Mock
    private EntryRepository entryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarEntries() {
        Entry entry1 = new Entry();
        entry1.setIdEntrada(1L);
        entry1.setIdProducto(1L);
        entry1.setCantidad(10L);

        Entry entry2 = new Entry();
        entry2.setIdEntrada(1L);
        entry2.setIdProducto(2L);
        entry2.setCantidad(20L);

        List<Entry> entries = List.of(entry1, entry2);

        entryService.guardarEntries(entries);

        verify(entryRepository, times(1)).saveAll(entries);
    }
}
