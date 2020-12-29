package com.rmarcello.starevent.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.rmarcello.starevent.model.Event;

public class EventUtil {
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
