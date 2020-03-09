package com.project.destinatrix;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.PhotoMetadata;

public class DestinationData {
    private String ID;
    private String name;
    private LatLng latlng;
    private Bitmap photo;
    private String address;

    public DestinationData(String ID, String name, Bitmap photo, String address){
        this.ID = ID;
        this.name = name;
        this.latlng = latlng;
        this.photo = photo;
        this.address = address;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
