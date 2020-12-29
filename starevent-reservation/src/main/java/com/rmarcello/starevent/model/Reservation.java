package com.rmarcello.starevent.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Reservation {

    @Min(0)
    Long id;

    @NotNull
    Long eventId;

    @NotNull
    String userId;

    @NotNull
    String secureCode;

    @NotNull
    String date;

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reservation [date=" + date + ", eventId=" + eventId + ", id=" + id + ", secureCode=" + secureCode
                + ", userId=" + userId + "]";
    }

    

}