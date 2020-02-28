package com.project.destinatrix;

class TripData {
    String title;
    String description;
    Integer image;
    String tripID;
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