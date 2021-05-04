package com.example.belfastinanutshell.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Interface.ItemClickListener;
import com.example.belfastinanutshell.R;

public class BusinessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtBusinessName, txtBusinessDescription, txtBusinessLocation, txtBusinessRating;
    public ImageView imageView;
    public ItemClickListener listener;

    public BusinessViewHolder(View itemView) {
        super(itemView);

        //potentially something to do with business_image and select business image
        imageView = (ImageView) itemView.findViewById(R.id.business_image);
        txtBusinessName = (TextView) itemView.findViewById(R.id.business_name);
        txtBusinessDescription = (TextView) itemView.findViewById(R.id.business_description);
        txtBusinessLocation = (TextView) itemView.findViewById(R.id.business_location);
        txtBusinessRating = (TextView) itemView.findViewById(R.id.business_userRating);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}