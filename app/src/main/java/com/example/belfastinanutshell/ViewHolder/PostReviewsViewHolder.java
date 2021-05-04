package com.example.belfastinanutshell.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.R;

public class PostReviewsViewHolder extends RecyclerView.ViewHolder
{
    public TextView usersPostReviewName, usersPostReviewText, usersPostReviewDate, usersPostReviewTime, usersPostRating;

    public PostReviewsViewHolder(View itemView) {
        super(itemView);

        usersPostReviewName = (TextView) itemView.findViewById(R.id.post_Review_User_Name);
        usersPostReviewText = (TextView) itemView.findViewById(R.id.post_review_Text);
        usersPostReviewDate = (TextView) itemView.findViewById(R.id.post_review_date);
        usersPostReviewTime = (TextView) itemView.findViewById(R.id.post_review_time);
        usersPostRating = (TextView) itemView.findViewById(R.id.SinglePostRatingNumber);



    }
}
