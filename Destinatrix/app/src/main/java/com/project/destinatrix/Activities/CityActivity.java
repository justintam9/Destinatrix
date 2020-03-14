package com.project.destinatrix.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.project.destinatrix.CustomCityAdapter;
import com.project.destinatrix.DataAction;
import com.project.destinatrix.FirebaseDatabaseHelper;
import com.project.destinatrix.R;
import com.project.destinatrix.ReadCallback;
import com.project.destinatrix.RemoveCityDialog;
import com.project.destinatrix.objects.CityData;
import com.project.destinatrix.ui.main.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityActivity extends AppCompatActivity implements RemoveCityDialog.cityDialogListener {
    ArrayList<CityData> cityList;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    CustomCityAdapter myAdapter;
    RecyclerView recyclerView;
    String tripId;
    FirebaseDatabaseHelper dbHelper;
    FirebaseDatabaseHelper dbHelperForRead;
    PlacesClient placesClient;

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG = "CityActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview_city);
        tripId = getIntent().getStringExtra("tripID");
        dbHelper = new FirebaseDatabaseHelper("cities");
        dbHelperForRead = new FirebaseDatabaseHelper("cities/" + tripId);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.places_api_key));
            placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        }

        int spanCount = 2; // 2 columns
        int spacing = 75; // 75px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        dbHelperForRead.readData(DataAction.CityData, new ReadCallback() {
            @Override
            public void onCallBack(ArrayList<Object> list) {
                CityActivity.this.cityList = (ArrayList<CityData>) (ArrayList<?>)list;
                CityActivity.this.myAdapter = new CustomCityAdapter(CityActivity.this,CityActivity.this.cityList);
                CityActivity.this.recyclerView.setAdapter(CityActivity.this.myAdapter);
            }
        });


        setupOnClickListeners();
    }

    private void setupOnClickListeners() {
        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(CityActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }

    public void remove(Integer pos) {
        myAdapter.remove(pos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String cityId = dbHelper.getDataId();
                // cityList.add(city);
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
                    CityData city = new CityData(place.getName(), bitmap, CityActivity.this.tripId, cityId, place.getLatLng());
                    cityList.add(city);
                    myAdapter.notifyDataSetChanged();
                    dbHelper.createData(city, DataAction.CityData);
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        CityData city = new CityData(place.getName(), null, CityActivity.this.tripId, cityId, place.getLatLng());
                        cityList.add(city);
                        myAdapter.notifyDataSetChanged();
                        dbHelper.createData(city, DataAction.CityData);
                        // Handle error with given status code.
                        Log.e(TAG, "Place not found: " + exception.getMessage());
                    }
                });
//                myAdapter.notifyDataSetChanged();

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

