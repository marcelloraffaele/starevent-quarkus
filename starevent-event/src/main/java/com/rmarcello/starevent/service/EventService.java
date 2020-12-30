package com.rmarcello.starevent.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

import com.rmarcello.starevent.model.Event;

@ApplicationScoped
public class EventService {

    //local simple database
	private Map<Long, Event> eventMap = new HashMap<Long, Event>();
	private long currentId=1;

	public List<Event> getAllActiveEvents() {
        final LocalDateTime now = LocalDateTime.now();
        List<Event> eventList = eventMap.values().stream()
			.filter( e -> e.getStartDate().isAfter(now) )
			.filter( e -> e.getAvailability() > 0)
            .collect(Collectors.toList());
		return eventList;
	}

	public Optional<Event> getEventById(Long id) {
		return Optional.ofNullable( eventMap.get(id) );
	}

    public @Valid Event persistEvent(@Valid Event event) {
		event.setId(currentId);
		currentId++;
		eventMap.put( event.getId(), event);
		return eventMap.get(event.getId());
	}
	
	public @Valid Event updateEvent(@Valid Event event) {
		eventMap.put( event.getId() , event);
		return eventMap.get(event.getId());
	}

	public boolean deleteEvent(Long id) {
        if(eventMap.containsKey(id)) {
            eventMap.remove(id);
            return true;
        }
        return false;
	}

	public Event getRandomEvent() {
		List<Event> eventListCopy = eventMap.values().stream()
			.collect(Collectors.toList());
		Collections.shuffle(eventListCopy);
		return eventListCopy.get(0);
	}

	public Event reserve(Long id, Long amount) {
		if(eventMap.containsKey(id)) {
			Event e = eventMap.get(id);
			e.setAvailability( e.getAvailability() - amount.intValue() );
            return e;
        }
        return null;
	}

	


}