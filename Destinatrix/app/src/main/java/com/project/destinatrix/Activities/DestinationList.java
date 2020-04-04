package com.project.destinatrix.Activities;

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
import com.project.destinatrix.R;
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

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            DestinationData data = new DestinationData();
            data.setName(place.getName());
            data.setDestinationId(place.getId());
            data.setAddress(place.getAddress());
            getPhoto(place, data);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
            }
        });
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
                mIntent.putExtra("id", destinations.get(i).getDestinationId());
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            int pos = position +1;
            view = getLayoutInflater().inflate(R.layout.destination_item_layout, null);
            TextView posView = (TextView) view.findViewById((R.id.item_numb));
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.textDestinationName);
            TextView textView_address = (TextView) view.findViewById(R.id.textDestinationDescription);

            imageView.setImageBitmap(destinations.get(position).getPhoto());
            textView_name.setText(destinations.get(position).getName());
            textView_address.setText(destinations.get(position).getAddress());
            //posView.setText(destinations.get(i).getID());
            posView.setText(" " +pos+" " );
            pos++;


            return view;
        }
    }

    public void getPhoto(Place place, DestinationData data) {
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
                data.setPhoto(bitmap);
                addToList(place, data);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                }
            });
        }
        else{
            addToList(place,data);
        }
    }

    public void addToList(Place place, DestinationData data){
        destinations.add(data);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }
}
