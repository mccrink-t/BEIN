package com.example.belfastinanutshell.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.belfastinanutshell.Businesses.SearchBusinessActivity;
import com.example.belfastinanutshell.MainActivity;
import com.example.belfastinanutshell.Posts.All_Posts;
import com.example.belfastinanutshell.R;

public class AdminHome extends AppCompatActivity{

    private CardView addBar, addRestaurant, addEntertainment, manageBusiness, managePost, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        addBar = (CardView) findViewById(R.id.add_bar_admin_card);
        addRestaurant = (CardView) findViewById(R.id.add_restaurant_admin_card);
        addEntertainment = (CardView) findViewById(R.id.add_entertainment_admin_card);
        manageBusiness = (CardView) findViewById(R.id.edit_delete_admin_card);
        managePost = (CardView) findViewById(R.id.manage_post_admin_card);
        logout = (CardView) findViewById(R.id.logout_admin_card);

        addBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, AdminAddNewBusiness.class);
                intent.putExtra("category", "Bars");
                startActivity(intent);
            }
        });

        addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, AdminAddNewBusiness.class);
                intent.putExtra("category", "Restaurants");
                startActivity(intent);
            }
        });

        addEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, AdminAddNewBusiness.class);
                intent.putExtra("category", "Entertainment");
                startActivity(intent);
            }
        });

        manageBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, SearchBusinessActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        managePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, All_Posts.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminHome.this, "Logging out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminHome.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}