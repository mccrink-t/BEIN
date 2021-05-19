package com.example.belfastinanutshell;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.belfastinanutshell.Businesses.All_Bars;
import com.example.belfastinanutshell.Businesses.All_Entertainment;
import com.example.belfastinanutshell.Businesses.All_Restaurants;
import com.example.belfastinanutshell.Businesses.SearchBusinessActivity;
import com.example.belfastinanutshell.Posts.AddNewPost;
import com.example.belfastinanutshell.Posts.All_Posts;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.Profile.Profile;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolBar;
    private View rootView;
    private DrawerLayout drawerLayout;
    private TextView username;
    private CircleImageView profilePic;
    private CardView searchBusinessesHome, barsHome, profileHome, restaurantHome, entertainmentHome, blogsHome, addBlogHome, wellBeingHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = (TextView) findViewById(R.id.fullname_home_page);
        profilePic = (CircleImageView) findViewById(R.id.user_profile_image_home);
        searchBusinessesHome = (CardView) findViewById(R.id.search_home_card);
        barsHome = (CardView) findViewById(R.id.bars_home_card);
        profileHome = (CardView) findViewById(R.id.profile_home_card);
        restaurantHome = (CardView) findViewById(R.id.restaurants_home_card);
        entertainmentHome = (CardView) findViewById(R.id.entertainment_home_card);
        blogsHome = (CardView) findViewById(R.id.blogs_home_card);
        addBlogHome = (CardView) findViewById(R.id.post_blog_home_card);
        wellBeingHome = (CardView) findViewById(R.id.wellBeing_home_card);

        //Toolbar (at top of each page)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);

        Paper.init(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        //set username and profile picture for nav bar
        userNameTextView.setText(Prevalent.CurrentOnlineUser.getFullName());
        Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        //set username and profile picture for home page
        username.setText(Prevalent.CurrentOnlineUser.getFullName());
        Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profilePic);


        searchBusinessesHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SearchBusinessActivity.class);
                startActivity(intent);
            }
        });

        barsHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, All_Bars.class);
                startActivity(intent);
            }
        });

        profileHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);
                startActivity(intent);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);
                startActivity(intent);
            }
        });

        restaurantHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, All_Restaurants.class);
                startActivity(intent);
            }
        });

        entertainmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, All_Entertainment.class);
                startActivity(intent);
            }
        });

        blogsHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, All_Posts.class);
                startActivity(intent);
            }
        });

        addBlogHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AddNewPost.class);
                startActivity(intent);
            }
        });

        wellBeingHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, StudentWellbeing.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

//    private void adminCheck(final String phone)
//    {
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                if (snapshot.child("Admin").child(phone).exists())
//                {
//                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);
//
//                    if (usersData.getPhone().equals(phone))
//                    {
//                        View adminPanel;
//                        adminPanel = findViewById(R.id.adminNavGroup);
//                        adminPanel.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


//    boolean method to take users input click selection and to navigate them to the selected activity
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Start the same Activity - aka refreshing
            Intent refresh = new Intent(this, Home.class);
            startActivity(refresh);
            finish();
        } else if (id == R.id.nav_profile) {
            //Intent to start Profile Activity
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        }else if (id == R.id.nav_search) {
            //Intent to start search business Activity
            Intent intent = new Intent(Home.this, SearchBusinessActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bars) {
            //Intent to start bars Activity
            Intent intent = new Intent(Home.this, All_Bars.class);
            startActivity(intent);
        } else if (id == R.id.nav_restaurants) {
            //Intent to start restaurants Activity
            Intent intent = new Intent(Home.this, All_Restaurants.class);
            startActivity(intent);
        } else if (id == R.id.nav_entertainment) {
            //Intent to start all entertainment businesses Activity
            Intent intent = new Intent(Home.this, All_Entertainment.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_blog) {
            //Intent to start add a blog Activity
            Intent intent = new Intent(Home.this, AddNewPost.class);
            startActivity(intent);
        } else if (id == R.id.nav_all_blogs) {
            //Intent to start blogs list activity
            Intent intent = new Intent(Home.this, All_Posts.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            //Intent to logout and destroy book method storing the current logged users details
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
