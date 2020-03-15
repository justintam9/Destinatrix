package com.project.destinatrix.objects;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class TripData implements Serializable {
    private String title;
    private String description;
    private Integer image;
    private String tripID;

    public TripData() {}

    public TripData(String title, String description, Integer image, String tripID) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.tripID = tripID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }
}