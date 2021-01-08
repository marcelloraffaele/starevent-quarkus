package com.rmarcello.starevent;

import com.rmarcello.starevent.model.Reservation;

import org.jboss.logging.Logger;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AssignIdToReservation implements Answer<Void> {
    private static Logger LOGGER = Logger.getLogger(AssignIdToReservation.class);

    private final Long id;

    public AssignIdToReservation(Long id) {
        LOGGER.info("******* "+id);
        this.id = id;
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        LOGGER.info("*******answer "+invocation);
        Reservation r = (Reservation) invocation.getArguments()[0];
        r.id = this.id;
        return null;
    }
}