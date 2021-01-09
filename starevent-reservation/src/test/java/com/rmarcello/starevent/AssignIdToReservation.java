package com.rmarcello.starevent;

import com.rmarcello.starevent.model.Reservation;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AssignIdToReservation implements Answer<Void> {
    
    private final Long id;

    public AssignIdToReservation(Long id) {
        this.id = id;
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        Reservation r = (Reservation) invocation.getArguments()[0];
        r.id = this.id;
        return null;
    }
}