package com.example.belfastinanutshell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminCategory extends AppCompatActivity{

    private ImageView bars, restaurants, entertainment;
    private Button updateDeleteBusinessBtn, logoutBtn;
    private Toolbar toolBar;
    private View rootView;
    private DrawerLayout drawerLayout;
    TextView userNameTextView;
    CircleImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        bars = (ImageView) findViewById(R.id.bars_logo);
        restaurants = (ImageView) findViewById(R.id.restaurants_logo);
        entertainment = (ImageView) findViewById(R.id.entertainment_logo);
        updateDeleteBusinessBtn = (Button) findViewById(R.id.category_admin_updateDelete_btn);
        logoutBtn = (Button) findViewById(R.id.admin_logout_btn);

//        //Toolbar (at top of each page)
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        final ActionBar actionbar = getSupportActionBar();
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        Paper.init(this);
//
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        View headerView = navigationView.getHeaderView(0);
//        userNameTextView = headerView.findViewById(R.id.user_profile_name);
//        profileImageView = headerView.findViewById(R.id.user_profile_image);
//        updateDeleteBusinessBtn = (Button) findViewById(R.id.category_admin_updateDelete_btn);


//        String AdminsPhoneNumber = Paper.book().read(Prevalent.AdminsPhoneNumber);
//        if (AdminsPhoneNumber != "")
//        {
//            if (!TextUtils.isEmpty(AdminsPhoneNumber))
//            {
//                adminCheck(AdminsPhoneNumber);
//            }
//        }

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

        updateDeleteBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, SearchBusinessActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminCategory.this, "Logging out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminCategory.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });




    }

//    private void adminCheck(final String phone)
//    {
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                if (snapshot.child("Admin").child(phone).exists())
//                {
//                    Admins usersData = snapshot.child("Admin").child(phone).getValue(Admins.class);
//
//                    if (usersData.getPhone().equals(phone))
//                    {
//                        View adminPanel;
//                        adminPanel = findViewById(R.id.adminNavGroup);
//                        adminPanel.setVisibility(View.VISIBLE);
//                        userNameTextView.setText("Admin User");
//                        profileImageView.setImageResource(R.drawable.profile);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.admincategory, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//
//        } else if (id == R.id.nav_profile) {
//            Intent intent = new Intent(AdminCategory.this, Profile.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_add_business) {
//            Intent intent = new Intent(AdminCategory.this, AdminAddNewBusiness.class);
//            startActivity(intent);
//        }else if (id == R.id.nav_search) {
//            Intent intent = new Intent(AdminCategory.this, SearchActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_bars) {
//            Intent intent = new Intent(AdminCategory.this, All_Bars.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_restaurants) {
//            Intent intent = new Intent(AdminCategory.this, All_Businesses.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_entertainment) {
//
//        } else if (id == R.id.nav_logout) {
//            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
//            Paper.book().destroy();
//            Intent intent = new Intent(AdminCategory.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}