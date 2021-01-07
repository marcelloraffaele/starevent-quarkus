package com.rmarcello.starevent.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.rmarcello.starevent.ReservationResource;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class PingEventResourceHealthCheck implements HealthCheck {

    @Inject
    ReservationResource resource;

    @Override
    public HealthCheckResponse call() {
        resource.ping();
        return HealthCheckResponse.named("Ping Reservation REST Endpoint").up().build();
    }
    
}
