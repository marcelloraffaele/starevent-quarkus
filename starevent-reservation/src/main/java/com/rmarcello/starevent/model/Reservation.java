package com.rmarcello.starevent.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name="reservation")
public class Reservation extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
    @Min(0)
    public Long id;

    @NotNull
    @Column(name = "event_id")
    public Long eventId;

    @NotNull
    @Column(name = "user_id")
    public String userId;

    @NotNull
    @Column(name = "secure_code")
    public String secureCode;

    @NotNull
    public LocalDateTime date;

    public static List<Reservation> getAllByUserId(String userId) {
        return list("userId=?1", userId);
    }   


    public static void doPersist(Reservation r) {
        persist(r);
    }

    @Override
    public String toString() {
        return "Reservation [date=" + date + ", eventId=" + eventId + ", id=" + id + ", secureCode=" + secureCode
                + ", userId=" + userId + "]";
    }
   

}