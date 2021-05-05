package com.example.belfastinanutshell.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.Model.Posts;
import com.example.belfastinanutshell.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class View_Post extends AppCompatActivity {

    private TextView pTitleView, pTextView, pUserNameTextView, totalPostRating, totalPostReviews, postDateView, postTimeView;
    private ImageView imagePost;
    private TextView closeBtn;
    private String postID = "";
    private Button addReviewBtn;
    private DatabaseReference postDetailsRef, postRatingRef;
    private String Post_Key;
    private String Post_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        //grab post ID from previous activity
        postID = getIntent().getStringExtra("postID");

        pTitleView = (TextView) findViewById(R.id.postTitle_Post_View);
        pTextView = (TextView) findViewById(R.id.text_Post_View);
        pUserNameTextView = (TextView) findViewById(R.id.users_Name_Post_View);
        addReviewBtn = (Button) findViewById(R.id.post_comment_btn);
        totalPostRating = (TextView) findViewById(R.id.textTotalPostRating);
        totalPostReviews = (TextView) findViewById(R.id.textTotalPostReviews);
        closeBtn = (TextView) findViewById(R.id.close_post_view_btn);
        imagePost = (ImageView) findViewById(R.id.image_post_view);
        postDateView = (TextView) findViewById(R.id.date_Post_View);
        postTimeView = (TextView) findViewById(R.id.time_Post_View);

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
                    postDateView.setText(posts.getDate());
                    postTimeView.setText(posts.getTime());
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

        //open activity to view reviews and add review. Taking over the post name and post ID from the key fields
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent postReviewIntent = new Intent(View_Post.this, PostReviewsActivity.class);
                postReviewIntent.putExtra("postID", Post_Key);
                postReviewIntent.putExtra("postTitle", Post_Title);
                startActivity(postReviewIntent);

            }
        });

    }

}