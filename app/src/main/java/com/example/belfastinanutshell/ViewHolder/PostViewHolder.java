package com.example.belfastinanutshell.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Interface.ItemClickListener;
import com.example.belfastinanutshell.R;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtPostTitle, txtPostDescription, txtPostUsersName, txtPostDate;
    public ImageView imageView;
    public ItemClickListener listener;

    public PostViewHolder(View itemView)
    {
        super(itemView);

        //potentially something to do with business_image and select business image
        imageView = (ImageView) itemView.findViewById(R.id.post_image);
        txtPostTitle = (TextView) itemView.findViewById(R.id.post_title);
        txtPostDescription = (TextView) itemView.findViewById(R.id.post_description);
        txtPostUsersName = (TextView) itemView.findViewById(R.id.post_user_name);
        txtPostDate = (TextView) itemView.findViewById(R.id.post_date);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}