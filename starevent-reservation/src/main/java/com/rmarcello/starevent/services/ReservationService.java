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
import com.rmarcello.starevent.repository.ReservationRepository;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class ReservationService {

    private final static Logger LOGGER = Logger.getLogger(ReservationService.class);

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    @RestClient
    EventsProxy eventClient;

    @Transactional(Transactional.TxType.SUPPORTS)
	public Optional<Reservation> getReservationById(Long id) {
		return reservationRepository.findByIdOptional(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
	public List<Reservation> getAllByUserId(String userId) {
		return reservationRepository.getAllByUserId(userId);
	}
    
    @Transactional(Transactional.TxType.SUPPORTS)
	public List<Reservation> getAll() {
		return reservationRepository.listAll();
	}

	public @Valid CreateReservationOut createReservation(@Valid CreateReservationIn req) {

        //call event service
		Event e = eventClient.getEvent(req.getEventId());
        LOGGER.debug("e = "+e);

        //call event service for event reservation, and availability decrement
        eventClient.reserve( e.getId(), 1L);

        Reservation reservation = new Reservation();
        reservation.setEventId( e.getId() );
        reservation.setUserId( req.getUserId() );
        reservation.setSecureCode( generateSecureCode() );
        reservation.setDate( LocalDateTime.now() );
        //persist
        reservationRepository.persist(reservation);

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

	public long countReservations() {
		return reservationRepository.count();
	}

}
