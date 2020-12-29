package com.rmarcello.starevent.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;

import org.jboss.logging.Logger;

import io.quarkus.jsonb.JsonbConfigCustomizer;

//@Singleton
public class MyJsonbConfigCustomizer /*implements JsonbConfigCustomizer*/ {
    
    static final Logger LOGGER = Logger.getLogger(MyJsonbConfigCustomizer.class);
    public void customize(JsonbConfig config) {
        LOGGER.debug("SETUP customization");

        String datePattern = "dd/MM/yyyy HH:mm:ss";
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern(datePattern);
        config.withSerializers(new LocalDateTimeSerializer(dataFormatter));
        config.withDeserializers(new LocalDatetimeDeserializer(dataFormatter));
        
    }
}
