package com.project.destinatrix;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
public class CustomTripAdapter extends ArrayAdapter<String> {
    ArrayList<TripData>  tripList;
    Context context;
    public CustomTripAdapter(Activity context, ArrayList<TripData> tripList){
        super (context, R.layout.triplist_row);

        this.context = context;
        this.tripList = tripList;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    public void remove(int position) {
        tripList.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.triplist_row,null,true);
            viewHolder.imag = (ImageView) convertView.findViewById(R.id.list_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.des = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        TripData trip = tripList.get(position);
        viewHolder.title.setText(trip.title);
        viewHolder.des.setText(trip.description);
        viewHolder.imag.setImageResource(trip.image);
        return convertView;

    }

    class ViewHolder{
        TextView title;
        TextView des;
        ImageView imag;
    }


}