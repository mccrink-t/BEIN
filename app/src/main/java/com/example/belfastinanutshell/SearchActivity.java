package com.example.belfastinanutshell;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Model.Businesses;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.ViewHolder.AdapterClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference ref;
    ArrayList<Businesses> list;
    RecyclerView recyclerView;
    androidx.appcompat.widget.SearchView searchView;
    private Toolbar toolBar;
    private View rootView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ref = FirebaseDatabase.getInstance().getReference().child("Businesses");
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
        searchView.onActionViewExpanded();
        searchView.setIconified(true);

        //Nav menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);

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




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref !=null)
        {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren())
                        {
                            list.add(ds.getValue(Businesses.class));
                        }
                        AdapterClass adapterClass = new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SearchActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if(searchView != null)
        {
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String s) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String s) {
//                    search(s);
//                    return true;
//                }
//            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String str)
    {
        ArrayList<Businesses> myList = new ArrayList<>();
        for(Businesses object : list)
        {
            if(object.getbName().toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(object);
            }
            AdapterClass adapter = new AdapterClass(myList);
//            adapter.setOnCallBack(new AdapterClass.OnCallBack() {
//                @Override
//                public void onDetail(Businesses businesses) {
//                    Intent intent = new Intent(SearchActivity.this, BusinessViewHolder.class);
//                    intent.putExtra("bID", businesses.getbID());
//                    startActivity(intent);
//                }
//            });
            recyclerView.setAdapter(adapter);
        }
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
            Intent intent = new Intent(SearchActivity.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(SearchActivity.this, Profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent refresh = new Intent(this, All_Bars.class);
            //Start the same Activity
            startActivity(refresh);
            finish();
        } else if (id == R.id.nav_bars) {
            Intent intent = new Intent(SearchActivity.this, All_Bars.class);
            startActivity(intent);
        } else if (id == R.id.nav_restaurants) {

        } else if (id == R.id.nav_entertainment) {

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}