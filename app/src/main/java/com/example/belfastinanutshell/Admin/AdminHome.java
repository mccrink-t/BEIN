package com.example.belfastinanutshell.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.belfastinanutshell.All_Posts;
import com.example.belfastinanutshell.Businesses.SearchBusinessActivity;
import com.example.belfastinanutshell.MainActivity;
import com.example.belfastinanutshell.R;

public class AdminHome extends AppCompatActivity{

    private CardView addBar, addRestaurant, addEntertainment, manageBusiness;
    private Button deletePost, logoutBtn;
    private Toolbar toolBar;
    private View rootView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        addBar = (CardView) findViewById(R.id.add_bar_admin_card);
        addRestaurant = (CardView) findViewById(R.id.add_restaurant_admin_card);
        addEntertainment = (CardView) findViewById(R.id.add_entertainment_admin_card);
        manageBusiness = (CardView) findViewById(R.id.edit_delete_admin_card);
        deletePost = (Button) findViewById(R.id.admin_delete_post_btn);

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

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHome.this, All_Posts.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
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