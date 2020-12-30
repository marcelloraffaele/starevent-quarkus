package com.rmarcello.starevent.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/events")
@RegisterProvider(EventsExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes("application/json")
@RegisterRestClient
public interface EventsProxy {

    @GET
    @Path("/{id}")
    public Event getEvent( @PathParam("id") Long id);
    
    @PUT
    @Path("/reserve/{id}/{amount}")
    public Event reserve( @PathParam("id") Long id, @PathParam("amount") Long amount );
    
}
