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

    private String image;
    private String tripId;
    private String cityId;
    private double latitude;
    private double longitude;

    public CityData() {}

    public CityData(String title, Bitmap image, String tripId, String cityId, LatLng lat_lng) {
        this.title = title;
        this.image = bitmapToString(image);
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

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap getEncodedImage() {
        try {
            byte [] encodeByte = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
