package com.rmarcello.starevent.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped
@RestClient
public class MockEventsProxy implements EventsProxy {

    @Override
    public Response getEvent(Long id) {
        return Response.ok().entity(createTestEvent(1)).build();
    }

    @Override
    public Response updateEvent(Event event) {
        return Response.ok().entity(event).build();
    }
    
    public static Event createTestEvent(int i) {
        Event e = new Event();
        e.setId( (long)i );
        e.setTitle("title " + i);
        e.setArtist( "artist_"+i );
        e.setDescription("description_"+i);
        //e.setStartDate( LocalDateTime.now().plusDays((long)i) );
        e.setStartDate( LocalDateTime.now().plusDays((long)i).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) );
        e.setWhere("where_"+i);
        e.setAddress("address_"+i);
        e.setPrice( 100f + i );
        e.setAvailability( 100 + i);
        return e;
    }
    
}
