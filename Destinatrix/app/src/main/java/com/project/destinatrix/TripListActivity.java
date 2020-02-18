package com.project.destinatrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class TripListActivity extends AppCompatActivity {
    private ListView list;
    ArrayList<TripData>  tripList;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        getSupportActionBar().setTitle("Trips");

        tripList = new ArrayList<>();
        tripList.add(new TripData("Test", "Test", getRandomImage()));
        list = (ListView) findViewById(R.id.listview);
        CustomTripAdapter adapter = new CustomTripAdapter(this, tripList);
        list.setAdapter(adapter);

        // TODO: Change intent to open correct screen.
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent mIntent = new Intent(TripListActivity.this, TripListActivity.class);
//                startActivity(mIntent);
//            }
//        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent edit = new Intent(this, TripListActivity.class);
                startActivity(edit);
            case R.id.menu_sign_out:
                Intent sign_out = new Intent(this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
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
        return images[(int)(Math.random()*(images.length))];
    }
}

