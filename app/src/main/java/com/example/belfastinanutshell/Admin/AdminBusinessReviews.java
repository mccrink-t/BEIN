package com.example.belfastinanutshell.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Model.BusinessReviewsModel;
import com.example.belfastinanutshell.R;
import com.example.belfastinanutshell.ViewHolder.BusinessReviewsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminBusinessReviews extends AppCompatActivity {

    private RecyclerView businessReviewList;
    private TextView closeBtn, businessReviewName;
    private RelativeLayout leaveReviewRating, leaveReviewText;

    private DatabaseReference businessRef;

    private String Business_Key;
    private String Business_Name;

    float ratingNumber = 0;
    int numberOfStars;
    TextView delete;
    String currentUsersID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_reviews);

        Business_Key = getIntent().getExtras().get("bID").toString();
        Business_Name = getIntent().getExtras().get("bName").toString();

        businessRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(Business_Key).child("Reviews");

        businessReviewList = (RecyclerView) findViewById(R.id.businessReviewList);
        businessReviewList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        businessReviewList.setLayoutManager(linearLayoutManager);

        closeBtn = (TextView) findViewById(R.id.close_single_business_reviews_btn);
        businessReviewName = (TextView) findViewById(R.id.review_Business_Name);
        leaveReviewRating = (RelativeLayout) findViewById(R.id.RL_middle);
        leaveReviewText = (RelativeLayout) findViewById(R.id.RL_bottom);

        businessReviewName.setText(Business_Name);

        leaveReviewRating.setVisibility(View.INVISIBLE);
        leaveReviewText.setVisibility(View.INVISIBLE);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(AdminBusinessReviews.this, Admin_Delete_Business_Review.class);
                                intent.putExtra("reviewID", model.getReviewID());
                                intent.putExtra("bID", Business_Key);
                                intent.putExtra("bName", Business_Name);
                                startActivity(intent);
                            }
                        });

                        if (model.getUserID() != null) {
                            currentUsersID = model.getUserID();
                        }

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
}
