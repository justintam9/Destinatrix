package com.project.destinatrix;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.project.destinatrix.Activities.DestinationMapAndListActivity;
import com.project.destinatrix.objects.CityData;
import com.project.destinatrix.objects.DestinationData;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class CustomCityAdapter extends RecyclerView.Adapter<CustomCityAdapter.MyViewHolder> {
    private Context mContext;
    private List<CityData> cityDataList;
    PlacesClient placesClient;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};

    public CustomCityAdapter(Context mContext, List<CityData> cityDataList, PlacesClient placesClient){
        this.mContext=mContext;
        this.cityDataList = cityDataList;
        this.placesClient = placesClient;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.cardview_city,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
    holder.city_title.setText(cityDataList.get(position).getTitle());
        if (cityDataList.get(position).getPlaceId() != null) {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.PHOTO_METADATAS);
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(cityDataList.get(position).getPlaceId(), fields);

            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                getPhoto(place, holder.city_image);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                }
            });
        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = position;
                removeCityDialog(pos);
                return true;
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DestinationMapAndListActivity.class);
                intent.putExtra("CityActivity", cityDataList.get(position).getCityId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityDataList.size();
    }

    public void remove(int position) {
        cityDataList.remove(position);
        notifyDataSetChanged();
    }
    private Integer getRandomImage() {
        return images[(int)(Math.random()*(images.length))];
    }


    public void removeCityDialog(int pos){
        Bundle args = new Bundle();
        args.putInt("pos",pos);
        RemoveCityDialog removeCityDialog = new RemoveCityDialog();
        removeCityDialog.setArguments(args);
        removeCityDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(),"city_dialog");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView city_title;
        ImageView city_image;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            city_title = itemView.findViewById(R.id.city_title);
            city_image = itemView.findViewById(R.id.city_image);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }

    public void getPhoto(Place place, ImageView img_view) {
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
                img_view.setImageBitmap(bitmap);

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                }
            });
        }
    }
}
