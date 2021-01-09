package com.rmarcello.starevent.util;

import java.time.LocalDateTime;
import java.util.Random;

import com.rmarcello.starevent.model.Event;

public class EventUtil {

	public static Event createTestEvent(boolean withId) {
		Random random = new Random();
        Event e = new Event();
        
        if(withId)
            e.setId((long)random.nextInt(100));
        e.setTitle( "title_" + random.nextInt(100) );
        e.setArtist( "artist_" + random.nextInt(100) );
        e.setDescription( "descr_" + random.nextInt(100) );
        e.setStartDate( LocalDateTime.now().plusDays((long) random.nextInt(100)) );
        e.setLocation( "location_" + random.nextInt(100) );;
        e.setAddress( "address_" + random.nextInt(100) );
        e.setPrice( 10.12f );
		e.setAvailability( 1000 );
		int i = random.nextInt(100);
        e.setImg("img/img_" + (i%3 +1)+".jpg" );
        return e;
	}
    
}
