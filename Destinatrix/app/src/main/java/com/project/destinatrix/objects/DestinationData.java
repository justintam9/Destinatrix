package com.project.destinatrix.objects;

import java.io.Serializable;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DestinationData implements Serializable {
    public String destinationId;
    public String name;
    private double latitude;
    private double longitude;
    public Double rating;
    public String cityId;
    public String address;

    @Exclude
    public Bitmap photo;
    @Exclude
    public AddressComponents addressComponents;
    @Exclude
    public OpeningHours hours;

    public DestinationData(){}

    public DestinationData(String destinationId, String name, Bitmap photo, LatLng latlng, String address, AddressComponents addressComponents, OpeningHours hours, Double rating, String cityId){
        this.destinationId = destinationId;
        this.name = name;
        this.latitude = latlng.latitude;
        this.longitude = latlng.longitude;
        this.photo = photo;
        this.address = address;
        this.addressComponents = addressComponents;
        this.hours = hours;
        this.rating = rating;
        this.cityId = cityId;
    }

    public OpeningHours getHours() {
        return hours;
    }

    public void setHours(OpeningHours hours) {
        this.hours = hours;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public AddressComponents getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(AddressComponents addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getDestinationId() {
        return destinationId;
    }
    public String getCityId() { return cityId; }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() { return longitude; }
//    public LatLng getLatLng() { return latLng; }

//    public void setLatLng(LatLng latLng) { this.latLng = latLng; }
    public void setLatitude(double lat) { this.latitude = lat; }
    public void setLongitude(double lon) { this.longitude = lon; }


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
    public void print(){
        System.out.println ("ID ="+ destinationId);
        System.out.println ("Name = "+name);
        System.out.println ("address" + address);
        System.out.println ("address components" + addressComponents);
    }


}