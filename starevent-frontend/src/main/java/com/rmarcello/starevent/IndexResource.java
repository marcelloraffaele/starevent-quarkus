package com.rmarcello.starevent;

import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.rmarcello.starevent.client.event.Event;
import com.rmarcello.starevent.client.event.EventsProxy;
import com.rmarcello.starevent.client.reservation.CreateReservationIn;
import com.rmarcello.starevent.client.reservation.CreateReservationOut;
import com.rmarcello.starevent.client.reservation.ReservationProxy;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/")
public class IndexResource {

    @Inject
    Template index; 

    @Inject
    Template moreinfo;

    @Inject
    Template reserve; 

    @Inject
    Template error; 

    @Inject
    @RestClient
    EventsProxy eventClient;

    @Inject
    @RestClient
    ReservationProxy reservationClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Timeout(value = 5000, unit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "fallbackIndex")
    public TemplateInstance index() {
        Event randomEvent = eventClient.getRandomEvent();
        List<Event> eventList = eventClient.all();
        return index
                .data("eventList", eventList )
                .data("randomEvent", randomEvent);  
    }

    public TemplateInstance fallbackIndex() {
        return genericError();
    }

    @GET
    @Path("/moreinfo/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Timeout(value = 5000, unit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "fallbackMoreinfo")
    public TemplateInstance moreinfo(@PathParam("id") Long id) {
        Event event = eventClient.getEvent(id);
        return moreinfo.data("event", event );  
    }

    public TemplateInstance fallbackMoreinfo(Long id) {
        return genericError();
    }

    @GET
    @Path("/reserve/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Timeout(value = 5000, unit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "fallbackReserve")
    public TemplateInstance reserve(@PathParam("id") Long id) {
        CreateReservationIn req = new CreateReservationIn();
        req.setEventId(id);
        req.setUserId("example.user@userworld.com");
        CreateReservationOut reservationOut = reservationClient.createReservation(req);
        Event event = eventClient.getEvent(reservationOut.getEventId());
        return reserve.data("reservationOut", reservationOut ).data("event", event );  
    }

    public TemplateInstance fallbackReserve(Long id) {
        return genericError();
    }

    public TemplateInstance genericError() {
        return error
                .data("errorTitle", "Sorry, It's not you. It's us" )
                .data("errorDescription", "We're experimenting internal server problem. Please try again later." );
    }
    
}