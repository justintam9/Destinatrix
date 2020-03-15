package com.project.destinatrix.objects;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;

public class DestinationData extends AppCompatActivity implements Serializable {
    public String ID;
    public String name;
    public LatLng latlng;
    public Bitmap photo;
    public String address;
    public AddressComponents addressComponents;
    public OpeningHours hours;
    public Double rating;

    public DestinationData(){

    }
    public DestinationData(String ID, String name, Bitmap photo, LatLng latlng, String address, AddressComponents addressComponents, OpeningHours hours, Double rating){
        this.ID = ID;
        this.name = name;
        this.latlng = latlng;
        this.photo = photo;
        this.address = address;
        this.addressComponents = addressComponents;
        this.hours = hours;
        this.rating = rating;
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
    public void print(){
        System.out.println ("ID ="+ID);
        System.out.println ("Name = "+name);
        System.out.println ("address" + address);
        System.out.println ("address components" + addressComponents);
    }


}