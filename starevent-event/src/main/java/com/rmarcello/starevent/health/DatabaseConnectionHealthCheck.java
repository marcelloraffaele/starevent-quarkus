package com.rmarcello.starevent.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.rmarcello.starevent.service.EventService;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DatabaseConnectionHealthCheck implements HealthCheck {
    
    @Inject
    EventService eventService;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Event Datasource connection health check");
        try {
            long cnt = eventService.countEvents();
            responseBuilder.withData("Number of events in the database", cnt).up();
        } catch (IllegalStateException e) {
            responseBuilder.down();
        }
        return responseBuilder.build();
    }
}
