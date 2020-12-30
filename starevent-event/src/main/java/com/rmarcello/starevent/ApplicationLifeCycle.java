package com.rmarcello.starevent;

import java.util.stream.IntStream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.rmarcello.starevent.service.EventService;
import com.rmarcello.starevent.util.EventUtil;

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

        // only for test, create 10 events after start
        IntStream.range(0, 10)
            .forEach( i-> eventService.persistEvent(EventUtil.createTestEvent(i) ));

    }
 
    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.debug("The application is stopping...");
    }
    

}