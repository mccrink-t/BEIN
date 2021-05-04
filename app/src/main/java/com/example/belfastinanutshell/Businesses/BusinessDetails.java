package com.example.belfastinanutshell.Businesses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.MapsActivity;
import com.example.belfastinanutshell.Model.Businesses;
import com.example.belfastinanutshell.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class BusinessDetails extends AppCompatActivity {

    private TextView bNameDetails, descriptionDetails, locationDetails, openingHrsDetails, contactInfoDetails, websiteDetails, totalBusinessRating, totalBusinessReviews;
    private ImageView imageDetails;
    private TextView closeBtn, locateBtn;
    private String businessID = "";
    private Button addReviewBtn;
    private DatabaseReference businessDetailsRef, businessRatingRef;
    private String Business_Key;
    private String Business_Name;

    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        //grab business ID from previous activity
        businessID = getIntent().getStringExtra("bID");

        bNameDetails = (TextView) findViewById(R.id.name_business_details);
        descriptionDetails = (TextView) findViewById(R.id.description_business_details);
        locationDetails = (TextView) findViewById(R.id.location_business_details);
        openingHrsDetails = (TextView) findViewById(R.id.opening_hours_business_details);
        contactInfoDetails = (TextView) findViewById(R.id.contact_info_business_details);
        websiteDetails = (TextView) findViewById(R.id.website_business_details);
        imageDetails = (ImageView) findViewById(R.id.image_business_details);
        addReviewBtn = (Button) findViewById(R.id.business_review_btn);
        locateBtn = (Button) findViewById(R.id.locate_business_btn);
        totalBusinessRating = (TextView) findViewById(R.id.textTotalBusinessRating);
        totalBusinessReviews = (TextView) findViewById(R.id.textTotalBusinessReviewsText);
        closeBtn = (TextView) findViewById(R.id.close_single_business_details_btn);

        //call information from business database firebase
        businessDetailsRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(businessID);
        businessRatingRef = businessDetailsRef.child("Reviews");

        getBusinessDetails();
    }

    private void getBusinessDetails() {
        businessDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Businesses businesses = snapshot.getValue(Businesses.class);

                    Business_Key = businesses.getbID();
                    Business_Name = businesses.getbName();

                    bNameDetails.setText(businesses.getbName());
                    descriptionDetails.setText(businesses.getDescription());
                    locationDetails.setText(businesses.getLocation());
                    openingHrsDetails.setText(businesses.getOpeningHrs());
                    contactInfoDetails.setText(businesses.getContactInfo());
                    websiteDetails.setText(businesses.getWebsite());
                    Picasso.get().load(businesses.getImage()).into(imageDetails);

                    businessRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                //works out the average rating of the business
                                ratingsAverage = ratingSum / numberOfRatings;

                                totalBusinessReviews.setText(String.format("%2.0f", numberOfRatings));
//                              convert to a string and set text to new value of 1 decimal place
                                totalBusinessRating.setText(String.format("%2.1f", ratingsAverage));

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                //convert the current business rating to the new calculated rating
                                HashMap<String, Object> businessMap = new HashMap<>();
                                businessMap.put("rating", totalBusinessRating.getText().toString());
                                businessDetailsRef.updateChildren(businessMap);

                            } else {
                                totalBusinessRating.setText("Not Yet Rated");
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
                Intent businessReviewIntent = new Intent(BusinessDetails.this, BusinessReviewsActivity.class);
                businessReviewIntent.putExtra("bID", Business_Key);
                businessReviewIntent.putExtra("bName", Business_Name);
                startActivity(businessReviewIntent);
            }
        });

        locateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent businessMapIntent = new Intent(BusinessDetails.this, MapsActivity.class);
                businessMapIntent.putExtra("bName", Business_Name);
                startActivity(businessMapIntent);
            }
        });
    }
}