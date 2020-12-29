package com.rmarcello.starevent;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.rmarcello.starevent.model.Event;
import com.rmarcello.starevent.service.EventService;

import org.jboss.logging.Logger;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/events")
public class EventResource {

    private final static Logger LOGGER = Logger.getLogger(EventResource.class);

    @Inject
    EventService service;

    @GET
    public Response all() {
        List<Event> allEvents = service.getAllActiveEvents();
        return Response.ok(allEvents).build();
    }

    @GET
    @Path("/random")
    public Response getRandomBook() {
        Event e = service.getRandomEvent();
        LOGGER.debug("Found random event " + e);
        return Response.ok(e).build();
    }

    @GET
    @Path("/{id}")
    public Response getEvent( @PathParam("id") Long id) {
        Optional<Event> event = service.getEventById(id);
        if (event.isPresent()) {
            LOGGER.debug("Found event " + event);
            return Response.ok(event).build();
        } else {
            LOGGER.debug("No event found with id " + id);
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @POST
    public Response createEvent( @Valid Event event, @Context UriInfo uriInfo) {
        
        event = service.persistEvent(event);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path( event.getId().toString() );        
        URI location = builder.build();
        LOGGER.debug("Event created:" + event + ", uri:" + location.toString());
        return Response.created(location).build();
    }

    @PUT
    public Response updateEvent( @Valid Event event ) {
        event = service.updateEvent(event);
        LOGGER.debug("Event updated with new valued " + event);
        return Response.ok(event).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook( @PathParam("id") Long id ) {
        service.deleteEvent(id);
        LOGGER.debug("Event deleted with " + id);
        return Response.noContent().build();
    }


}