package com.example.belfastinanutshell.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.belfastinanutshell.Admin.AdminDeletePost;
import com.example.belfastinanutshell.Businesses.All_Bars;
import com.example.belfastinanutshell.Businesses.All_Restaurants;
import com.example.belfastinanutshell.Businesses.SearchBusinessActivity;
import com.example.belfastinanutshell.Home;
import com.example.belfastinanutshell.MainActivity;
import com.example.belfastinanutshell.Model.Posts;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.Profile.Profile;
import com.example.belfastinanutshell.R;
import com.example.belfastinanutshell.ViewHolder.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class All_Posts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText searchBarPosts;
    private ImageView searchPostBtn;
    private RecyclerView searchPostsRV;
    private String searchPostsInput;
    private DatabaseReference postsRef;
    private Toolbar toolBar;
    private View rootView;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fabIcon;

    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);

        searchBarPosts = (EditText) findViewById(R.id.searchBarPostEditText);
        searchPostBtn = (ImageView) findViewById(R.id.searchPostBtn);
        searchPostsRV = (RecyclerView) findViewById(R.id.searchPostRV);
        searchPostsRV.setLayoutManager(new LinearLayoutManager(All_Posts.this));
        fabIcon = (FloatingActionButton) findViewById(R.id.addReviewIconFab);

        searchPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPostsInput = searchBarPosts.getText().toString();
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


        fabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postReviewIntent = new Intent(All_Posts.this, AddNewPost.class);
                Toast.makeText(All_Posts.this, "From here you can add a new Post", Toast.LENGTH_SHORT).show();
                startActivity(postReviewIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("UserPosts");

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postsRef.orderByChild("postTitle").startAt(searchPostsInput),
                        Posts.class).build();

        FirebaseRecyclerAdapter<Posts, PostViewHolder>
                adapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int i, @NonNull Posts model) {
                holder.txtPostTitle.setText(model.getPostTitle());
                holder.txtPostDescription.setText(model.getPostDescription());
                holder.txtPostUsersName.setText(model.getUsersFullName());
                holder.txtPostDate.setText(model.getDate());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                //when a user clicks on a business view holder
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if statement to check if the user is an Admin
                        if (userType.equals("Admin")) {
//                          send admin to delete Post activity
                            Intent intent = new Intent(All_Posts.this, AdminDeletePost.class);
                            intent.putExtra("postID", model.getPostID());
                            intent.putExtra("postTitle", model.getPostTitle());
                            startActivity(intent);
                        }
                        //else if the user is not an Admin
                        else {
                            //send user to business details activity
                            Intent intent = new Intent(All_Posts.this, View_Post.class);
                            intent.putExtra("postID", model.getPostID());
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, parent, false);
                PostViewHolder holder = new PostViewHolder(view);
                return holder;
            }
        };

        searchPostsRV.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.all_posts, menu);
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
            Intent intent = new Intent(All_Posts.this, Home.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(All_Posts.this, Profile.class);
            startActivity(intent);

        } else if (id == R.id.nav_search) {
            Intent refresh = new Intent(All_Posts.this, SearchBusinessActivity.class);
            //Start the same Activity
            startActivity(refresh);
            finish();


        } else if (id == R.id.nav_bars) {
            Intent intent = new Intent(All_Posts.this, All_Bars.class);
            startActivity(intent);

        } else if (id == R.id.nav_restaurants) {
            Intent intent = new Intent(All_Posts.this, All_Restaurants.class);
            startActivity(intent);

        } else if (id == R.id.nav_entertainment) {

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            Paper.book().destroy();
            Intent intent = new Intent(All_Posts.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}