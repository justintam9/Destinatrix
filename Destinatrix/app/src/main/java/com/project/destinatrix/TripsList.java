package com.project.destinatrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class TripsList extends AppCompatActivity implements TripDialog.tripDialogListener{
    private ListView list;
    ArrayList<TripData>  tripList;
    private CustomTripAdapter adapter;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        getSupportActionBar().setTitle("Trips");

        tripList = new ArrayList<>();
        list = findViewById(R.id.listview);
        adapter = new CustomTripAdapter(this, tripList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(i);
                System.out.println(l);
            }
        });

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(list),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                adapter.remove(position);
                            }
                        });

        list.setOnTouchListener(touchListener);
        list.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(TripsList.this, "Position " + position, LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    @Override
    public void applyTexts(String name, String description) {
        tripList.add(new TripData(name,description,getRandomImage()));
    }

    public void openDialog(){
        TripDialog tripDialog = new TripDialog();
        tripDialog.show(getSupportFragmentManager(), "trip Dialog");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent edit = new Intent(this, TripsList.class);
                startActivity(edit);
            case R.id.menu_sign_out:
                Intent sign_out = new Intent(this, TripsList.class);
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

