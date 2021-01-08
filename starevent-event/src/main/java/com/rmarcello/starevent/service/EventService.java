package com.rmarcello.starevent.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.rmarcello.starevent.model.Event;
import com.rmarcello.starevent.repository.EventRepository;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class EventService {

	//private Logger LOGGER = Logger.getLogger(EventService.class);

	@Inject EventRepository eventRepository;
	
	@Transactional(Transactional.TxType.SUPPORTS)
	public List<Event> getAllActiveEvents() {
        final LocalDateTime now = LocalDateTime.now();
        List<Event> eventList = eventRepository.listAvailableEvents(now);
		return eventList;
	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Optional<Event> getEventById(Long id) {
		return eventRepository.findByIdOptional( id );
	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Event getRandomEvent() {
		return eventRepository.findRandom();
		
	}

    public @Valid Event persistEvent(@Valid Event event) {
		eventRepository.persist(event);
		return event;
	}
	
	public @Valid Event updateEvent(@Valid Event event) {
		Optional<Event> opt = eventRepository.findByIdOptional(event.getId());
		if( opt.isPresent() ){
			eventRepository.getEntityManager().merge(event);
			return event;
		}
		return null;
	}

	public boolean deleteEvent(Long id) {
		return eventRepository.deleteById(id);
	}
	

	public Event reserve(Long id, Long amount) {
		Optional<Event> opt = eventRepository.findByIdOptional(id);
		if( opt.isPresent() ){
			Event e = opt.get();
			e.setAvailability( e.getAvailability() - amount.intValue() );
            return e;
        }
        return null;
	}

	public long countEvents() {
		return eventRepository.count();
	}

	


}