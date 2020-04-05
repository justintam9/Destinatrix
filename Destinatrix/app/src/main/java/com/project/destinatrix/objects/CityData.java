package com.project.destinatrix.objects;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.BitmapFactory;
import android.util.Base64;

@IgnoreExtraProperties
public class CityData implements Serializable {
    private String title;
    private String placeId;

    private String image;
    private String tripId;
    private String cityId;
    private double latitude;
    private double longitude;

    public CityData() {}

    public CityData(String title, String placeId, String tripId, String cityId, LatLng lat_lng) {
        this.title = title;
        this.placeId = placeId;
        this.tripId = tripId;
        this.cityId = cityId;
        this.latitude = lat_lng.latitude;
        this.longitude = lat_lng.longitude;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTripId(String tripId) { this.tripId = tripId; }
    public void setCityId(String cityId) { this.cityId = cityId; }

    public String getPlaceId() { return placeId; }

    public String getTitle() {
        return title;
    }
    public String getTripId() {
        return tripId;
    }

    public String getImage() {
        return image;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public String getCityId() {
        return cityId;
    }

}
