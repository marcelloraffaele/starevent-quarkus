package com.rmarcello.starevent;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.rmarcello.starevent.model.Reservation;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ReservationResourceTest {



    @Test
    public void createAndCheck() {
        Reservation r = createRandomReservation();


        String location = given()
        .body(r)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
        .when().post("/api/reservation")
        .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract().header("Location");

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
                .body("id", Is.is( r.getId().intValue() ))
                .body("eventId", Is.is( r.getEventId().intValue() ))
                .body("userId", Is.is( r.getUserId() ))
                .body("secureCode", Is.is( r.getSecureCode() ))
                .body("date", Is.is( r.getDate()))
                ;

    }

    private Reservation createRandomReservation() {
        Random random = new Random();
        Reservation r = new Reservation();
        r.setId( (long) random.nextInt(100) );
        r.setEventId( (long) random.nextInt(100) );
        r.setUserId( "user_" + random.nextInt(100) );
        r.setSecureCode( "sc_" + random.nextInt(100) );
        r.setDate( "dt_" + random.nextInt(100) );
        return r;
    }

}