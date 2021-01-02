package com.rmarcello.starevent.client.event;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class Event {

    Long id;
    
    @NotNull
    String title;
    
    String artist;

    @NotNull
    String description;

    @NotNull
    @JsonbDateFormat("dd/MM/yyyy HH:mm:ss")
    LocalDateTime startDate;
    
    @NotNull
    String where;

    @NotNull
    String address;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer=3, fraction=2)
    Float price;

    @NotNull
    @Min(0)
    Integer availability;

    String img;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Event [address=" + address + ", artist=" + artist + ", availability=" + availability + ", description="
                + description + ", id=" + id + ", img=" + img + ", price=" + price + ", startDate=" + startDate
                + ", title=" + title + ", where=" + where + "]";
    }

    
    
    
    

}
