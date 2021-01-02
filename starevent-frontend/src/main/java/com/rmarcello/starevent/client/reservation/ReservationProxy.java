package com.rmarcello.starevent.client.reservation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.rmarcello.starevent.client.EventsExceptionMapper;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/reservation")
@RegisterProvider(EventsExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes("application/json")
@RegisterRestClient
public interface ReservationProxy {

    @POST
    public CreateReservationOut createReservation( CreateReservationIn req );
    
}
