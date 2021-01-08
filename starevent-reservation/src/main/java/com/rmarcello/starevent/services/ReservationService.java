package com.rmarcello.starevent.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.rmarcello.starevent.beans.CreateReservationIn;
import com.rmarcello.starevent.beans.CreateReservationOut;
import com.rmarcello.starevent.client.Event;
import com.rmarcello.starevent.client.EventsProxy;
import com.rmarcello.starevent.model.Reservation;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class ReservationService {

    private final static Logger LOGGER = Logger.getLogger(ReservationService.class);

    @Inject
    @RestClient
    EventsProxy eventClient;

    @Transactional(Transactional.TxType.SUPPORTS)
	public Optional<Reservation> getReservationById(Long id) {
        return Reservation.findByIdOptional(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
	public List<Reservation> getAllByUserId(String userId) {
		return Reservation.getAllByUserId(userId);
	}
    
    @Transactional(Transactional.TxType.SUPPORTS)
	public List<Reservation> getAll() {
		return Reservation.listAll();
	}

	public @Valid CreateReservationOut createReservation(@Valid CreateReservationIn req) {
        try {
            //call event service
            Event e = eventClient.getEvent(req.getEventId());

            //call event service for event reservation, and availability decrement
            eventClient.reserve( e.getId(), 1L);

            Reservation reservation = new Reservation();
            reservation.eventId= e.getId() ;
            reservation.userId = req.getUserId() ;
            reservation.secureCode = generateSecureCode() ;
            reservation.date = LocalDateTime.now() ;

            //persist
            Reservation.doPersist(reservation);

            CreateReservationOut resp = new CreateReservationOut();
            resp.setId( reservation.id );
            resp.setEventId( reservation.eventId );
            resp.setUserId( reservation.userId );
            resp.setSecureCode( reservation.secureCode );
            resp.setDate( reservation.date );
            return resp;
        } catch(Exception e ) {
            LOGGER.warn("wanring: " + e.getMessage(), e) ;
            throw e;
        }
	}

    public long countReservations() {
		return Reservation.count();
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
