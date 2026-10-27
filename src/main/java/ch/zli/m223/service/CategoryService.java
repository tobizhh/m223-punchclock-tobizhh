package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.model.Category;
import ch.zli.m223.repository.CategoryRepository;
import ch.zli.m223.repository.EntryRepository;

@ApplicationScoped
public class CategoryService {
    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private EntryRepository entryRepository;

    @Transactional
    public Category createCategory(Category category) {
        categoryRepository.persist(category);
        return category;
    }

    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category existing = findById(id);
        existing.setTitle(category.getTitle());
        return existing;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category existing = findById(id);
        entryRepository.removeCategory(existing);
        categoryRepository.delete(existing);
    }

    public Category findById(Long id) {
        return categoryRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));
    }

    public List<Category> findAll() {
        return categoryRepository.listAll();
    }
}
