package com.example.belfastinanutshell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategory extends AppCompatActivity {

    private ImageView bars, restaurants, entertainment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        bars = (ImageView) findViewById(R.id.bars_logo);
        restaurants = (ImageView) findViewById(R.id.restaurants_logo);
        entertainment = (ImageView) findViewById(R.id.entertainment_logo);

        bars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewBusiness.class);
                intent.putExtra("category", "Bars");
                startActivity(intent);
            }
        });

        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewBusiness.class);
                intent.putExtra("category", "Restaurants");
                startActivity(intent);
            }
        });

        entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewBusiness.class);
                intent.putExtra("category", "Entertainment");
                startActivity(intent);
            }
        });
    }
}