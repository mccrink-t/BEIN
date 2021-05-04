package com.example.belfastinanutshell.Businesses;

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
import com.example.belfastinanutshell.Model.BusinessReviewsModel;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.R;
import com.example.belfastinanutshell.ViewHolder.BusinessReviewsViewHolder;
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

public class BusinessReviewsActivity extends AppCompatActivity {

    private ImageView postBusinessReviewBtn;
    private EditText businessReviewInputText;
    private RecyclerView businessReviewList;
    private TextView closeBtn, businessReviewName;
    private RatingBar singleUserBusinessRating;
    private TextView singleUserBusinessRatingTextView;
    private String uniqueBusinessReviewID = null;
    private String userType = "";

//    private Button deleteBusinessReview;

    private DatabaseReference usersRef, businessRef, businessRatingRef;

    private String Business_Key;
    private String Business_Name;

    float ratingNumber = 0;
    int numberOfStars;
    TextView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_reviews);
        uniqueBusinessReviewID = UUID.randomUUID().toString();

        Business_Key = getIntent().getExtras().get("bID").toString();
        Business_Name = getIntent().getExtras().get("bName").toString();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        businessRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(Business_Key).child("Reviews");
        businessRatingRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(Business_Key);

        businessReviewList = (RecyclerView) findViewById(R.id.businessReviewList);
        businessReviewList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        businessReviewList.setLayoutManager(linearLayoutManager);

        businessReviewInputText = (EditText) findViewById(R.id.businessReviewInput);
        postBusinessReviewBtn = (ImageView) findViewById(R.id.postBusinessReviewBtn);
        closeBtn = (TextView) findViewById(R.id.close_single_business_reviews_btn);
        businessReviewName = (TextView) findViewById(R.id.review_Business_Name);
        singleUserBusinessRating = (RatingBar) findViewById(R.id.singleUserBusinessRating);
        singleUserBusinessRatingTextView = (TextView) findViewById(R.id.singleUserBusinessRatingText);

//        deleteBusinessReview = (Button) findViewById(R.id.delete_business_review_btn);

        delete = (TextView) findViewById(R.id.temporaryRatingHolder);

        businessReviewName.setText(Business_Name);

        singleUserBusinessRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingNumber = singleUserBusinessRating.getRating();
                numberOfStars = singleUserBusinessRating.getNumStars();
                singleUserBusinessRatingTextView.setText(String.format("%2.1f",ratingNumber));
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postBusinessReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String fullName = snapshot.child("fullName").getValue().toString();
                            String userID = snapshot.child("phone").getValue().toString();

                            ValidateBusinessReview(fullName, userID);
                            businessReviewInputText.setText("");
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

        FirebaseRecyclerOptions<BusinessReviewsModel> options =
                new FirebaseRecyclerOptions.Builder<BusinessReviewsModel>()
                        .setQuery(businessRef, BusinessReviewsModel.class)
                        .build();

        FirebaseRecyclerAdapter<BusinessReviewsModel, BusinessReviewsViewHolder> adapter =
                new FirebaseRecyclerAdapter<BusinessReviewsModel, BusinessReviewsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BusinessReviewsViewHolder holder, int position, @NonNull BusinessReviewsModel model) {
                        holder.usersReviewName.setText(model.getFullName());
                        holder.usersReviewText.setText(model.getReview());
                        holder.usersReviewDate.setText(model.getDate());
                        holder.usersReviewTime.setText(model.getTime());
                        holder.usersBusinessRating.setText(model.getRating());

                    }

                    @NonNull
                    @Override
                    public BusinessReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_business_reviews_layout, parent, false);
                        BusinessReviewsViewHolder holder = new BusinessReviewsViewHolder(view);
                        return holder;
                    }
                };

        businessReviewList.setAdapter(adapter);
        adapter.startListening();


    }

    //    private void deleteReview()
//    {
//
//        businessRef.child(reviewID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                Intent intent = new Intent(BusinessReviewsActivity.this, AdminEditBusiness.class);
//                intent.putExtra("bID", Business_Key);
//                startActivity(intent);
//                finish();
//
//                Toast.makeText(BusinessReviewsActivity.this, "Review Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void ValidateBusinessReview(String fullName, String userID) {
        String businessReviewText = businessReviewInputText.getText().toString();
        String saveCurrentDate, saveCurrentTime;

        int ratingToInt = 0;
        //convert to int and round value to 0 decimal places
//        int ratingInt = Math.round(Integer.valueOf(singleUserBusinessRatingTextView.getText().toString()));
//        convert back to string

        if (singleUserBusinessRatingTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Review Unsuccessful : Please Leave a Star Rating", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(businessReviewText)) {
            Toast.makeText(this, "Review Unsuccessful : Please Write Your Review to submit", Toast.LENGTH_SHORT).show();
        } else {

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
            //code to generate a random Id for each new Business Review

            HashMap businessReviewsMap = new HashMap();
            //mapping the values inside the variables to go into the fields on the business review table
            businessReviewsMap.put("reviewID", uniqueBusinessReviewID);
            businessReviewsMap.put("userID", userID);
            businessReviewsMap.put("review", businessReviewText);
            businessReviewsMap.put("date", saveCurrentDate);
            businessReviewsMap.put("time", saveCurrentTime);
            businessReviewsMap.put("fullName", fullName);
            businessReviewsMap.put("rating", rating);

            //update the database table businesses, and the selected business, to add the new data into it via the hashmap
            businessRef.child(uniqueBusinessReviewID).updateChildren(businessReviewsMap)
                    .addOnCompleteListener(new OnCompleteListener() {

                        @Override
                        public void onComplete(@NonNull Task task) {
                            //if the review was submitted successfully
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(BusinessReviewsActivity.this, Home.class);
                                startActivity(intent);
                                Toast.makeText(BusinessReviewsActivity.this, "Review was added to " + Business_Name + " successfully!", Toast.LENGTH_SHORT).show();
                            }
                            //else if an error occured
                            else {
                                Toast.makeText(BusinessReviewsActivity.this, "Error Occurred, Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}