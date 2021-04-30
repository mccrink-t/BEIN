package com.example.belfastinanutshell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewBusiness extends AppCompatActivity {

    private String categoryName, bName, description, openingHours, location, contactInfo, website;
    private Button addNewBusinessBtn;
    private EditText inputBusinessName, inputBusinessDescription, inputBusinessOpeningHours,
            inputBusinessLocation, inputBusinessContactInfo, inputBusinessWebsite;
    private ImageView inputBusinessPhoto;
    private static final int imageChoice =1;
    private Uri imageURI;
    private String saveCurrentDate, saveCurrentTime;
    private String businessRandomKey;
    private StorageReference businessImagesRef;
    private String downloadImageUrl;
    private DatabaseReference businessRef;
    private ProgressDialog loadingBar;
    private TextView closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_business);

        categoryName = getIntent().getExtras().get("category").toString();
        businessImagesRef = FirebaseStorage.getInstance().getReference().child("Business Images");
        businessRef = FirebaseDatabase.getInstance().getReference().child("Businesses");

        addNewBusinessBtn = (Button) findViewById(R.id.add_new_businessbtn);
        inputBusinessPhoto = (ImageView) findViewById(R.id.select_business_image);
        inputBusinessName = (EditText) findViewById(R.id.business_name);
        inputBusinessDescription = (EditText) findViewById(R.id.business_description);
        inputBusinessOpeningHours = (EditText) findViewById(R.id.business_opening_hours);
        inputBusinessLocation = (EditText) findViewById(R.id.business_location);
        inputBusinessContactInfo = (EditText) findViewById(R.id.business_contact_info);
        inputBusinessWebsite = (EditText) findViewById(R.id.business_website);
        loadingBar = new ProgressDialog(this);
        closeBtn = (TextView) findViewById(R.id.close_add_new_business_btn);

        inputBusinessPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addNewBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validateBusinessData();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, imageChoice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == imageChoice && resultCode == RESULT_OK && data != null)
        {
            imageURI = data.getData();
            inputBusinessPhoto.setImageURI(imageURI);
        }
    }

    private void validateBusinessData()
    {
        bName = inputBusinessName.getText().toString();
        description = inputBusinessDescription.getText().toString();
        openingHours = inputBusinessOpeningHours.getText().toString();
        location = inputBusinessLocation.getText().toString();
        contactInfo = inputBusinessContactInfo.getText().toString();
        website = inputBusinessWebsite.getText().toString();

        if(imageURI == null)
        {
            Toast.makeText(this, "Business Image required...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(bName))
        {
            Toast.makeText(this, "Please add Business Name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Please add Business Description...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(openingHours))
        {
            Toast.makeText(this, "Please add Business Opening Hours...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(location))
        {
            Toast.makeText(this, "Please add Business Location...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(contactInfo))
        {
            Toast.makeText(this, "Please add Business Contact Information...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(website))
        {
            Toast.makeText(this, "Please add Business Website...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeBusinessInformation();
        }
    }

    private void storeBusinessInformation()
    {
        loadingBar.setTitle("Add New Business");
        loadingBar.setMessage("Please wait whilst we add the new Business");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //creating unique random key by combing current date and current time for each business
        businessRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = businessImagesRef.child(imageURI.getLastPathSegment() + businessRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminAddNewBusiness.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddNewBusiness.this, "Business Information Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewBusiness.this, "Business Image URL Successfully add to the Database!", Toast.LENGTH_SHORT).show();

                            saveBusinessInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveBusinessInfoToDatabase()
    {
        int number = 0;
        String rating = String.valueOf(number);

        HashMap<String, Object> businessMap = new HashMap<>();
        businessMap.put("bID", businessRandomKey);
        businessMap.put("date", saveCurrentDate);
        businessMap.put("time", saveCurrentTime);
        businessMap.put("category", categoryName);
        businessMap.put("image", downloadImageUrl);
        businessMap.put("bName", bName);
        businessMap.put("description", description);
        businessMap.put("openingHrs", openingHours);
        businessMap.put("contactInfo", contactInfo);
        businessMap.put("location", location);
        businessMap.put("website", website);
        businessMap.put("rating", rating);

        businessRef.child(businessRandomKey).updateChildren(businessMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewBusiness.this, AdminCategory.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewBusiness.this, "Business is added Successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewBusiness.this, "Error " +message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}