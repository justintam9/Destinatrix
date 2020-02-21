package com.project.destinatrix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity implements RemoveCityDialog.cityDialogListener{
    ArrayList<CityData> cityList;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    CustomCityAdapter myAdapter;
    RecyclerView view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityList = new ArrayList<>();
        view = findViewById(R.id.recyclerview_city);

        myAdapter = new CustomCityAdapter(this,cityList);
        cityList.add(new CityData("hi",getRandomImage()));
        cityList.add(new CityData("hi",getRandomImage()));
        cityList.add(new CityData("hi",getRandomImage()));
        cityList.add(new CityData("hi",getRandomImage()));
        view.setLayoutManager(new GridLayoutManager(this,3));
        view.setAdapter(myAdapter);


        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent search = new Intent(this, SearchCityActivity.class);
                //startActivity(search);
            }
        });


    }
    public Integer getRandomImage(){
        return images[(int)(Math.random()*(images.length))];
    }


    public void remove(Integer pos) {
        myAdapter.remove(pos);
    }


}

