package com.project.destinatrix.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.project.destinatrix.DirectionsParser;
import com.project.destinatrix.R;
import com.project.destinatrix.objects.DestinationData;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MoreDetailsActivity extends AppCompatActivity {
    public PlacesClient placesClient;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String TAG = "MoreDetailsActivity";
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Places.initialize(getApplicationContext(), getString(R.string.places_api_key));
        placesClient = Places.createClient(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        Intent intent = this.getIntent();
        String id = getIntent().getExtras().getString("id");
        System.out.println(id);
        List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG,Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.PHOTO_METADATAS, Place.Field.OPENING_HOURS, Place.Field.RATING);
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
        TextView address_view = findViewById(R.id.address);
        TextView name_view = findViewById(R.id.destinationName);
        TextView city_view = findViewById(R.id.destinationCity);
        TextView hours_view = findViewById(R.id.hours);
        RatingBar rating_view = findViewById(R.id.rating);

        List<AddressComponent> addressComponents = data.getAddressComponents().asList();
        String fullCity = parseAddress(addressComponents);

        //rating
        if (data.getRating() != null) {
            double d = data.getRating();
            float rating = (float) d;
            rating_view.setRating(rating);
        } else {
            rating_view.setRating(0);
        }

        //hours
        String hours = "";
        if (data.getHours() != null) {
            List<String> listHours = data.getHours().getWeekdayText();
            for (int i = 0; i < listHours.size(); i++){
                hours = hours + listHours.get(i) + "\n";
            }
        }

        getArrivalTime(data.getLatitude(), data.getLongitude());

        img_view.setImageBitmap(data.getPhoto());
        address_view.setText(data.getAddress());
        name_view.setText(data.getName());
        city_view.setText(fullCity);
        hours_view.setText(hours);
    }

    protected void getPhoto(Place place) {
        // Get the photo metadata.
        if (place.getPhotoMetadatas() != null) {
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
                DestinationData ddata = new DestinationData(place.getId(), place.getName(), bitmap, place.getLatLng(), place.getAddress(), place.getAddressComponents(), place.getOpeningHours(), place.getRating(), "");
                make(ddata);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    DestinationData ddata = new DestinationData(place.getId(), place.getName(), null, place.getLatLng(), place.getAddress(), place.getAddressComponents(), place.getOpeningHours(), place.getRating(), "");
                    make(ddata);
                    // Handle error with given status code.
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                }
            });
        } else {
            DestinationData ddata = new DestinationData(place.getId(), place.getName(), null, place.getLatLng(), place.getAddress(), place.getAddressComponents(), place.getOpeningHours(), place.getRating(), "");
            make(ddata);
        }
    }

    public String parseAddress(List<AddressComponent> addressComponents){
        String state = "";
        String country = "";
        String city = "";
        String fullCity;
        for (int i = 0; i < addressComponents.size(); i++) {
            List types = addressComponents.get(i).getTypes();
            for (int j = 0; j < types.size(); j++) {
                if (types.get(j).equals("administrative_area_level_1")) {
                    state = addressComponents.get(i).getName();
                }
                if (types.get(j).equals("locality")) {
                    city = addressComponents.get(i).getName();
                }
                if (types.get(j).equals("country")) {
                    country = addressComponents.get(i).getName();
                }
            }
        }

        fullCity = city +", "+ state +", " + country;
        return fullCity;
    }

    public void getArrivalTime(Double latitude, Double longitude){
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        double userLat = lastKnownLocation.getLatitude();
        double userLong = lastKnownLocation.getLongitude();
        LatLng current = new LatLng(userLat, userLong);
        String urlString = getDirectionsUrl(current, latitude, longitude);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        //taskRequestDirections.execute(urlString);

    }
    public void addToArrivalView(String response){
        TextView arrival_view = findViewById(R.id.arrival);
        arrival_view.setText(response);
    }

    private String getDirectionsUrl(LatLng origin, Double latitude, Double longitude) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + latitude + "," + longitude;

        String key = "key=" + getString(R.string.places_api_key);

        String parameters = str_origin + "&" + str_dest + "&" + key;

        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }
    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }



    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addToArrivalView(responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);

        }
    }
    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                JSONObject jsonData = new JSONObject(strings[0]);
                JSONArray jsonRoutes = jsonData.getJSONArray("routes");
                for (int i = 0; i < jsonRoutes.length(); i++) {
                    JSONObject jsonRoute = jsonRoutes.getJSONObject(i);

                    JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                    JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                    JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                    JSONObject jsonDuration = jsonLeg.getJSONObject("duration");


                    String duration = jsonDuration.getString("text");
                    addToArrivalView(duration);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {

        }
    }

}
