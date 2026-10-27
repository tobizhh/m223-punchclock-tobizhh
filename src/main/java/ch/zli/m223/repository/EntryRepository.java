package ch.zli.m223.repository;

import java.util.List;

import ch.zli.m223.model.Category;
import ch.zli.m223.model.Entry;
import ch.zli.m223.model.Tag;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EntryRepository implements PanacheRepository<Entry> {

    public List<Entry> findByTag(Tag tag) {
        return list("select e from Entry e join e.tags t where t = ?1", tag);
    }

    public long removeCategory(Category category) {
        return update("category = null where category = ?1", category);
    }
}
