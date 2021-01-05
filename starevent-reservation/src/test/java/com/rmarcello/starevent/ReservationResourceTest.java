package com.rmarcello.starevent;

//https://www.baeldung.com/mockito-void-methods
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.rmarcello.starevent.beans.CreateReservationIn;
import com.rmarcello.starevent.model.Reservation;
import com.rmarcello.starevent.repository.ReservationRepository;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class ReservationResourceTest {

    private static Logger LOGGER = Logger.getLogger(ReservationResourceTest.class);

    @InjectMock
    ReservationRepository reservationRepository;

    @Test
    public void createAndCheck() {
        final long ID = 12345L;
        // prepare mock
        Reservation r = createRandomReservation();
        Mockito.doReturn(Optional.of(r)).when(reservationRepository).findByIdOptional(any(Long.class));
        Mockito.doAnswer(invocation -> {
            Reservation in = invocation.getArgument(0);
            in.setId(ID);
            return null;
        }).when(reservationRepository).persist(isA(Reservation.class));

        // test logic
        CreateReservationIn cri = createRandomReservationIn(r);
        LOGGER.debug("invoking reservation, cri: " + cri);

        String location = given().contentType(MediaType.APPLICATION_JSON).body(cri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON).when().post("/api/reservation").then()
                .statusCode(Status.CREATED.getStatusCode()).extract().header("Location");

        LOGGER.debug("created!, location: " + location);

        // Extracts the Location and stores the event id
        assertTrue(location.contains("/api/reservation"));
        String[] segments = location.split("/");
        String createdId = segments[segments.length - 1];
        assertNotNull(createdId);
        assertEquals(ID, Long.parseLong(createdId));

        // Checks the event has been created
        given().pathParam("id", createdId)
            .when()
                .get("/api/reservation/{id}")
            .then()
                .statusCode(Status.OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body("eventId", Is.is(cri.getEventId().intValue()))
                .body("userId", Is.is(cri.getUserId()))
                .body("secureCode", Is.is(r.getSecureCode()))
                .body("date", Is.is( matchDate(r.getDate() ) ));

    }


    private CreateReservationIn createRandomReservationIn(Reservation reservation) {
        CreateReservationIn r = new CreateReservationIn();
        r.setEventId(reservation.getEventId());
        r.setUserId(reservation.getUserId());
        return r;
    }

    private Reservation createRandomReservation() {
        Random random = new Random();
        Reservation r = new Reservation();
        r.setId((long) random.nextInt(100));
        r.setEventId((long) random.nextInt(100));
        r.setUserId("user_" + random.nextInt(100));
        r.setSecureCode("sc_" + random.nextInt(100));
        r.setDate(LocalDateTime.now());
        return r;
    }

    private Matcher<String> matchDate(LocalDateTime d) {
        String str = d.toString();
        return Matchers.equalTo( str.substring(0, str.length()-2) );
    }

}