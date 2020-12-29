package com.rmarcello.starevent.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.rmarcello.starevent.client.Event;
import com.rmarcello.starevent.client.EventsProxy;
import com.rmarcello.starevent.client.exception.EventNotFoundException;
import com.rmarcello.starevent.model.Reservation;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReservationService {

    private final static Logger LOGGER = Logger.getLogger(ReservationService.class);


    //local simple database
    private Map<Long, Reservation> eventMap = new HashMap<Long, Reservation>();
    
    @Inject
    @RestClient
    EventsProxy eventClient;

	public Optional<Reservation> getReservationById(Long id) {
		return Optional.ofNullable( eventMap.get(id) );
	}

	public @Valid Reservation persistReservation(@Valid Reservation event) {
        
        Response resp = eventClient.getEvent(event.getEventId());
        LOGGER.info("eventStatus = "+resp.getStatus());
        
        Event e = resp.readEntity(Event.class);
        LOGGER.info("e = "+e);

        eventMap.put( event.getId() , event);
        return eventMap.get(event.getId());
        
        
        
	}

}
