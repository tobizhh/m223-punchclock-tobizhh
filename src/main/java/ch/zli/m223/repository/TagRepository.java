package ch.zli.m223.repository;

import java.util.Collection;
import java.util.List;

import ch.zli.m223.model.Tag;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {

    public List<Tag> findByIds(Collection<Long> ids) {
        return list("id in ?1", ids);
    }
}
