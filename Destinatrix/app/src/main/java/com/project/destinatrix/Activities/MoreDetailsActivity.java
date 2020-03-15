package com.project.destinatrix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.destinatrix.R;

public class MoreDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        Bundle b = getIntent().getExtras();
        if(b != null)
            System.out.println(b.getString("id"));

        Button close = findViewById(R.id.more_details_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoreDetailsActivity.super.onBackPressed();
            }
        });
    }
}
