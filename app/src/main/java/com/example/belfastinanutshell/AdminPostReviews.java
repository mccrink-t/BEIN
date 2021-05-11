package com.example.belfastinanutshell;

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

import com.example.belfastinanutshell.Model.PostReviewsModel;
import com.example.belfastinanutshell.ViewHolder.PostReviewsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPostReviews extends AppCompatActivity {

    private RecyclerView postReviewList;
    private TextView closeBtn, postReviewTitle;
    private TextView singleUserPostRatingTextView;
    private RelativeLayout leaveReviewRating, leaveReviewText;
    private DatabaseReference postRef;
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

        Post_Key = getIntent().getExtras().get("postID").toString();
        Post_Title = getIntent().getExtras().get("postTitle").toString();

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("UserPosts").child(Post_Key).child("Reviews");

        postReviewList = (RecyclerView) findViewById(R.id.postReviewList);
        postReviewList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postReviewList.setLayoutManager(linearLayoutManager);

        closeBtn = (TextView) findViewById(R.id.close_single_post_reviews_btn);
        postReviewTitle = (TextView) findViewById(R.id.review_Post_Title);
        singleUserPostRatingTextView = (TextView) findViewById(R.id.singleUserPostRatingText);
        leaveReviewRating = (RelativeLayout) findViewById(R.id.RL_middle);
        leaveReviewText = (RelativeLayout) findViewById(R.id.RL_bottom);


        postReviewTitle.setText(Post_Title);

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

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(AdminPostReviews.this, Admin_Delete_Post_Review.class);
                                intent.putExtra("reviewID", model.getReviewID());
                                intent.putExtra("postID", Post_Key);
                                intent.putExtra("postTitle", Post_Title);
                                startActivity(intent);
                            }
                        });

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


}