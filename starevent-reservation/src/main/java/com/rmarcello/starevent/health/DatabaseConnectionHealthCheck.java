package com.rmarcello.starevent.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.rmarcello.starevent.services.ReservationService;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DatabaseConnectionHealthCheck implements HealthCheck {
    
    @Inject
    ReservationService reservationService;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Reservation Datasource connection health check");
        try {
            long cnt = reservationService.countReservations();
            responseBuilder.withData("Number of Reservation in the database", cnt).up();
        } catch (IllegalStateException e) {
            responseBuilder.down();
        }
        return responseBuilder.build();
    }
}
