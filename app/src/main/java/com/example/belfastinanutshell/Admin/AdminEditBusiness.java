package com.example.belfastinanutshell.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.R;
import com.example.belfastinanutshell.SearchBusinessActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditBusiness extends AppCompatActivity {

    private EditText businessName, businessDescription, businessOpeningHrs, businessLocation,
            businessContactInfo, businessWebsite;
    private Button updateBusinessBtn, deleteBusinessBtn;
    private ImageView businessImageView;
    private TextView closeBtn;

    //variable to grab business ID
    private String businessID = "";
    private DatabaseReference businessDetailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_business);

        businessName = (EditText) findViewById(R.id.edit_business_name);
        businessDescription = (EditText) findViewById(R.id.edit_business_description);
        businessOpeningHrs = (EditText) findViewById(R.id.edit_business_opening_hours);
        businessLocation = (EditText) findViewById(R.id.edit_business_location);
        businessContactInfo = (EditText) findViewById(R.id.edit_business_contact_info);
        businessWebsite = (EditText) findViewById(R.id.edit_business_website);
        updateBusinessBtn = (Button) findViewById(R.id.update_business_btn);
        deleteBusinessBtn = (Button) findViewById(R.id.delete_business_btn);
        closeBtn = (TextView) findViewById(R.id.close_edit_business_btn);
        businessImageView = (ImageView) findViewById(R.id.business_image);

        //grab business ID from previous activity
        businessID = getIntent().getStringExtra("bID");
        businessDetailsRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(businessID);

        //run method to retrieve current business data and store in the Edit Text Fields
        getBusinessDetails();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBusiness();
            }
        });

        deleteBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteBusiness();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminEditBusiness.this);
                builder.setMessage("Are you sure you want to delete this business?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    private void deleteBusiness()
    {
        businessDetailsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(AdminEditBusiness.this, SearchBusinessActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminEditBusiness.this, "Business Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBusiness()
    {
        String updateBusinessName = businessName.getText().toString();
        String updateBusinessDescription = businessDescription.getText().toString();
        String updateBusinessOpeningHrs  = businessOpeningHrs.getText().toString();
        String updateBusinessLocation = businessLocation.getText().toString();
        String updateBusinessContactInfo = businessContactInfo.getText().toString();
        String updateBusinessWebsite = businessWebsite.getText().toString();
        
        if(updateBusinessName.equals(""))
        {
            Toast.makeText(this, "Please enter Business Name.", Toast.LENGTH_SHORT).show();
        }
        else if(updateBusinessDescription.equals(""))
        {
            Toast.makeText(this, "Please enter Business Description.", Toast.LENGTH_SHORT).show();
        }
        else if(updateBusinessOpeningHrs.equals(""))
        {
            Toast.makeText(this, "Please enter Business Opening Hours.", Toast.LENGTH_SHORT).show();
        }
        else if(updateBusinessLocation.equals(""))
        {
            Toast.makeText(this, "Please enter Business Location.", Toast.LENGTH_SHORT).show();
        }
        else if(updateBusinessContactInfo.equals(""))
        {
            Toast.makeText(this, "Please enter Business Contact Info.", Toast.LENGTH_SHORT).show();
        }else if(updateBusinessWebsite.equals(""))
        {
            Toast.makeText(this, "Please enter Business Website.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> businessMap = new HashMap<>();
            businessMap.put("bID", businessID);
            businessMap.put("bName", updateBusinessName);
            businessMap.put("description", updateBusinessDescription);
            businessMap.put("openingHrs", updateBusinessOpeningHrs);
            businessMap.put("contactInfo", updateBusinessContactInfo);
            businessMap.put("location", updateBusinessLocation);
            businessMap.put("website", updateBusinessWebsite);

            businessDetailsRef.updateChildren(businessMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(AdminEditBusiness.this, SearchBusinessActivity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(AdminEditBusiness.this, "Business Successfully Updated!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String message = task.getException().toString();
                        Toast.makeText(AdminEditBusiness.this, "Error " +message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void getBusinessDetails()
    {
        businessDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    //potentially add one for category so admin can view business category

                    String grabBusinessName = snapshot.child("bName").getValue().toString();
                    String grabBusinessDescription = snapshot.child("description").getValue().toString();
                    String grabBusinessOpeningHrs = snapshot.child("openingHrs").getValue().toString();
                    String grabBusinessLocation = snapshot.child("location").getValue().toString();
                    String grabBusinessContactInfo = snapshot.child("contactInfo").getValue().toString();
                    String grabBusinessWebsite = snapshot.child("website").getValue().toString();
                    String grabBusinessImage = snapshot.child("image").getValue().toString();

                    businessName.setText(grabBusinessName);
                    businessDescription.setText(grabBusinessDescription);
                    businessOpeningHrs.setText(grabBusinessOpeningHrs);
                    businessLocation.setText(grabBusinessLocation);
                    businessContactInfo.setText(grabBusinessContactInfo);
                    businessWebsite.setText(grabBusinessWebsite);
                    Picasso.get().load(grabBusinessImage).into(businessImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}