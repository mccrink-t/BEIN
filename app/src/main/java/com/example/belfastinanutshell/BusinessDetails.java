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

public class BusinessDetails extends AppCompatActivity {

    private TextView bNameDetails, descriptionDetails, locationDetails, openingHrsDetails, contactInfoDetails, websiteDetails;
    private ImageView imageDetails;
    private String businessID = "";
    private Button addReviewBtn;
//    private RatingBar businessRating;
    private DatabaseReference businessDetailsRef;
    private String bNameReviewTitle;
    private String Post_Key;

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
//        businessRating = (RatingBar) findViewById(R.id.businessRating);



        //call information from business database firebase
        businessDetailsRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(businessID);
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

                    bNameDetails.setText(businesses.getbName());
                    descriptionDetails.setText(businesses.getDescription());
                    locationDetails.setText(businesses.getLocation());
                    openingHrsDetails.setText(businesses.getOpeningHrs());
                    contactInfoDetails.setText(businesses.getContactInfo());
                    websiteDetails.setText(businesses.getWebsite());
                    Picasso.get().load(businesses.getImage()).into(imageDetails);

                    bNameReviewTitle = businesses.getbName();

//                    displayCustomerReviews();
//                    businessRating.setRating(Integer.valueOf(businessDetailsRef.child("rating").getValue(rating);
//                            getRating.toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent businessReviewIntent = new Intent(BusinessDetails.this, BusinessReviewsActivity.class);
                businessReviewIntent.putExtra("bID", Post_Key);
                startActivity(businessReviewIntent);

//                DatabaseReference bReviewRef = FirebaseDatabase.getInstance()
//                        .getReference().child("Businesses").child("bID");
//                EditText businessReview = new EditText(v.getContext());
//
//                AlertDialog businessReviewDialog = new AlertDialog.Builder(
//                        BusinessDetails.this).create();
//
//                // Setting Dialog Title
//                businessReviewDialog.setTitle(bNameReviewTitle);
//
//                // Setting Dialog Message
//                businessReviewDialog.setMessage("\nPlease write your Review");
//
//                // Setting Icon to Dialog
//                businessReviewDialog.setIcon(R.drawable.ic_profile_icon);
//
//                businessReviewDialog.setView(businessReview);
//
//                // Setting OK Button
//                businessReviewDialog.setButton(Dialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener(){
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
            }
        });
    }

    private void openReviewDialog()
    {



    }

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