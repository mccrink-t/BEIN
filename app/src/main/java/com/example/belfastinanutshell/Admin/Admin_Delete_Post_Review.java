package com.example.belfastinanutshell.Admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.Model.PostReviewsModel;
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

public class Admin_Delete_Post_Review extends AppCompatActivity {

    private TextView pNameDetails, usersNamePostReview, usersCommentPostReview, usersRatingPostReview, datePostReview, timePostReview;
    private ImageView postImageReview;
    private TextView closeBtn;
    private String postID = "";
    private String postReviewID = "";
    private Button deletePostReviewBtn;
    private DatabaseReference postDetailsRef, reviewDetailsRef;
    private String Post_Key;
    private String Post_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_post_review);

        //grab post ID from previous activity
        Post_Key = getIntent().getStringExtra("postID");
        Post_Title = getIntent().getExtras().get("postTitle").toString();

        //grab post Review ID from previous activity
        postReviewID = getIntent().getStringExtra("reviewID");

        pNameDetails = (TextView) findViewById(R.id.title_post_details_review);
        usersNamePostReview = (TextView) findViewById(R.id.userName_post_review_details);
        usersCommentPostReview = (TextView) findViewById(R.id.comment_post_review_details);
        usersRatingPostReview = (TextView) findViewById(R.id.rating_post_review_details);
        datePostReview = (TextView) findViewById(R.id.date_post_review_details);
        timePostReview = (TextView) findViewById(R.id.time_post_review_details);
        postImageReview = (ImageView) findViewById(R.id.image_post_details_review);
        deletePostReviewBtn = (Button) findViewById(R.id.delete_post_review_btn);
        closeBtn = (TextView) findViewById(R.id.close_single_post_review_admin_btn);

        //call information from business review database firebase
        postDetailsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_Key);
        reviewDetailsRef = postDetailsRef.child("Reviews").child(postReviewID);
        getPostReviewDetails();
        getPostDetails();

    }

    private void getPostDetails() {
        postDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Posts post = snapshot.getValue(Posts.class);
                    pNameDetails.setText(post.getPostTitle());
                    Picasso.get().load(post.getImage()).into(postImageReview);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostReviewDetails() {
        reviewDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PostReviewsModel review = snapshot.getValue(PostReviewsModel.class);

                    usersNamePostReview.setText(review.getFullName());
                    usersCommentPostReview.setText(review.getReview());
                    usersRatingPostReview.setText(review.getReview());
                    datePostReview.setText(review.getDate());
                    timePostReview.setText(review.getTime());
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
        deletePostReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deletePostReview();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Delete_Post_Review.this);
                builder.setMessage("Are you sure you want to delete this review?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    private void deletePostReview()
    {
        reviewDetailsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                finish();

                Toast.makeText(Admin_Delete_Post_Review.this, "Review Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


}