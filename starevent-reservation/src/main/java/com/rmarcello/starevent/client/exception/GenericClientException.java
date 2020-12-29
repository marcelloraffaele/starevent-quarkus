package com.rmarcello.starevent.client.exception;

public class GenericClientException extends RuntimeException {
    public final int responseCode;

    public GenericClientException(int responseCode) {
        super();
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    

}