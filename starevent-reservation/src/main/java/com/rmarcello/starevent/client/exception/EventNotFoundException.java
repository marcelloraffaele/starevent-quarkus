package com.rmarcello.starevent.client.exception;

public class EventNotFoundException extends GenericClientException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EventNotFoundException(int responseCode) {
        super(responseCode);
    }

}