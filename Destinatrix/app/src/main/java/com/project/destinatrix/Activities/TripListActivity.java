package com.project.destinatrix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.destinatrix.AddTripDialog;
import com.project.destinatrix.CustomTripAdapter;
import com.project.destinatrix.DataAction;
import com.project.destinatrix.EditTripDialog;
import com.project.destinatrix.FirebaseDatabaseHelper;
import com.project.destinatrix.R;
import com.project.destinatrix.ReadCallback;
import com.project.destinatrix.objects.TripData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class TripListActivity extends AppCompatActivity implements AddTripDialog.tripDialogListener, EditTripDialog.tripDialogListener {
    private ListView list;
    ArrayList<TripData>  tripList;
    private CustomTripAdapter adapter;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    private FirebaseDatabaseHelper fbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        getSupportActionBar().setTitle("Trips");

        fbHelper = new FirebaseDatabaseHelper("trips");
        TripListActivity.this.list = findViewById(R.id.listview);
        fbHelper.readData(DataAction.TripData, new ReadCallback() {
            @Override
            public void onCallBack(ArrayList<Object> list) {
                TripListActivity.this.tripList = (ArrayList<TripData>)(Object)list;
                TripListActivity.this.adapter = new CustomTripAdapter(TripListActivity.this, TripListActivity.this.tripList);
                TripListActivity.this.list.setAdapter(adapter);
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int pos, long id) {
                editTripDialog(pos);
                return true;
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
                    Intent mIntent = new Intent(TripListActivity.this, CityActivity.class);
                    mIntent.putExtra("tripID",tripList.get(position).getTripID());
                    startActivity(mIntent);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTripDialog();
            }
        });
    }

    @Override
    public void applyTexts(String name, String description) {
        TripData trip = new TripData(name,description,getRandomImage(),getCurrentTimeStamp());
        trip.setTripID(fbHelper.getDataId());
        tripList.add(trip);
        adapter.notifyDataSetChanged();
        fbHelper.createData(trip, DataAction.TripData);
    }

    public void editTexts(String name, String description, Integer pos) {
        adapter.edit(pos,name,description);
    }

    public void addTripDialog(){
        AddTripDialog addTripDialog = new AddTripDialog();
        addTripDialog.show(getSupportFragmentManager(), "add_Trip_Dialog");
    }

    public void editTripDialog(int pos){
        Bundle args = new Bundle();
        args.putInt("pos",pos);
        EditTripDialog editTripDialog = new EditTripDialog();
        editTripDialog.setArguments(args);
        editTripDialog.show(getSupportFragmentManager(), "edit_Trip_Dialog");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
}

