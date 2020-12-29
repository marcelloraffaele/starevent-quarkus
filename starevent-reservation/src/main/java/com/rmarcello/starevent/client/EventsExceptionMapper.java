package com.rmarcello.starevent.client;

import javax.ws.rs.core.Response;

import com.rmarcello.starevent.client.exception.EventNotFoundException;
import com.rmarcello.starevent.client.exception.GenericClientException;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

public class EventsExceptionMapper implements ResponseExceptionMapper<RuntimeException> { // (2)

    @Override
    public RuntimeException toThrowable(Response response) {
        int status = response.getStatus(); // (3)

        RuntimeException re;
        switch (status) {
            case 404:
                re = new EventNotFoundException(status);
                break;
            default:
                re = new GenericClientException(status);
        }
        return re;
    }

}
