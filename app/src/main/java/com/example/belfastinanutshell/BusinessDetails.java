package com.example.belfastinanutshell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.Model.Businesses;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class BusinessDetails extends AppCompatActivity {

    private TextView bNameDetails, descriptionDetails, locationDetails, openingHrsDetails, contactInfoDetails, websiteDetails, totalBusinessRating;
    private ImageView imageDetails;
    private TextView closeBtn;
    private String businessID = "";
    private Button addReviewBtn;
//    private RatingBar businessRating;
    private DatabaseReference businessDetailsRef, businessRatingRef;
    private String bNameReviewTitle;
    private String Post_Key;
    private String Post_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        businessID = getIntent().getStringExtra("bID");
        bNameDetails = (TextView) findViewById(R.id.name_business_details);
        descriptionDetails = (TextView) findViewById(R.id.description_business_details);
        locationDetails = (TextView) findViewById(R.id.location_business_details);
        openingHrsDetails = (TextView) findViewById(R.id.opening_hours_business_details);
        contactInfoDetails = (TextView) findViewById(R.id.contact_info_business_details);
        websiteDetails = (TextView) findViewById(R.id.website_business_details);
        imageDetails = (ImageView) findViewById(R.id.image_business_details);
        addReviewBtn = (Button) findViewById(R.id.business_review_btn);
        totalBusinessRating =(TextView) findViewById(R.id.textTotalBusinessRating);
        closeBtn = (TextView) findViewById(R.id.close_single_business_details_btn);
//        businessRating = (RatingBar) findViewById(R.id.businessRating);



        //call information from business database firebase
        businessDetailsRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(businessID);
        businessRatingRef = businessDetailsRef.child("Reviews");

        getBusinessDetails();
    }

    private void getBusinessDetails()
    {
        businessDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if(snapshot.exists()){
                    Businesses businesses = snapshot.getValue(Businesses.class);

                    Post_Key = businesses.getbID();
                    Post_Name = businesses.getbName();

                    bNameDetails.setText(businesses.getbName());
                    descriptionDetails.setText(businesses.getDescription());
                    locationDetails.setText(businesses.getLocation());
                    openingHrsDetails.setText(businesses.getOpeningHrs());
                    contactInfoDetails.setText(businesses.getContactInfo());
                    websiteDetails.setText(businesses.getWebsite());
                    Picasso.get().load(businesses.getImage()).into(imageDetails);
//                    totalBusinessRating.setText(businesses.getRating());

                    bNameReviewTitle = businesses.getbName();

                    businessRatingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            int ratingSum =0;
                            float numberOfRatings = 0;
                            float ratingsAverage = 0;
                            int ratingAverageTotal = 0;

//                            for(DataSnapshot ds : snapshot.getChildren()){
//                                Map<String, Object> map = (Map<String, Object>)ds.getValue();
//                                Object rating = map.get("rating");
//                                int individualRating = Integer.parseInt(String.valueOf(rating));
//                                sum+=individualRating;
//
//                                totalBusinessRating.setText(String.valueOf(sum));

//                            for loop to run through children of reviews table to grab all ratings value
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                Map<String, Object> map = (Map<String, Object>) ds.getValue();
//                                map ratings value to an object
                                Object rating = map.get("rating");
//                                convert the string rating to an int to carry out calculations
                                int individualRating = Integer.parseInt(String.valueOf(rating));
                                ratingSum += individualRating;
                                //increment ratings by 1 through each loop, to count the number of reviews posted
                                numberOfRatings++;
                            }
                            if(numberOfRatings != 0) {
                                ratingsAverage = ratingSum/numberOfRatings;
                                //convert to an integer
//                                ratingAverageTotal = Math.round(ratingsAverage);
//                                convert to a string and set text to new value
                                totalBusinessRating.setText(String.format("%2.1f", ratingsAverage));

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                //convert the current business rating to the new calculated rating
                                HashMap<String, Object> businessMap = new HashMap<>();
                                businessMap.put("rating", totalBusinessRating.getText().toString());
                                businessDetailsRef.updateChildren(businessMap);

                            }else{
                                totalBusinessRating.setText("Not Yet Rated");
                            }




//                            int ratingSum = 0;
//                            float ratingsTotal = 0;
//                            for(DataSnapshot child : snapshot.child("uniqueBusinessReviewID").child("rating").getChildren()){
//                                ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
//                                ratingsTotal++;
//                            }
//
//                            float ratingAverage = ratingSum/ratingsTotal;
//                            totalBusinessRating.setText(String.valueOf(ratingAverage));
//
//                            HashMap<String, Object> businessMap = new HashMap<>();
//                            businessMap.put("rating", totalBusinessRating);
//                            businessRatingRef.updateChildren(businessMap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });






//                    displayCustomerReviews();
//                    businessRating.setRating(Integer.valueOf(businessDetailsRef.child("rating").getValue(rating);
//                            getRating.toString()));
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
                businessReviewIntent.putExtra("bID", Post_Key);
                businessReviewIntent.putExtra("bName", Post_Name);
                startActivity(businessReviewIntent);
            }
        });
    }

//    private void openReviewDialog()
//    {
//
//
//
//    }

//    private void displayCustomerReviews()
//    {
//        businessRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
//            {
//                businessDetailsRef.child("rating").setValue(rating);
//            }
//        });
//    }
}