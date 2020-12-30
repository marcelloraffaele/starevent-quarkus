package com.rmarcello.starevent.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.test.Mock;

@Mock
@ApplicationScoped
@RestClient
public class MockEventsProxy implements EventsProxy {

    private HashMap<Long, Event> map = new HashMap<>();

    @Override
    public Event getEvent( Long id){
        ifNotExsistCreateIt(id);

        return map.get(id);
    }  
    
    @Override
    public Event reserve( Long id, Long amount ){
        ifNotExsistCreateIt(id);
        Event e = map.get(id);
        e.setAvailability( e.getAvailability()-1 );
        return e;
    }
    
    private void ifNotExsistCreateIt(Long id) {
        if( !map.containsKey(id) ) {
            Event e = createTestEvent(id.intValue());
            map.put(id, e);
        }
    }

    public static Event createTestEvent(int i) {
        Event e = new Event();
        e.setId( (long)i );
        e.setTitle("title " + i);
        e.setArtist( "artist_"+i );
        e.setDescription("description_"+i);
        e.setStartDate( LocalDateTime.now().plusDays((long)i) );
        e.setWhere("where_"+i);
        e.setAddress("address_"+i);
        e.setPrice( 100f + i );
        e.setAvailability( 100 + i);
        return e;
    }
    
}
