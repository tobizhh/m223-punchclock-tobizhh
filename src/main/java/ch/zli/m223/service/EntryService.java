package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.model.Entry;
import ch.zli.m223.repository.EntryRepository;

@ApplicationScoped
public class EntryService {
    @Inject
    private EntryRepository entryRepository;

    @Transactional
    public Entry createEntry(Entry entry) {
        entryRepository.persist(entry);
        return entry;
    }

    @Transactional
    public Entry updateEntry(Long id, Entry entry) {
        Entry existing = entryRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Entry with id " + id + " not found."));
        existing.setCheckIn(entry.getCheckIn());
        existing.setCheckOut(entry.getCheckOut());
        return existing;
    }

    @Transactional
    public void deleteEntry(Long id) {
        if (!entryRepository.deleteById(id)) {
            throw new NotFoundException("Entry with id " + id + " not found.");
        }
    }

    public List<Entry> findAll() {
        return entryRepository.listAll();
    }
}
