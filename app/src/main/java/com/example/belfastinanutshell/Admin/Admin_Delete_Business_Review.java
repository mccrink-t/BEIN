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

import com.example.belfastinanutshell.Model.BusinessReviewsModel;
import com.example.belfastinanutshell.Model.Businesses;
import com.example.belfastinanutshell.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Admin_Delete_Business_Review extends AppCompatActivity {

    private TextView bNameDetails, usersNameReview, usersCommentReview, usersRatingReview, dateReview, timeReview;
    private ImageView imageDetails;
    private TextView closeBtn;
    private String businessID = "";
    private String businessReviewID = "";
    private Button deleteBusinessReviewBtn;
    private DatabaseReference businessDetailsRef, reviewDetailsRef;
    private String Business_Key;
    private String Business_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_business_review);

        //grab business ID from previous activity
        Business_Key = getIntent().getStringExtra("bID");
        Business_Name = getIntent().getExtras().get("bName").toString();

        //grab business Review ID from previous activity
        businessReviewID = getIntent().getStringExtra("reviewID");

        bNameDetails = (TextView) findViewById(R.id.name_business_details_review);
        usersNameReview = (TextView) findViewById(R.id.userName_business_review_details);
        usersCommentReview = (TextView) findViewById(R.id.comment_business_review_details);
        usersRatingReview = (TextView) findViewById(R.id.rating_business_review_details);
        dateReview = (TextView) findViewById(R.id.date_business_review_details);
        timeReview = (TextView) findViewById(R.id.time_business_review_details);
        imageDetails = (ImageView) findViewById(R.id.image_business_details_review);
        deleteBusinessReviewBtn = (Button) findViewById(R.id.delete_business_review_btn);
        closeBtn = (TextView) findViewById(R.id.close_single_business_review_admin_btn);

        //call information from business review database firebase
        businessDetailsRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(Business_Key);
        reviewDetailsRef = businessDetailsRef.child("Reviews").child(businessReviewID);
        getBusinessReviewDetails();
        getBusinessDetails();

    }

    private void getBusinessDetails() {
        businessDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Businesses business = snapshot.getValue(Businesses.class);
                    bNameDetails.setText(business.getbName());
                    Picasso.get().load(business.getImage()).into(imageDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getBusinessReviewDetails() {
        reviewDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    BusinessReviewsModel reviews = snapshot.getValue(BusinessReviewsModel.class);

                    usersNameReview.setText(reviews.getFullName());
                    usersCommentReview.setText(reviews.getReview());
                    usersRatingReview.setText(reviews.getRating());
                    dateReview.setText(reviews.getDate());
                    timeReview.setText(reviews.getTime());
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
        deleteBusinessReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteBusinessReview();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Delete_Business_Review.this);
                builder.setMessage("Are you sure you want to delete this review?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    private void deleteBusinessReview()
    {
        reviewDetailsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                finish();

                Toast.makeText(Admin_Delete_Business_Review.this, "Review Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


}