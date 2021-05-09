package com.example.belfastinanutshell.Businesses;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Home;
import com.example.belfastinanutshell.MainActivity;
import com.example.belfastinanutshell.Model.Businesses;
import com.example.belfastinanutshell.Posts.AddNewPost;
import com.example.belfastinanutshell.Posts.All_Posts;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.Profile.Profile;
import com.example.belfastinanutshell.R;
import com.example.belfastinanutshell.ViewHolder.BusinessViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class All_Bars extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference BusinessesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolBar;
    private View rootView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_businesses);

        //Toolbar (at top of each page)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);
        BusinessesRef = FirebaseDatabase.getInstance().getReference().child("Businesses");
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

        userNameTextView.setText(Prevalent.CurrentOnlineUser.getFullName());
        Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Businesses> options =
                new FirebaseRecyclerOptions.Builder<Businesses>()
                        .setQuery(BusinessesRef.orderByChild("category").equalTo("Bars"), Businesses.class)
                        .build();


        FirebaseRecyclerAdapter<Businesses, BusinessViewHolder> adapter =
                new FirebaseRecyclerAdapter<Businesses, BusinessViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BusinessViewHolder holder, int position, @NonNull Businesses model) {
                        holder.txtBusinessName.setText(model.getbName());
                        holder.txtBusinessDescription.setText(model.getDescription());
                        holder.txtBusinessLocation.setText(model.getLocation());
                        holder.txtBusinessRating.setText(model.getRating());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(All_Bars.this, BusinessDetails.class);
                                intent.putExtra("bID", model.getbID());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_items_layout, parent, false);
                        BusinessViewHolder holder = new BusinessViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

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
        getMenuInflater().inflate(R.menu.all_businesses, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Intent to start Home Activity
            Intent intent = new Intent(All_Bars.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            //Intent to start Profile Activity
            Intent intent = new Intent(All_Bars.this, Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            //Intent to start search business Activity
            Intent intent = new Intent(All_Bars.this, SearchBusinessActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bars) {
            //Start the same Activity - aka refreshing
            Intent refresh = new Intent(this, All_Bars.class);
            startActivity(refresh);
            finish();
        } else if (id == R.id.nav_restaurants) {
            //Intent to start restaurants Activity
            Intent intent = new Intent(All_Bars.this, All_Restaurants.class);
            startActivity(intent);
        } else if (id == R.id.nav_entertainment) {
            //Intent to start all entertainment businesses Activity
            Intent intent = new Intent(All_Bars.this, All_Entertainment.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_blog) {
            //Intent to start add a blog Activity
            Intent intent = new Intent(All_Bars.this, AddNewPost.class);
            startActivity(intent);
        } else if (id == R.id.nav_all_blogs) {
            //Intent to start blogs list activity
            Intent intent = new Intent(All_Bars.this, All_Posts.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            //Intent to logout and destroy book method storing the current logged users details
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            Intent intent = new Intent(All_Bars.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}