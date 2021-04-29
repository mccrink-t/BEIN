package com.example.belfastinanutshell.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.R;

public class BusinessReviewsViewHolder extends RecyclerView.ViewHolder {

    public TextView usersReviewName, usersReviewText, usersReviewDate, usersReviewTime, usersBusinessRating;

    public BusinessReviewsViewHolder(View itemView) {
        super(itemView);

        usersReviewName = (TextView) itemView.findViewById(R.id.business_Review_User_Name);
        usersReviewText = (TextView) itemView.findViewById(R.id.business_review_Text);
        usersReviewDate = (TextView) itemView.findViewById(R.id.business_review_date);
        usersReviewTime = (TextView) itemView.findViewById(R.id.business_review_time);
        usersBusinessRating = (TextView) itemView.findViewById(R.id.SingleBusinessRatingNumber);



    }

}
