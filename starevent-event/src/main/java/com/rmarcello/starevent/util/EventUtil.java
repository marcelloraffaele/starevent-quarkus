package com.rmarcello.starevent.util;

import java.time.LocalDateTime;
import java.util.Random;

import com.github.javafaker.Faker;
import com.rmarcello.starevent.model.Event;

public class EventUtil {

    public static Event createTestEvent(int i) {
        String[] titlePart = new String[]{"in concert", "in Tour", "Unplagged", "in Arena"};
        Random r = new Random();

        Faker faker = new Faker();
        String artist = faker.name().fullName();

        Event e = new Event();
        e.setId( (long)i );
        e.setTitle(artist + " " + titlePart[r.nextInt(titlePart.length)] );
        e.setArtist( artist );
        e.setDescription( faker.lorem().paragraph() );
        e.setStartDate( LocalDateTime.now().plusDays((long)i) );
        e.setWhere( faker.address().city() );
        e.setAddress( faker.address().streetAddress() );
        e.setPrice( r.nextInt(10000)/100f );
        e.setAvailability( r.nextInt(1000));
        e.setImg("img/img_" + (i%3 +1)+".jpg" );
        return e;
    }
    
}
