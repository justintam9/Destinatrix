package com.project.destinatrix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityActivity extends AppCompatActivity implements RemoveCityDialog.cityDialogListener{
    ArrayList<CityData> cityList;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    CustomCityAdapter myAdapter;
    RecyclerView view;

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG = "CityActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityList = new ArrayList<>();
        view = findViewById(R.id.recyclerview_city);

        myAdapter = new CustomCityAdapter(this,cityList);
        cityList.add(new CityData("hi",getRandomImage()));
        cityList.add(new CityData("hi",getRandomImage()));
        cityList.add(new CityData("hi",getRandomImage()));
        cityList.add(new CityData("hi",getRandomImage()));
        view.setLayoutManager(new GridLayoutManager(this,3));
        view.setAdapter(myAdapter);

        setupOnClickListeners();
    }

    private void setupOnClickListeners() {
        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), getString(R.string.places_api_key));
                }
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(CityActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }

    private Integer getRandomImage() {
        return images[(int)(Math.random()*(images.length))];
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
                CityData city = new CityData(place.getName(), getRandomImage());
                cityList.add(city);
                myAdapter.notifyDataSetChanged();

                // TODO: ADD TO FIREBASE!

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

