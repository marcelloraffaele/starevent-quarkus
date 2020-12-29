package com.rmarcello.starevent.client.exception;

public class EventNotFoundException extends GenericClientException {

    public EventNotFoundException(int responseCode) {
        super(responseCode);
    }

}