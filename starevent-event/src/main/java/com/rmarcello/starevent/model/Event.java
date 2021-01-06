package com.rmarcello.starevent.model;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Event representation")
@Entity
@Table(name="event")
public class Event {

    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
    Long id;
    
    @NotNull
    String title;
    
    String artist;

    @NotNull
    String description;

    @Schema(example = "01/01/2021 00:00:00", pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonbDateFormat("dd/MM/yyyy HH:mm:ss")
    @Column(name = "start_date")
    LocalDateTime startDate;
    
    @NotNull
    String location;

    @NotNull
    String address;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer=3, fraction=2)
    Float price;

    @NotNull
    @Min(0)
    Integer availability;

    String img;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
                + ", title=" + title + ", location=" + location + "]";
    }

}
