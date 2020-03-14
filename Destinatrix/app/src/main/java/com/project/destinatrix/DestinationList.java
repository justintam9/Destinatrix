package com.project.destinatrix;

import android.content.Intent;
import android.os.Bundle;

import com.project.destinatrix.Activities.MoreDetailsActivity;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.project.destinatrix.objects.DestinationData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DestinationList extends Fragment {
    ListView listView;
    ArrayList<DestinationData> destinations = new ArrayList<>();
    CustomAdapter customAdapter;
    Integer[] images = {R.drawable.stock_image1, R.drawable.stock_image2, R.drawable.stock_image3, R.drawable.stock_image4, R.drawable.stock_image5};
    PlacesClient placesClient;

    public void setDestination(String placeId) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS, Place.Field.PHOTO_METADATAS);
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);
        DestinationData data = new DestinationData();
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Bitmap photo = getPhoto(place);
            data.setName(place.getName());
            data.setAddress(place.getAddress());
            data.setPhoto(photo);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
            }
        });
        destinations.add(data);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Places.initialize(getContext(), getString(R.string.places_api_key));
        placesClient = com.google.android.libraries.places.api.Places.createClient(getContext());
        return inflater.inflate(R.layout.activity_destination_list, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        listView = (ListView) view.findViewById(R.id.listview);
        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent(getContext(), MoreDetailsActivity.class);
                mIntent.putExtra("id", destinations.get(i).getID());
                startActivity(mIntent);
            }
        });
    }

    public Integer getRandomImage() {
        return images[(int) (Math.random() * (images.length))];
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return destinations.size(); // SIZE OF THE DATA
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.destination_item_layout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.textDestinationName);
            TextView textView_address = (TextView) view.findViewById(R.id.textDestinationDescription);

            imageView.setImageBitmap(destinations.get(i).getPhoto());
            textView_name.setText(destinations.get(i).getName());
            textView_address.setText(destinations.get(i).getAddress());


            return view;
        }
    }

    public Bitmap getPhoto(Place place) {
        // Get the photo metadata.
        PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
        // Get the attribution text.
        String attributions = photoMetadata.getAttributions();
        final Bitmap[] bitmap = {null};
        // Create a FetchPhotoRequest.
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500) // Optional.
                .setMaxHeight(300) // Optional.
                .build();
        placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
            bitmap[0] = fetchPhotoResponse.getBitmap();
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
            }
        });
        return bitmap[0];
    }
}
