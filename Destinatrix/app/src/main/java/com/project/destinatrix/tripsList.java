package com.project.destinatrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class tripsList extends AppCompatActivity {
    private ListView list;
    ArrayList<tripData>  tripList;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        getSupportActionBar().setTitle("Trips");

        tripList = new ArrayList<>();
        tripList.add(new tripData("Test", "Test", getRandomImage()));
        list = (ListView) findViewById(R.id.listview);
        customTripAdapter adapter = new customTripAdapter(this, tripList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent(tripsList.this, tripsList.class);
                startActivity(mIntent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent edit = new Intent(this, tripsList.class);
                startActivity(edit);
            case R.id.menu_sign_out:
                Intent sign_out = new Intent(this, tripsList.class);
                startActivity(sign_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trips_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public Integer getRandomImage(){
        return images[(int)(Math.random()*(images.length+1))];
    }
}

