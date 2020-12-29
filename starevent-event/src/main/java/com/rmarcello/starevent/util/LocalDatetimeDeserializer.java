package com.rmarcello.starevent.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.jboss.logging.Logger;

public class LocalDatetimeDeserializer implements JsonbDeserializer<LocalDateTime> {

    private static Logger LOGGER = Logger.getLogger(LocalDatetimeDeserializer.class);

    private final DateTimeFormatter dataFormatter;
    
    public LocalDatetimeDeserializer(DateTimeFormatter dataFormatter) {
        this.dataFormatter = dataFormatter;
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctx, java.lang.reflect.Type rtType) { 
        try {

            while (parser.hasNext()) {
                Event event = parser.next();
                LOGGER.debug("jsonevent: " + event);
                if (event == JsonParser.Event.KEY_NAME ) {
                    
                } else
                if (event == JsonParser.Event.VALUE_STRING ) {
                    // Deserialize name property                    
                    String str = parser.getString();
                    LOGGER.debug("deserializing: " + str);
                    LocalDateTime d = LocalDateTime.parse(str, dataFormatter);
                    return d;
                }
                parser.next(); // move to VALUE
            }

            return null;

            
        } catch(Exception e) {
            LOGGER.error("error: " + e, e);
            return null;
        }
    }

}