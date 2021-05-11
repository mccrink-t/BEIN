package com.example.belfastinanutshell.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.AdminPostReviews;
import com.example.belfastinanutshell.Model.Posts;
import com.example.belfastinanutshell.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdminDeletePost extends AppCompatActivity {
    private TextView pTitleView, pTextView, pUserNameTextView, totalPostRating, totalPostReviews;
    private ImageView imagePost;
    private TextView closeBtn;
    private String postID = "";
    private String post_Title = "";
    private Button deletePostBtn, postReviewsBtn;
    private DatabaseReference postDetailsRef, postRatingRef, postsDeleteRef;
    private String Post_Key;
    private String Post_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_post);

        //grab post ID from previous activity
        postID = getIntent().getStringExtra("postID");


        pTitleView = (TextView) findViewById(R.id.admin_postTitle_Post_View);
        pTextView = (TextView) findViewById(R.id.admin_text_Post_View);
        pUserNameTextView = (TextView) findViewById(R.id.admin_users_Name_Post_View);
        deletePostBtn = (Button) findViewById(R.id.admin_delete_post_btn);
        postReviewsBtn = (Button) findViewById(R.id.admin_post_reviews_btn);
        totalPostRating = (TextView) findViewById(R.id.admin_textTotalPostRating);
        totalPostReviews = (TextView) findViewById(R.id.admin_textTotalPostReviews);
        closeBtn = (TextView) findViewById(R.id.admin_close_post_view_btn);
        imagePost = (ImageView) findViewById(R.id.admin_image_post_view);

        //call information from post database firebase
        postDetailsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("UserPosts").child(postID);
        postRatingRef = postDetailsRef.child("Reviews");

        getPostDetails();
    }

    private void getPostDetails() {
        postDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Posts posts = snapshot.getValue(Posts.class);

                    Post_Key = posts.getPostID();
                    Post_Title = posts.getPostTitle();

                    pTitleView.setText(posts.getPostTitle());
                    pTextView.setText(posts.getPostText());
                    pUserNameTextView.setText(posts.getUsersFullName());
                    Picasso.get().load(posts.getImage()).into(imagePost);

                    postRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int ratingSum = 0;
                            float numberOfRatings = 0;
                            float ratingsAverage = 0;

//                            for loop to run through children of reviews table to grab all ratings value
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                                map ratings value to an object
                                Object rating = map.get("rating");
//                                convert the string rating to an int to carry out calculations
                                int individualRating = Integer.parseInt(String.valueOf(rating));
                                ratingSum += individualRating;
                                //increment ratings by 1 through each loop, to count the number of reviews posted
                                numberOfRatings++;
                            }
                            //if rating reviews have been submitted
                            if (numberOfRatings != 0) {
                                //works out the average rating of the post
                                ratingsAverage = ratingSum / numberOfRatings;

                                totalPostReviews.setText(String.format("%2.0f", numberOfRatings));
//                              convert to a string and set text to new value of 1 decimal place
                                totalPostRating.setText(String.format("%2.1f", ratingsAverage));

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserPosts");

                                //convert the current post rating to the new calculated rating
                                HashMap<String, Object> postMap = new HashMap<>();
                                postMap.put("rating", totalPostRating.getText().toString());
                                postDetailsRef.updateChildren(postMap);

                            } else {
                                totalPostRating.setText("Not Yet Rated");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postReviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postReviewIntent = new Intent(AdminDeletePost.this, AdminPostReviews.class);
                postReviewIntent.putExtra("postID", Post_Key);
                postReviewIntent.putExtra("postTitle", Post_Title);
                startActivity(postReviewIntent);
            }
        });

        //open activity to view reviews and add review. Taking over the post name and post ID from the key fields
        deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                            deletePost();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminDeletePost.this);
                builder.setMessage("Are you sure you want to delete this post?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    private void deletePost() {
        postsDeleteRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("UserPosts").child(postID);
        postsDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(AdminDeletePost.this, AdminHome.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminDeletePost.this, "Post Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

}