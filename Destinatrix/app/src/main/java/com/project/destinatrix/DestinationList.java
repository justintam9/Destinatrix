package com.project.destinatrix;

import android.content.Intent;
import android.os.Bundle;

import com.project.destinatrix.Activities.MoreDetailsActivity;

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

import java.util.ArrayList;

public class DestinationList extends Fragment {
    ListView listView;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> descriptions = new ArrayList<String>();
    CustomAdapter customAdapter;
    public void setDestination(String name, String address){
        names.add(name);
        descriptions.add(address);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_destination_list, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        listView = (ListView) view.findViewById(R.id.listview);
        customAdapter = new CustomAdapter();
        names.add("Centre Pompidou");
        descriptions.add("Place Georges-Pompidou, 75004 Paris, France");
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent(getContext(), MoreDetailsActivity.class);
                startActivity(mIntent);
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.size(); // SIZE OF THE DATA
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
            view = getLayoutInflater().inflate(R.layout.destination_item_layout,null);
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView textView_name = (TextView)view.findViewById(R.id.textDestinationName);
            TextView textView_description = (TextView)view.findViewById(R.id.textDestinationDescription);

//            imageView.setImageResource(IMAGES[i]);
            textView_name.setText(names.get(i));
            textView_description.setText(descriptions.get(i));

            return view;
        }
    }

}
