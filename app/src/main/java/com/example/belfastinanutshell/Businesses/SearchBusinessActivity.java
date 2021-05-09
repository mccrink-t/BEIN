package com.example.belfastinanutshell.Businesses;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.example.belfastinanutshell.Admin.AdminEditBusiness;
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

public class SearchBusinessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText searchBarBusiness;
    private ImageView searchBusinessBtn;
    private RecyclerView searchBusinessRV;
    private String searchBusinessInput;
    private DatabaseReference businessesRef;
    private Toolbar toolBar;
    private View rootView;
    private DrawerLayout drawerLayout;

    private Button orderByName, orderByRating;

    //text to be shown if admins logged in
    private TextView closeBtn, logoutAdminBtn;

    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_business);

        searchBarBusiness = (EditText) findViewById(R.id.searchBarBusinessEditText);
        searchBusinessBtn = (ImageView) findViewById(R.id.searchBusinessBtn);
        searchBusinessRV = (RecyclerView) findViewById(R.id.searchBusinessRV);

        orderByName = (Button) findViewById(R.id.orderByNameBtn);
        orderByRating = (Button) findViewById(R.id.orderByRatingBtn);

        closeBtn = (TextView) findViewById(R.id.searchBusinessAdminCloseBtn);
        logoutAdminBtn = (TextView) findViewById(R.id.searchBusinessAdminLogoutBtn);

        searchBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBusinessInput = searchBarBusiness.getText().toString();
                onStart();
            }
        });

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

        //intent to ensure that if user comes from admin activity it grabs the admin info
        //otherwise it does not look for the admin info, thus allowing end user to log in
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //grabs Admin info intent on category page
            //usertype stores admin value
            userType = getIntent().getExtras().get("Admin").toString();
        }

        //if the end user opens the search activity display their username and profile image in the nav bar menu
        if (!userType.equals("Admin")) {
            userNameTextView.setText(Prevalent.CurrentOnlineUser.getFullName());
            Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }
        //else if its an admin change the name to admin account
        else {
            userNameTextView.setText("Admin");
        }

        orderByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderByBusinessName();
            }
        });

        orderByRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderByBusinessRating();
            }
        });

    }

    //method to order all the businesses by highest name
    private void OrderByBusinessName() {
        businessesRef = FirebaseDatabase.getInstance().getReference().child("Businesses");

        FirebaseRecyclerOptions<Businesses> options =
                new FirebaseRecyclerOptions.Builder<Businesses>().setQuery(businessesRef.orderByChild("bName").startAt(searchBusinessInput),
                        Businesses.class).build();

        FirebaseRecyclerAdapter<Businesses, BusinessViewHolder>
                adapter = new FirebaseRecyclerAdapter<Businesses, BusinessViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BusinessViewHolder holder, int i, @NonNull Businesses model) {
                holder.txtBusinessName.setText(model.getbName());
                holder.txtBusinessDescription.setText(model.getDescription());
                holder.txtBusinessLocation.setText(model.getLocation());
                holder.txtBusinessRating.setText(model.getRating());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                //when a user clicks on a business view holder
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if statement to check if the user is an Admin
                        if (userType.equals("Admin")) {
//                          send admin to edit business activity
                            Intent intent = new Intent(SearchBusinessActivity.this, AdminEditBusiness.class);
                            intent.putExtra("bID", model.getbID());
                            intent.putExtra("bName", model.getbName());
                            startActivity(intent);
                        }
                        //else if the user is not an Admin
                        else {
                            //send user to business details activity
                            Intent intent = new Intent(SearchBusinessActivity.this, BusinessDetails.class);
                            intent.putExtra("bID", model.getbID());
                            startActivity(intent);
                        }
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

        searchBusinessRV.setLayoutManager(new LinearLayoutManager(SearchBusinessActivity.this));
        searchBusinessRV.setAdapter(adapter);
        adapter.startListening();
    }

    //method to order all the businesses by highest rated to lowest
    private void orderByBusinessRating() {
        businessesRef = FirebaseDatabase.getInstance().getReference().child("Businesses");

        FirebaseRecyclerOptions<Businesses> options =
                new FirebaseRecyclerOptions.Builder<Businesses>()
                        .setQuery(businessesRef.orderByChild("rating").startAt(searchBusinessInput),
                                Businesses.class).build();

        FirebaseRecyclerAdapter<Businesses, BusinessViewHolder>
                adapter = new FirebaseRecyclerAdapter<Businesses, BusinessViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BusinessViewHolder holder, int i, @NonNull Businesses model) {
                holder.txtBusinessName.setText(model.getbName());
                holder.txtBusinessDescription.setText(model.getDescription());
                holder.txtBusinessLocation.setText(model.getLocation());
                holder.txtBusinessRating.setText(model.getRating());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                //when a user clicks on a business view holder
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if statement to check if the user is an Admin
                        if (userType.equals("Admin")) {
//                          send admin to edit business activity
                            Intent intent = new Intent(SearchBusinessActivity.this, AdminEditBusiness.class);
                            intent.putExtra("bID", model.getbID());
                            intent.putExtra("bName", model.getbName());
                            startActivity(intent);
                        }
                        //else if the user is not an Admin
                        else {
                            //send user to business details activity
                            Intent intent = new Intent(SearchBusinessActivity.this, BusinessDetails.class);
                            intent.putExtra("bID", model.getbID());
                            startActivity(intent);
                        }
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

        //reverse the order of the recyclerview, due to it laying it out by lowest rating to highest
        //through doing this, the highest rated business will be at the top of the list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        searchBusinessRV.setLayoutManager(linearLayoutManager);

        searchBusinessRV.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();

        businessesRef = FirebaseDatabase.getInstance().getReference().child("Businesses");

        FirebaseRecyclerOptions<Businesses> options =
                new FirebaseRecyclerOptions.Builder<Businesses>().setQuery(businessesRef.orderByChild("bName").startAt(searchBusinessInput),
                        Businesses.class).build();

        FirebaseRecyclerAdapter<Businesses, BusinessViewHolder>
                adapter = new FirebaseRecyclerAdapter<Businesses, BusinessViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BusinessViewHolder holder, int i, @NonNull Businesses model) {
                holder.txtBusinessName.setText(model.getbName());
                holder.txtBusinessDescription.setText(model.getDescription());
                holder.txtBusinessLocation.setText(model.getLocation());
                holder.txtBusinessRating.setText(model.getRating());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                //when a user clicks on a business view holder
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if statement to check if the user is an Admin
                        if (userType.equals("Admin")) {
//                          send admin to edit business activity
                            Intent intent = new Intent(SearchBusinessActivity.this, AdminEditBusiness.class);
                            intent.putExtra("bID", model.getbID());
                            intent.putExtra("bName", model.getbName());
                            startActivity(intent);
                        }
                        //else if the user is not an Admin
                        else {
                            //send user to business details activity
                            Intent intent = new Intent(SearchBusinessActivity.this, BusinessDetails.class);
                            intent.putExtra("bID", model.getbID());
                            startActivity(intent);
                        }
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

        searchBusinessRV.setLayoutManager(new LinearLayoutManager(SearchBusinessActivity.this));
        searchBusinessRV.setAdapter(adapter);
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


    //if cases used to check if user is admin
    //restricts admin from using most of the features on the nav bar as these are setup for end users
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //If statement to check that the current user is not an admin, For Users an Intent to start the Home Activity
            if (!userType.equals("Admin")) {
                Intent intent = new Intent(SearchBusinessActivity.this, Home.class);
                startActivity(intent);
            }
            //If statement to check that the current user is not an admin, For Users an Intent to start the Profile Activity
        } else if (id == R.id.nav_profile) {
            if (!userType.equals("Admin")) {
                Intent intent = new Intent(SearchBusinessActivity.this, Profile.class);
                startActivity(intent);
            }
            //If statement to check that the current user is not an admin, For Users an Intent to refresh the activity
        } else if (id == R.id.nav_search) {
            if (!userType.equals("Admin")) {
                Intent refresh = new Intent(this, SearchBusinessActivity.class);
                //Start the same Activity
                startActivity(refresh);
                finish();
            }
            //If statement to check that the current user is not an admin, For Users an Intent to start the Bars Activity
        } else if (id == R.id.nav_bars) {
            if (!userType.equals("Admin")) {
                Intent intent = new Intent(SearchBusinessActivity.this, All_Bars.class);
                startActivity(intent);
            }
            //If statement to check that the current user is not an admin, For Users an Intent to start the Restaurants Activity
        } else if (id == R.id.nav_restaurants) {
            if (!userType.equals("Admin")) {
                Intent intent = new Intent(SearchBusinessActivity.this, All_Restaurants.class);
                startActivity(intent);
            }
            //If statement to check that the current user is not an admin, For Users an Intent to start the All Entertainment Businesses Activity
        } else if (id == R.id.nav_entertainment) {
            if (!userType.equals("Admin")) {
                Intent intent = new Intent(SearchBusinessActivity.this, All_Entertainment.class);
                startActivity(intent);
            }
            //If statement to check that the current user is not an admin, For Users an Intent to start the Add Blog Activity
        } else if (id == R.id.nav_add_blog) {
            if (!userType.equals("Admin")) {
                Intent intent = new Intent(SearchBusinessActivity.this, AddNewPost.class);
                startActivity(intent);
            }
            //If statement to check that the current user is not an admin, For Users an Intent to start the All Blogs Activity
        } else if (id == R.id.nav_all_blogs) {
            if (!userType.equals("Admin")) {
                Intent intent = new Intent(SearchBusinessActivity.this, All_Posts.class);
                startActivity(intent);
            }
            //Intent to logout and destroy book method storing the current logged users details
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            Intent intent = new Intent(SearchBusinessActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}