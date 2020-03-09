package com.project.destinatrix;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class MoreDetailsActivity extends AppCompatActivity {
    public PlacesClient placesClient;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG = "MoreDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(getString(R.string.places_api_key));
        Places.initialize(getApplicationContext(), getString(R.string.places_api_key));
        placesClient = Places.createClient(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        Intent intent = this.getIntent();
        String id = getIntent().getExtras().getString("id");
        System.out.println(id);
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.PHOTO_METADATAS, Place.Field.OPENING_HOURS, Place.Field.RATING);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(id, fields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            getPhoto(place);
            Log.i(TAG, "Place found: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.getMessage());
            }
        });


        Button close = findViewById(R.id.more_details_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoreDetailsActivity.super.onBackPressed();
            }
        });
    }

    public void make(DestinationData data) {
        ImageView img_view = findViewById(R.id.image);
        TextView name_view = findViewById(R.id.destinationName);
        TextView city_view = findViewById(R.id.destinationCity);
        TextView hours_view = findViewById(R.id.hours);
        RatingBar rating_view = findViewById(R.id.rating);

        List<AddressComponent> addressComponents = data.getAddressComponents().asList();
        String state = "";
        String country = "";
        String city = "";
        String fullCity;
        for (int i = 0; i < addressComponents.size(); i++) {
            List types = addressComponents.get(i).getTypes();
            for (int j = 0; j < types.size(); j++) {
                if (types.get(j) == "administrative_area_level_1") {
                    state = addressComponents.get(i).getName();
                }
                if (types.get(j) == "locality") {
                    city = addressComponents.get(i).getName();
                }
                if (types.get(j) == "country") {
                    country = addressComponents.get(i).getName();
                }
            }
        }
        fullCity = city + state + country;

        double d = data.getRating();
        float rating = (float) d;

        img_view.setImageBitmap(data.getPhoto());
        name_view.setText(data.getName());
        city_view.setText(fullCity);
        hours_view.setText(data.getHours().toString());
        rating_view.setRating(rating);
    }

    protected void getPhoto(Place place) {
        // Get the photo metadata.
        PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
        // Get the attribution text.
        String attributions = photoMetadata.getAttributions();

        // Create a FetchPhotoRequest.
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500) // Optional.
                .setMaxHeight(300) // Optional.
                .build();
        placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
            Bitmap bitmap = fetchPhotoResponse.getBitmap();
            DestinationData ddata = new DestinationData(place.getId(), place.getName(), bitmap, place.getLatLng(), place.getAddress(), place.getAddressComponents(), place.getOpeningHours(), place.getRating());
            make(ddata);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                DestinationData ddata = new DestinationData(place.getId(), place.getName(), null, place.getLatLng(), place.getAddress(), place.getAddressComponents(), place.getOpeningHours(), place.getRating());
                make(ddata);
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.getMessage());
            }
        });

    }
}
