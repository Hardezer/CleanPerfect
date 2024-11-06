package com.example.cleanperfectback.Services;

import com.example.cleanperfectback.Entities.Entry;
import com.example.cleanperfectback.Repositories.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryService {
    @Autowired
    private EntryRepository entryRepository;

    public void guardarEntries(List<Entry> entries) {
        entryRepository.saveAll(entries);
    }
  //  public void guardarEntry(Entry entry) {entryRepository.save(entry);    }
}
