package org.acme.camp.service;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.camp.model.Hero;
import org.acme.camp.repository.CampRepository;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;
import java.util.Optional;

@GraphQLApi
public class GraphQLCampService {

    @Inject
    CampRepository campRepository;

    @Query
    public List<Hero> heroes() {
        return campRepository.listAll();
    }

    @Transactional
    @Mutation
    public Hero register(Hero hero) {
        campRepository.persist(hero);
        Log.info("Persisting " + hero);
        return hero;
    }

    @Transactional
    @Mutation
    public boolean remove(String name) {
        Optional<Hero> toBeRemoved = campRepository
            .findByNameOptional(
                name);
        if(toBeRemoved.isPresent()) {
            campRepository.delete(toBeRemoved.get());
            return true;
        } else {
            return false;
        }
    }

}
