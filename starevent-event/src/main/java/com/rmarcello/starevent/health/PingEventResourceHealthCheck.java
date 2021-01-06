package com.rmarcello.starevent.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.rmarcello.starevent.EventResource;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class PingEventResourceHealthCheck implements HealthCheck {

    @Inject
    EventResource er;

    @Override
    public HealthCheckResponse call() {
        er.ping();
        return HealthCheckResponse.named("Ping Event REST Endpoint").up().build();
    }
    
}
