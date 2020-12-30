package com.rmarcello.starevent;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Random;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.rmarcello.starevent.beans.CreateReservationIn;
import com.rmarcello.starevent.model.Reservation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ReservationResourceTest {

    private static Logger LOGGER = Logger.getLogger(ReservationResourceTest.class);

    @Test
    public void createAndCheck() {
        CreateReservationIn cri = createRandomReservation();

        LOGGER.debug( "invoking reservation, cri: " + cri );

        String location = given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(cri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
                .post("/api/reservation")
                .then()
                    .statusCode(Status.CREATED.getStatusCode())
                    .extract().header("Location");

        LOGGER.debug( "created!, location: " + location );

        // Extracts the Location and stores the event id
        assertTrue(location.contains("/api/reservation"));
        String[] segments = location.split("/");
        String id = segments[segments.length - 1];
        assertNotNull(id);
        
        // Checks the event has been created
        given()
            .pathParam("id", id)
            .when().get("/api/reservation/{id}")
                .then().statusCode(Status.OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body("eventId", Is.is( cri.getEventId().intValue() ))
                .body("userId", Is.is( cri.getUserId() ))
                .body("secureCode", IsNot.not(Matchers.nullValue()) )
                .body("date", IsNot.not(Matchers.nullValue()) )
                ;

    }

    private CreateReservationIn createRandomReservation() {
        Random random = new Random();
        CreateReservationIn r = new CreateReservationIn();
        r.setEventId( (long) random.nextInt(100) );
        r.setUserId( "user_" + random.nextInt(100) );
        return r;
    }

}