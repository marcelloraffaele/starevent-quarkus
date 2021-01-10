package com.rmarcello.starevent;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.rmarcello.starevent.beans.CreateReservationIn;
import com.rmarcello.starevent.beans.CreateReservationOut;
import com.rmarcello.starevent.client.exception.EventNotFoundException;
import com.rmarcello.starevent.model.Reservation;
import com.rmarcello.starevent.services.ReservationService;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;

@Path("/api/reservation")
public class ReservationResource {

    private final static Logger LOGGER = Logger.getLogger(ReservationResource.class);

    @Inject
    ReservationService service;

    @GET
    public Response getAll() {
        List<Reservation> l = service.getAll();
        return Response.ok(l).build();
    }

    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "ping";
    }

    @GET
    @Path("/{id}")
    @Timed(name = "ReservationResource_getReservation", absolute = true, description = "Times how long it takes to invoke the getReservation method", unit = MetricUnits.MILLISECONDS)
    public Response getReservation(@PathParam("id") Long id) {
        Optional<Reservation> reservation = service.getReservationById(id);
        if (reservation.isPresent()) {
            LOGGER.debug("Found reservation " + reservation);
            return Response.ok(reservation).build();
        } else {
            LOGGER.debug("No reservation found with id " + id);
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/user/{userId}")
    @Timed(name = "ReservationResource_getReservationByUserId", absolute = true, description = "Times how long it takes to invoke the getReservationByUserId method", unit = MetricUnits.MILLISECONDS)
    public Response getReservationByUserId(@PathParam("userId") String userId) {
        List<Reservation> list = service.getAllByUserId(userId);
        return Response.ok(list).build();
    }

    @Timeout(value = 5000, unit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "fallbackCreateReservation")
    @CircuitBreaker(successThreshold = 10, requestVolumeThreshold = 4, failureRatio=0.5, delay = 1000)
    @Timed(name = "ReservationResource_createReservation", absolute = true, description = "Times how long it takes to invoke the createReservation method", unit = MetricUnits.MILLISECONDS)
    @POST
    public Response createReservation(@Valid CreateReservationIn req, @Context UriInfo uriInfo) {
        LOGGER.info("createReservation - START - req=" + req);
        try {
            CreateReservationOut resp = service.createReservation(req);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(resp.getId().toString());
            URI location = builder.build();
            LOGGER.debug("Reservetion created:" + resp + ", uri:" + location.toString());
            return Response.created(location).entity(resp).build();
        } catch ( EventNotFoundException e) {
            LOGGER.info("event not found");
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    public Response fallbackCreateReservation(CreateReservationIn req, UriInfo uriInfo) {
        LOGGER.info("fallbackCreateReservation: " + req );
        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }
    


}