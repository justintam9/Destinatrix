package com.project.destinatrix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class tripsList extends AppCompatActivity {
    private ListView list;
    private CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

/*        getSupportActionBar().setTitle("Trips");

        ArrayList<SubjectData>  tripList = new ArrayList<>();
        tripList.add(new SubjectData("Test", "@drawable/destinatrix.png"));

        adapter = new CustomAdapter(this, tripList);
        list.setAdapter(adapter);

        LinearLayout layout = findViewById((R.id.list_layout));
        for(int i =0; i<adapter.getCount(); i++){
            ImageView imag = new ImageView(this);
            SubjectData trip = tripList.get(i);
            String uri = trip.image;
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            imag.setImageDrawable(res);
            layout.addView(imag);
        }
*/
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
}

