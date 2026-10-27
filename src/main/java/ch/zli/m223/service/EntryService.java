package ch.zli.m223.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.model.Category;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import ch.zli.m223.repository.CategoryRepository;
import ch.zli.m223.repository.EntryRepository;
import ch.zli.m223.repository.TagRepository;

@ApplicationScoped
public class EntryService {
    @Inject
    private EntryRepository entryRepository;

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private TagRepository tagRepository;

    @Transactional
    public Entry createEntry(Entry entry, Long categoryId, List<Long> tagIds) {
        entry.setCategory(findCategory(categoryId));
        entry.setTags(findTags(tagIds));
        entryRepository.persist(entry);
        return entry;
    }

    @Transactional
    public Entry updateEntry(Long id, Entry entry, Long categoryId, List<Long> tagIds) {
        Entry existing = findById(id);
        existing.setCheckIn(entry.getCheckIn());
        existing.setCheckOut(entry.getCheckOut());
        existing.setCategory(findCategory(categoryId));
        existing.setTags(findTags(tagIds));
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

    public Entry findById(Long id) {
        return entryRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Entry with id " + id + " not found."));
    }

    private Category findCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findByIdOptional(categoryId)
            .orElseThrow(() -> new NotFoundException("Category with id " + categoryId + " not found."));
    }

    private Set<Tag> findTags(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Long> ids = new HashSet<>(tagIds);
        Set<Tag> tags = new HashSet<>(tagRepository.findByIds(ids));
        if (tags.size() != ids.size()) {
            throw new NotFoundException("One or more tags not found: " + tagIds);
        }
        return tags;
    }
}
