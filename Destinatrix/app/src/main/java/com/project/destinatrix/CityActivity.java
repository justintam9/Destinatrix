package com.project.destinatrix;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity implements AddCityDialog.cityDialogListener{
    List<CityData> cityList;
    Integer[] images = {R.drawable.stock_image1,R.drawable.stock_image2,R.drawable.stock_image3,R.drawable.stock_image4,R.drawable.stock_image5};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cityList = new ArrayList<>();
        cityList.add(new CityData("test",getRandomImage()));
        cityList.add(new CityData("test",getRandomImage()));
        cityList.add(new CityData("test",getRandomImage()));
        cityList.add(new CityData("test",getRandomImage()));

        RecyclerView view = findViewById(R.id.recyclerview_city);
        CustomCityAdapter myAdapter = new CustomCityAdapter(this,cityList);
        view.setLayoutManager(new GridLayoutManager(this,3));
        view.setAdapter(myAdapter);

        FloatingActionButton fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCityDialog();
            }
        });
    }
    public Integer getRandomImage(){
        return images[(int)(Math.random()*(images.length))];
    }

    public void addCityDialog(){
        AddCityDialog addCityDialog = new AddCityDialog();
        addCityDialog.show(getSupportFragmentManager(), "add_City_Dialog");
    }

    @Override
    public void applyTexts(String name) {
        cityList.add(new CityData(name,getRandomImage()));
    }

}
