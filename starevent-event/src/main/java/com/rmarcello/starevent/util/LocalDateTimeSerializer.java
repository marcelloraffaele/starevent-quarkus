package com.rmarcello.starevent.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

public class LocalDateTimeSerializer implements JsonbSerializer<LocalDateTime> {

    private final DateTimeFormatter dataFormatter;
    
    public LocalDateTimeSerializer(DateTimeFormatter dataFormatter) {
        this.dataFormatter = dataFormatter;
    }

    @Override
    public void serialize(LocalDateTime d, JsonGenerator generator, SerializationContext ctx) {

        String formatted = d.format(dataFormatter);     
        generator.write( formatted );

    }
}