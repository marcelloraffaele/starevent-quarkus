package com.rmarcello.starevent.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.rmarcello.starevent.model.Reservation;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ReservationRepository implements PanacheRepository<Reservation> {
    
    public List<Reservation> getAllByUserId(String userId) {
        return this.list("userId=?1", userId);
    }

}
