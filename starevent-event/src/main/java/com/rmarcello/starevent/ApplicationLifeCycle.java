package com.rmarcello.starevent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rmarcello.starevent.service.EventService;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;

@ApplicationScoped
public class ApplicationLifeCycle {

    private static final Logger LOGGER = Logger.getLogger(ApplicationLifeCycle.class);

    @Inject
    EventService eventService;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.debug("The application is starting with profile " + ProfileManager.getActiveProfile());
    }
 
    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.debug("The application is stopping...");
    }
    

}