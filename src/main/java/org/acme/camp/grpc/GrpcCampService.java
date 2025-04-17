package org.acme.camp.grpc;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.camp.model.Hero;
import org.acme.camp.model.HeroResponse;
import org.acme.camp.model.InsertHeroRequest;
import org.acme.camp.model.CampService;
import org.acme.camp.model.RemoveHeroRequest;
import org.acme.camp.repository.CampRepository;

import java.util.Optional;

@GrpcService
public class GrpcCampService implements CampService {

    @Inject
    CampRepository campRepository;

    @Override
    @Blocking // ->> we need to use Blocking to
//    tell Quarkus that even if the methods return reactive types, we cannot execute them
//    on the eventloop thread because they run database I/O operations
    // just for demo purpose (reactive + normal data access),
    // the best way is using hibernate reactive panache without @Blocking
    public Multi<HeroResponse> add(Multi<InsertHeroRequest> requests) {
        return requests
            .map(request -> {
                Hero hero = new Hero();
                hero.setName(request.getName());
                hero.setClasse(request.getClasse());
                return hero;
            }).onItem().invoke(hero -> {
                QuarkusTransaction.requiringNew().run( () -> {
                    campRepository.persist(hero);
                    Log.info("Persisting " + hero);
                });
            }).map(hero -> HeroResponse.newBuilder()
                .setName(hero.getName())
                .setClasse(hero.getClasse())
                .setId(hero.getId())
                .build());
    }

    @Override
    @Blocking
    @Transactional
    public Uni<HeroResponse> remove(RemoveHeroRequest request) {
        Optional<Hero> optionalHero = campRepository
            .findByNameOptional(
                request.getName());

        if (optionalHero.isPresent()) {
            Hero removedHero = optionalHero.get();
            campRepository.delete(removedHero);
            return Uni.createFrom().item(HeroResponse.newBuilder()
                .setName(removedHero.getName())
                .setClasse(removedHero.getClasse())
                .setId(removedHero.getId())
                .build());
        }
        return Uni.createFrom().nullItem();
    }
}
