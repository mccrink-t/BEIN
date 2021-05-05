package com.example.belfastinanutshell.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Home;
import com.example.belfastinanutshell.Model.PostReviewsModel;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.R;
import com.example.belfastinanutshell.ViewHolder.PostReviewsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PostReviewsActivity extends AppCompatActivity {

    private ImageView postReviewBtn;
    private EditText postReviewInputText;
    private RecyclerView postReviewList;
    private TextView closeBtn, postReviewTitle;
    private RatingBar singleUserPostRating;
    private TextView singleUserPostRatingTextView;
    private String uniquePostReviewID = null;
    private String userType = "";

    private DatabaseReference usersRef, postRef, postRatingRef;

    private String Post_Key;
    private String Post_Title;

    float ratingNumber = 0;
    int numberOfStars;
    TextView delete;
    String currentUsersID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reviews);
        uniquePostReviewID = UUID.randomUUID().toString();

        Post_Key = getIntent().getExtras().get("postID").toString();
        Post_Title = getIntent().getExtras().get("postTitle").toString();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("UserPosts").child(Post_Key).child("Reviews");
        postRatingRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("UserPosts").child(Post_Key);

        postReviewList = (RecyclerView) findViewById(R.id.postReviewList);
        postReviewList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postReviewList.setLayoutManager(linearLayoutManager);

        postReviewInputText = (EditText) findViewById(R.id.postReviewInput);
        postReviewBtn = (ImageView) findViewById(R.id.submitPostReviewBtn);
        closeBtn = (TextView) findViewById(R.id.close_single_post_reviews_btn);
        postReviewTitle = (TextView) findViewById(R.id.review_Post_Title);
        singleUserPostRating = (RatingBar) findViewById(R.id.singleUserPostRating);
        singleUserPostRatingTextView = (TextView) findViewById(R.id.singleUserPostRatingText);

//        deletePostReview = (Button) findViewById(R.id.delete_post_review_btn);

        delete = (TextView) findViewById(R.id.temporaryRatingHolder);

        postReviewTitle.setText(Post_Title);

        singleUserPostRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingNumber = singleUserPostRating.getRating();
                numberOfStars = singleUserPostRating.getNumStars();
                singleUserPostRatingTextView.setText(String.format("%2.1f", ratingNumber));
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String fullName = snapshot.child("fullName").getValue().toString();
                            String userID = snapshot.child("phone").getValue().toString();

                            ValidatePostReview(fullName, userID);
                            postReviewInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PostReviewsModel> options =
                new FirebaseRecyclerOptions.Builder<PostReviewsModel>()
                        .setQuery(postRef, PostReviewsModel.class)
                        .build();

        FirebaseRecyclerAdapter<PostReviewsModel, PostReviewsViewHolder> adapter =
                new FirebaseRecyclerAdapter<PostReviewsModel, PostReviewsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostReviewsViewHolder holder, int position, @NonNull PostReviewsModel model) {
                        holder.usersPostReviewName.setText(model.getFullName());
                        holder.usersPostReviewText.setText(model.getReview());
                        holder.usersPostReviewDate.setText(model.getDate());
                        holder.usersPostReviewTime.setText(model.getTime());
                        holder.usersPostRating.setText(model.getRating());

                        if (model.getUserID() != null) {
                            currentUsersID = model.getUserID();
                        }
                    }

                    @NonNull
                    @Override
                    public PostReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_reviews_layout, parent, false);
                        PostReviewsViewHolder holder = new PostReviewsViewHolder(view);
                        return holder;
                    }
                };

        postReviewList.setAdapter(adapter);
        adapter.startListening();
    }

    //    private void deleteReview()
//    {
//
//        postRef.child(reviewID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                Intent intent = new Intent(PostReviewsActivity.this, AdminEditPost.class);
//                intent.putExtra("bID", Post_Key);
//                startActivity(intent);
//                finish();
//
//                Toast.makeText(PostReviewsActivity.this, "Review Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void ValidatePostReview(String fullName, String userID) {
        String postReviewText = postReviewInputText.getText().toString();
        String saveCurrentDate, saveCurrentTime;

        int ratingToInt = 0;
        //convert to int and round value to 0 decimal places
//        int ratingInt = Math.round(Integer.valueOf(singleUserPostRatingTextView.getText().toString()));
//        convert back to string

        if(currentUsersID != null && currentUsersID.equals(userID)){
            Toast.makeText(this, "Post Unsuccessful, You have already Reviewed this Post.", Toast.LENGTH_SHORT).show();
        }
        else if (singleUserPostRatingTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Review Unsuccessful : Please Leave a Star Rating", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(postReviewText)) {
            Toast.makeText(this, "Review Unsuccessful : Please Write Your Review to submit", Toast.LENGTH_SHORT).show();
        }

//        else if (userReviewCheck.equals(userID)) {
//            Toast.makeText(this, "Post Unsuccessful, You have already Reviewed this Post.", Toast.LENGTH_SHORT).show();
//        }
        else {

            //converted the rating to an integer (Removing decimals)
            ratingToInt = Math.round(ratingNumber);
            //converted integer to string
            String rating = String.valueOf(ratingToInt);

            Calendar calendar = Calendar.getInstance();
            //method to grab the current date
            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            //method to grab the current time
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());

//            randomIdGeneration = saveCurrentDate + saveCurrentTime;
            //code to generate a random Id for each new Post Review

            HashMap postReviewsMap = new HashMap();
            //mapping the values inside the variables to go into the fields on the post review table
            postReviewsMap.put("reviewID", uniquePostReviewID);
            postReviewsMap.put("userID", userID);
            postReviewsMap.put("review", postReviewText);
            postReviewsMap.put("date", saveCurrentDate);
            postReviewsMap.put("time", saveCurrentTime);
            postReviewsMap.put("fullName", fullName);
            postReviewsMap.put("rating", rating);

            //update the database table posts, and the selected post, to add the new data into it via the hashmap
            postRef.child(uniquePostReviewID).updateChildren(postReviewsMap)
                    .addOnCompleteListener(new OnCompleteListener() {

                        @Override
                        public void onComplete(@NonNull Task task) {
                            //if the review was submitted successfully
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(PostReviewsActivity.this, Home.class);
                                startActivity(intent);
                                Toast.makeText(PostReviewsActivity.this, "Review was added to " + Post_Title + " successfully!", Toast.LENGTH_SHORT).show();
                            }
                            //else if an error occured
                            else {
                                Toast.makeText(PostReviewsActivity.this, "Error Occurred, Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}