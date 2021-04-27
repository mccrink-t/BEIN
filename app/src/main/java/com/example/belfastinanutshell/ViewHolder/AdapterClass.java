package com.example.belfastinanutshell.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Model.Businesses;
import com.example.belfastinanutshell.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder>{

    ArrayList<Businesses> list;
    public AdapterClass(ArrayList<Businesses> list)
    {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.business_items_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.name.setText(list.get(position).getbName());
        holder.description.setText(list.get(position).getDescription());
        holder.location.setText(list.get(position).getLocation());
//        Picasso.get().load(list.get(position).getImage());
        Picasso.get().load(list.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, location;
        ImageView image;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            //could be something to do with implementing R class
            name = itemView.findViewById(R.id.business_name);
            description = itemView.findViewById(R.id.business_description);
            location = itemView.findViewById(R.id.business_location);
            image = itemView.findViewById(R.id.business_image);
        }
    }
}
