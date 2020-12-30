package com.rmarcello.starevent.beans;

import javax.validation.constraints.NotNull;

public class CreateReservationIn {

    @NotNull
    Long eventId;

    @NotNull
    String userId;

    public CreateReservationIn() {}

    public CreateReservationIn( Long eventId, String userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{" +
            " eventId='" + getEventId() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }


    

}