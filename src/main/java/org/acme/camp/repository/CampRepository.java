package org.acme.camp.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.camp.model.Hero;

import java.util.Optional;


@ApplicationScoped
public class CampRepository implements PanacheRepository<Hero> {

    public Optional<Hero> findByNameOptional(
        String name) {
        return find("name", name)
            .firstResultOptional();
    }
}
