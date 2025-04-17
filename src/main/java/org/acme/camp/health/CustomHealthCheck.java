package org.acme.camp.health;

import jakarta.inject.Inject;

import io.smallrye.health.api.Wellness;
import org.acme.camp.repository.CampRepository;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Wellness
public class CustomHealthCheck implements HealthCheck {
    @Inject
    CampRepository campRepository;

    @Override
    public HealthCheckResponse call() {
        long campCount = campRepository.findAll().count();
        boolean wellnessStatus = campCount > 0;
        return HealthCheckResponse.builder()
                .name("camp-count-check")
                .status(wellnessStatus)
                .withData("camp-count", campCount)
                .build();
    }
}
