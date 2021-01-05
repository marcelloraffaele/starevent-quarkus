package com.rmarcello.starevent.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import com.rmarcello.starevent.model.Event;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {

	public List<Event> listAvailableEvents(LocalDateTime now) {
        List<Event> l = this.list("availability > 0 and startDate > ?1", now);
		return l;
	}

	public Event findRandom() {
        long count = this.count();
        int randomPage = new Random().nextInt((int) count);
        return this.findAll().page(randomPage, 1).firstResult();
	}


}