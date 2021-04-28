package com.example.belfastinanutshell.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Model.Businesses;
import com.example.belfastinanutshell.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    private ArrayList<Businesses> mBusinesses = new ArrayList<>();
//        private Context context;
    private OnCallBack onCallBack;

    public AdapterClass(ArrayList<Businesses> businessesList) {
//        this.context = context;
        this.mBusinesses = businessesList;
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.business_items_layout, parent, false);
        return new AdapterClass.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Businesses model = getItem(position);
        holder.name.setText(mBusinesses.get(position).getbName());
        holder.description.setText(mBusinesses.get(position).getDescription());
        holder.location.setText(mBusinesses.get(position).getLocation());

        Picasso.get().load(mBusinesses.get(position).getImage()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCallBack != null) {
                    onCallBack.onDetail(model);
                }
            }
        });
    }


//        Businesses model = getItem(position);
//        if(model != null) {
//            holder.location.setText(model.getLocation());
//            holder.name.setText(model.getbName());
//            holder.description.setText(model.getDescription());
//            holder.description.setText(model.getDescription());
//            holder.businessSingleCard.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(onCallBack != null){
//                        onCallBack.onDetail(model);
//                    }
//                }
//            });
//        }


    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }

    private Businesses getItem(int position) {
        return mBusinesses.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, location;
        ImageView image;
        private RelativeLayout businessSingleCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.business_name);
            description = itemView.findViewById(R.id.business_description);
            location = itemView.findViewById(R.id.business_location);
            image = itemView.findViewById(R.id.business_image);
            businessSingleCard = itemView.findViewById(R.id.single_business_layout);

        }
    }

    public interface OnCallBack {
        void onDetail(Businesses businesses);

    }
}
