package com.rmarcello.starevent.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;

import com.rmarcello.starevent.beans.CreateReservationIn;
import com.rmarcello.starevent.beans.CreateReservationOut;
import com.rmarcello.starevent.client.Event;
import com.rmarcello.starevent.client.EventsProxy;
import com.rmarcello.starevent.model.Reservation;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ReservationService {

    private final static Logger LOGGER = Logger.getLogger(ReservationService.class);


    //local simple database
    private Map<Long, Reservation> reservationMap = new HashMap<Long, Reservation>();
    private long currentId = 1;

    @Inject
    @RestClient
    EventsProxy eventClient;

	public Optional<Reservation> getReservationById(Long id) {
		return Optional.ofNullable( reservationMap.get(id) );
	}

	public @Valid CreateReservationOut createReservation(@Valid CreateReservationIn req) {

        //call event service
		Event e = eventClient.getEvent(req.getEventId());
        LOGGER.debug("e = "+e);

        Reservation reservation = new Reservation();
        reservation.setId( currentId );
        currentId++;
        reservation.setEventId( e.getId() );
        reservation.setUserId( req.getUserId() );
        reservation.setSecureCode( generateSecureCode() );
        reservation.setDate( LocalDateTime.now() );
        //persist
        reservationMap.put( reservation.getId() , reservation);

        //call event service for event reservation, and availability decrement
        eventClient.reserve( e.getId(), 1L);

        CreateReservationOut resp = new CreateReservationOut();
        resp.setId( reservation.getId() );
        resp.setEventId( reservation.getEventId() );
        resp.setUserId( reservation.getUserId() );
        resp.setSecureCode( reservation.getSecureCode() );
        resp.setDate( reservation.getDate() );
        return resp;
	}

    private String generateSecureCode() {
        Random r = new Random();
        final String possibleChars = "ABCDEFGHILMNOPQRSTUVZWY1234567890";
        String code = IntStream.range(0, 15).boxed()
            .map( i-> possibleChars.charAt(r.nextInt(possibleChars.length()))+"")
            .collect( Collectors.joining() );
        return code;
    }

}
