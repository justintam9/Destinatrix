package com.project.destinatrix.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.graphics.Bitmap;

import com.project.destinatrix.CustomCityAdapter;
import com.project.destinatrix.DataAction;
import com.project.destinatrix.FirebaseDatabaseHelper;
import com.project.destinatrix.IntelligentItinerary;
import com.project.destinatrix.R;
import com.project.destinatrix.ReadCallback;
import com.project.destinatrix.objects.CityData;
import com.project.destinatrix.objects.DestinationData;
import com.project.destinatrix.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DestinationMapAndListActivity extends AppCompatActivity {
    SectionsPagerAdapter sectionsPagerAdapter;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG = "DestinationMapAndListActivity";
    PlacesClient placesClient;
    FirebaseDatabaseHelper dbHelper;
    FirebaseDatabaseHelper dbHelperForRead;
    String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_map_and_list);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.places_api_key));
            placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        }
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        cityId = getIntent().getStringExtra("CityActivity");
        dbHelper = new FirebaseDatabaseHelper("destinations");
        dbHelperForRead = new FirebaseDatabaseHelper("destinations/" + cityId);
        dbHelperForRead.readData(DataAction.DestinationData, new ReadCallback() {
            @Override
            public void onCallBack(ArrayList<Object> list) {
                // PERFORM: MINIMUM TRAVELERS ALGO

                LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.GPS_PROVIDER;
                @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                double userLat = lastKnownLocation.getLatitude();
                double userLong = lastKnownLocation.getLongitude();

                ArrayList<Object> itinerary = IntelligentItinerary.computeItinerary(userLat, userLong, list);

                for (Object item: itinerary) {
                    DestinationData data = (DestinationData)item;
                    sectionsPagerAdapter.setItem(data.getDestinationId());
                }
            }
        });

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        ImageButton backButton = findViewById(R.id.backButton_destination);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DestinationMapAndListActivity.super.onBackPressed();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(DestinationMapAndListActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                sectionsPagerAdapter.setItem(place.getId());
                // TODO: ADD TO FIREBASE!
                DestinationData destinationData = new DestinationData(place.getId(), place.getName(), null, place.getLatLng(), place.getAddress(), place.getAddressComponents(), place.getOpeningHours(), place.getRating(), cityId);
                dbHelper.createData(destinationData, DataAction.DestinationData);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error adding city", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // cancelled
                Log.i(TAG, "Cancelled search");
            }
        }
    }
}