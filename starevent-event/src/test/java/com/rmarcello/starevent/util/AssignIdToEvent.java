package com.rmarcello.starevent.util;

import com.rmarcello.starevent.model.Event;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AssignIdToEvent implements Answer<Void> {

    private final Long id;

    public AssignIdToEvent(Long id) {
        this.id = id;
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        Event r = (Event) invocation.getArguments()[0];
        r.setId(this.id);
        return null;
    }
}