package com.rmarcello.starevent.client.exception;

public class GenericClientException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1614105208078024464L;
    
    public final int responseCode;

    public GenericClientException(int responseCode) {
        super();
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    

}