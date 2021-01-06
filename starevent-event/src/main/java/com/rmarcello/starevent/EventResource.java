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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.rmarcello.starevent.model.Event;
import com.rmarcello.starevent.service.EventService;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/events")
public class EventResource {

    private final static Logger LOGGER = Logger.getLogger(EventResource.class);

    @Inject
    EventService service;

    @Operation(summary = "Returns a random event")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "ping";
    }

    @Operation(summary = "Returns all the active events from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Event.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No events")
    @Counted(name = "countAllEvent", description = "Counts how many times the all Event method has been invoked")
    @Timed(name = "timeAllEvent", description = "Times how long it takes to invoke the all Event method", unit = MetricUnits.MILLISECONDS)
    @GET
    public Response all() {
        List<Event> allEvents = service.getAllActiveEvents();
        return Response.ok(allEvents).build();
    }

    @Operation(summary = "Returns a random event")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Event.class)))
    @Counted(name = "countGetRandomEvent", description = "Counts how many times the random Event method has been invoked")
    @GET
    @Path("/random")
    public Response getRandomEvent() {
        Event e = service.getRandomEvent();
        LOGGER.debug("Found random event " + e);
        return Response.ok(e).build();
    }

    @Operation(summary = "Returns a event for a given id")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Event.class)))
    @APIResponse(responseCode = "404", description = "The event is not found for the given id")
    @Counted(name = "countGetEvent", description = "Counts how many times the getEvent method has been invoked")
    @Timed(name = "timeGetEvent", description = "Times how long it takes to invoke the getEvent method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/{id}")
    public Response getEvent( @Parameter(description = "Event id", required = true) @PathParam("id") Long id) {
        Optional<Event> event = service.getEventById(id);
        if (event.isPresent()) {
            LOGGER.debug("Found event " + event);
            return Response.ok(event).build();
        } else {
            LOGGER.debug("No event found with id " + id);
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @Operation(summary = "Creates a valid event")
    @APIResponse(responseCode = "201", description = "The URI of the created event", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @Counted(name = "countCreateEvent", description = "Counts how many times the createEvent method has been invoked")
    @Timed(name = "timeCreateEvent", description = "Times how long it takes to invoke the createEvent method", unit = MetricUnits.MILLISECONDS)
    @POST
    public Response createEvent( @RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Event.class))) @Valid Event event, @Context UriInfo uriInfo) {
        event = service.persistEvent(event);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path( event.getId().toString() );        
        URI location = builder.build();
        LOGGER.debug("Event created:" + event + ", uri:" + location.toString());
        return Response.created(location).build();
    }

    @Operation(summary = "Updates an existing event")
    @APIResponse(responseCode = "200", description = "The updated event", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Event.class)))
    @Counted(name = "countUpdateEvent", description = "Counts how many times the updateEvent method has been invoked")
    @Timed(name = "timeUpdateEvent", description = "Times how long it takes to invoke the updateEvent method", unit = MetricUnits.MILLISECONDS)
    @PUT
    public Response updateEvent( @Valid Event event ) {
        Event answer = service.updateEvent(event);
        LOGGER.debug("Event updated with new valued " + answer);
        if(answer!=null) {
            return Response.ok(event).build();
        }
        //else
        return Response.status(Status.NOT_FOUND).build();
    }

    @Operation(summary = "Delete a event for a given id")
    @APIResponse(responseCode = "204", description = "The event has been successfully deleted")
    @APIResponse(responseCode = "404", description = "The event is not found for the given id")
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@Parameter(description = "Event id", required = true) @PathParam("id") Long id ) {
        service.deleteEvent(id);
        LOGGER.debug("Event deleted: " + id);
        return Response.noContent().build();
    }

    @Operation(summary = "Reserve an event for a given event id and a given amount of ticket. The amount of ticket will be removed from event availability.")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Event.class)))
    @APIResponse(responseCode = "404", description = "The event is not found for the given id")
    @Counted(name = "countReserveEvent", description = "Counts how many times the reserve event method has been invoked")
    @Timed(name = "timeReserveEvent", description = "Times how long it takes to invoke the reserve Event method", unit = MetricUnits.MILLISECONDS)
    @PUT
    @Path("/reserve/{id}/{amount}")
    public Response reserve( @Parameter(description = "Event id", required = true) @PathParam("id") Long id, 
        @Parameter(description = "ticket amount", required = true) @PathParam("amount") Long amount ) {

        Optional<Event> event = service.getEventById(id);
        if (!event.isPresent()) {
            LOGGER.debug("No event found with id " + id);
            return Response.status(Status.NOT_FOUND).build();
        }
        
        if ( event.get().getAvailability().intValue()<=0 ) {
            LOGGER.debug("no availability");
            return Response.status(Status.NOT_FOUND).build();
        }

        Event eventRest = service.reserve(id, amount);
        LOGGER.debug("Event reserved " + eventRest);
        return Response.ok(eventRest).build();
    }

}