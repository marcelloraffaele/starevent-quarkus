package com.rmarcello.starevent;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.rmarcello.starevent.model.Event;
import com.rmarcello.starevent.repository.EventRepository;
import com.rmarcello.starevent.util.AssignIdToEvent;
import com.rmarcello.starevent.util.EventUtil;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.internal.mapping.JsonbMapper;
import io.restassured.path.json.mapper.factory.DefaultYassonObjectMapperFactory;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventResourceTest {

    @InjectMock
    EventRepository eventRepository;

    @BeforeAll
    public static void init(){
        //Configure the default Rest Assured Mapper to Jsonb mapper. By default RestAssured try to find other mapper and the test could fail.
        //https://github.com/rest-assured/rest-assured/blob/master/examples/rest-assured-itest-java/src/test/java/io/restassured/itest/java/CustomObjectMappingITest.java
        //info at https://www.javadoc.io/doc/io.rest-assured/rest-assured/latest/io/restassured/internal/mapping/JsonbMapper.html
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(ObjectMapperConfig.objectMapperConfig().defaultObjectMapper(  new JsonbMapper(new DefaultYassonObjectMapperFactory()) ) );

    }
    
    @Test
    @Order(1)
    public void testBaseEndpoint() {    
        // generate a list of 3 events
        List<Event> originEvent = IntStream.rangeClosed(1,3).boxed().map( i-> EventUtil.createTestEvent(true) ).collect(Collectors.toList());
        //mock the repository to return this list
        Mockito.when( eventRepository.listAvailableEvents(any(LocalDateTime.class)) ).thenReturn( originEvent );
        //invoke the resource and parse the body
        List<Event> eventList = given()
          .when()
          .accept(ContentType.JSON)  
          .get("/api/events")
          .then()
             .statusCode(OK.getStatusCode())
             .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
             .extract().body().as( new TypeRef<List<Event>>() {} );
        //check if the resource list is equal to mocked list
        assertEquals(originEvent, eventList, "check if the resource list is equal to mocked list");
        
    }

    
    @Test
    @Order(2)
    void createEvent() {
        //init mocked objects
        Event eventToAdd = EventUtil.createTestEvent(false);
        long createID= 12345;       
        //mock the repository persist method, it will set the Id on the event during create
        Mockito.doAnswer(new AssignIdToEvent(createID) ).when( eventRepository ).persist(any(Event.class));
        //invoke the resource and get the location header
        String location = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(eventToAdd)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().post("/api/events")
                .then()
                    .statusCode(Status.CREATED.getStatusCode()).extract().header("Location");
        //assertions
        assertTrue(location.contains("/api/events"));
        String[] segments = location.split("/");
        long createdEventId = Long.parseLong( segments[segments.length - 1] );
        assertNotNull(createdEventId);        
        assertEquals(createID, createdEventId, "check if location Id is equal to mocked id");

    }

    @Test
    @Order(3)
    void getEventOK() {
        // generate a an event
        Event originEvent =EventUtil.createTestEvent(true);
        //mock the repository to return the created event
        Mockito.when( eventRepository.findByIdOptional(any(Long.class)) ).thenReturn( Optional.of(originEvent) );
        //invoke the resource and get event
        Event event = given()
            .pathParam("id", originEvent.getId() )
            .when().get("/api/events/{id}")
                .then().statusCode(OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .extract().body().as( new TypeRef<Event>() {} );
        assertNotNull(event);        
        assertEquals(originEvent, event, "check if the event is equal to mocked event");
    }

    @Test
    @Order(4)
    void getEvent_NOT_FOUND() {
        // generate a an event
        Event originEvent = null;
        //mock the repository to return the created event
        Mockito.when( eventRepository.findByIdOptional(any(Long.class)) ).thenReturn( Optional.ofNullable(originEvent) );
        //invoke the resource and get event
        given()
            .pathParam("id", 1L )
            .when().get("/api/events/{id}")
                .then().statusCode(NOT_FOUND.getStatusCode());

    }
    
    @Test
    @Order(5)
    void updateTest() {
        // generate a an event
        Event originEvent = EventUtil.createTestEvent(true);
        Event updatedEvent = EventUtil.createTestEvent(false);
        updatedEvent.setId(originEvent.getId());
        //mock the repository
        Mockito.when( eventRepository.findByIdOptional(any(Long.class)) ).thenReturn( Optional.ofNullable(originEvent) );
        Mockito.when( eventRepository.update(any(Event.class)) ).thenReturn( updatedEvent );

        // updating an event
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(updatedEvent)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when().put("/api/events")
            .then()
                .statusCode(OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body( "id", Is.is(originEvent.getId().intValue()) )
                .body("title", Is.is(updatedEvent.getTitle()))
                .body("description", Is.is(updatedEvent.getDescription()))
                .body("address", Is.is(updatedEvent.getAddress()))
                .body("price", Is.is(updatedEvent.getPrice()))
                .body("location", Is.is(updatedEvent.getLocation()))
                .body("startDate", matchDate(updatedEvent.getStartDate() ));
        
    }

    @Test
    @Order(6)
    void deleteTest() {
        long ID = 123;
        //mock the repository
        Mockito.when( eventRepository.deleteById(any(Long.class)) ).thenReturn( true );
        // deleting an event
        given()
            .pathParam("id", ID)
            .when().delete("/api/events/{id}")
                .then().statusCode( NO_CONTENT.getStatusCode());
        
    }

    private Matcher<String> matchDate(LocalDateTime d) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String str = d.format(fmt);
        return Matchers.equalTo( str );
    }
    
}