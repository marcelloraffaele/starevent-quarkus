package com.rmarcello.starevent.beans;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateReservationOut {

    @NotNull
    @Min(0)
    Long id;

    @NotNull
    Long eventId;

    @NotNull
    String userId;

    @NotNull
    String secureCode;

    @JsonbDateFormat("dd/MM/yyyy HH:mm:ss")
    LocalDateTime date;

    public CreateReservationOut() {}

    public CreateReservationOut(Long id, Long eventId, String userId, String secureCode, LocalDateTime date) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.secureCode = secureCode;
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSecureCode() {
        return this.secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", eventId='" + getEventId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", secureCode='" + getSecureCode() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }


    

}