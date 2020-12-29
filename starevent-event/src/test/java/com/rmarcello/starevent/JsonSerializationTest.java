package com.rmarcello.starevent;

import java.time.format.DateTimeFormatter;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import com.rmarcello.starevent.model.Event;
import com.rmarcello.starevent.util.EventUtil;
import com.rmarcello.starevent.util.LocalDateTimeSerializer;
import com.rmarcello.starevent.util.LocalDatetimeDeserializer;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JsonSerializationTest {

    static final Logger LOGGER = Logger.getLogger(JsonSerializationTest.class);
    @Test
    @Disabled
    public void serializationTest() {

        String datePattern = "dd/MM/yyyy HH:mm:ss";
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern(datePattern);
        Event event = EventUtil.createTestEvent(11);

        JsonbConfig config = new JsonbConfig().withFormatting(true);
        config.withSerializers(new LocalDateTimeSerializer(dataFormatter));
        config.withDeserializers(new LocalDatetimeDeserializer(dataFormatter));

        Jsonb jsonb = JsonbBuilder.create(config);

        String json = jsonb.toJson(event);

        LOGGER.debug(">" + json );

        Event parsedEvent = jsonb.fromJson(json, Event.class);

        LOGGER.debug(">" + parsedEvent );

    }

}
