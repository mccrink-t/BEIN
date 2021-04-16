package com.example.belfastinanutshell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText fNameEditText, institutionEditText, emailEditText, degreeEditText;
    private TextView changeProfilePicBtn,  closeBtn, updateProfileBtn, yearofStudyTextView;
    private CircleImageView profilePicView;
    private Spinner dropdownYOS;

    //variables for image
    private Uri imageUri;
    private String myUrl ="";
    private StorageReference storageProfilePicRef;
    private String checker ="";
    private StorageTask uploadTask;

    //testing
    private List<String> nomeConsulta = new ArrayList<String>();
    private ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fNameEditText = (EditText) findViewById(R.id.settings_fName_TV);
        institutionEditText = (EditText) findViewById(R.id.settings_university_TV);
        emailEditText = (EditText) findViewById(R.id.settings_email_TV);
        degreeEditText = (EditText) findViewById(R.id.settings_degree_TV);
        changeProfilePicBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeBtn = (TextView) findViewById(R.id.close_settings_btn);
        updateProfileBtn = (TextView) findViewById(R.id.update_settings_btn);
        yearofStudyTextView = (TextView) findViewById(R.id.settings_year_of_study_TV);
        profilePicView = (CircleImageView) findViewById(R.id.settings_profile_image);

        //dropdown for year of study
        dropdownYOS = findViewById(R.id.settings_year_of_study_Spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.yearOfStudy, android.R.layout.simple_spinner_item);
        // layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dropdownYOS.setAdapter(adapter);

        storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        //Call to display all of the users current data on their profile
        userInfoDisplay(profilePicView, fNameEditText, institutionEditText, emailEditText, dropdownYOS, degreeEditText, yearofStudyTextView);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checker.equals("clicked"))
                {
                    //call to update all of their new data - including new profile pic
                    userInfoUpdated();
                }
                else
                {
                    //call to update all of their new data - NOT including a profile pic
                    //checker used to check if users wants to update the profile pics as well as the data
                    updateOnlyUserData();
                }
            }
        });

        changeProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //if user clicks on this button assign clicked value
                checker ="clicked";

                // crop image down to size using code from https://github.com/ArthurHub/Android-Image-Cropper
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(Settings.this);
            }
        });
    }

    //updates a user profile when they are not changing the profile image
    private void updateOnlyUserData()
    {
        //store data into Users table on firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("fullName", fNameEditText.getText().toString());
        userMap. put("institution", institutionEditText.getText().toString());
        userMap. put("email", emailEditText.getText().toString());
        userMap. put("yearOfStudy", dropdownYOS.getSelectedItem().toString());
        userMap. put("degreeOfStudy", degreeEditText.getText().toString());
        userMap. put("yearOfStudy", yearofStudyTextView.getText().toString());

        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(Settings.this, Home.class));
        Toast.makeText(Settings.this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == RESULT_OK  &&  data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //Store result into imageUri
            imageUri = result.getUri();
            //display it on the imageview to show user what picture they chose
            profilePicView.setImageURI(imageUri);
        }
        //if an image is not selected
        else
        {
            Toast.makeText(this, "Error Occurred, Please Try Again.", Toast.LENGTH_SHORT).show();
            //Refresh page
            startActivity(new Intent(Settings.this, Settings.class));
            finish();
        }
    }

    //Store new user info and data into database
    private void userInfoUpdated()
    {
        if (TextUtils.isEmpty(fNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your Name!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(institutionEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your School Institution", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(degreeEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter what you are currently studying...", Toast.LENGTH_SHORT).show();
        }
        //checks checker variable (if an image has been added)
        else if(checker.equals("clicked"))
        {
            //call to upload image to database
            uploadImage();
        }
    }

    //method to upload image to database
    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Edit Profile");
        progressDialog.setMessage("Updating Profile...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null)
        {
            //replace old image with new image
            final StorageReference uploadFileRef = storageProfilePicRef.child(Prevalent.CurrentOnlineUser.getPhone()
            + ".jpg");

            //save file to the storage
            uploadTask = uploadFileRef.putFile(imageUri);

            //get result from upload task
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return uploadFileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        //getting result of url and storing it in variable
                        Uri downloadUrl = task.getResult();
                        //convert Uri to url
                        myUrl = downloadUrl.toString();

                        //store data into Users table on firebase
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("image", myUrl);
                        userMap. put("fullName", fNameEditText.getText().toString());
                        userMap. put("institution", institutionEditText.getText().toString());
                        userMap. put("email", emailEditText.getText().toString());
                        userMap. put("yearOfStudy", dropdownYOS.getSelectedItem().toString());
                        userMap. put("degreeOfStudy", degreeEditText.getText().toString());
                        userMap. put("yearOfStudy", yearofStudyTextView.getText().toString());

                        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(Settings.this, Home.class));
                        Toast.makeText(Settings.this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Settings.this, "Error occurred when Updating Profile", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Image is not Selected.", Toast.LENGTH_SHORT).show();
        }
    }

    //Method to display all of the users current data on their profile
    private void userInfoDisplay(CircleImageView profilePicView, EditText fNameEditText, EditText institutionEditText, EditText emailEditText, Spinner dropdownYOS, EditText degreeEditText, TextView yearofStudyTextView)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {

                if (snapshot.exists())
                {
                    //if a child image exists
                    if (snapshot.child("image").exists())
                    {
                        //if it exists - fetch and display on the profile
                        String image = snapshot.child("image").getValue().toString();
                        String institution = snapshot.child("institution").getValue().toString();
                        String fullName = snapshot.child("fullName").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
//                        String yearofstudy = snapshot.child("yearofstudy").getValue(String.class);
                        String degreeOfStudy = snapshot.child("degreeOfStudy").getValue().toString();
                        String yearOfStudy = snapshot.child("yearOfStudy").getValue().toString();

                        Picasso.get().load(image).into(profilePicView);
                        institutionEditText.setText(institution);
                        fNameEditText.setText(fullName);
                        emailEditText.setText(email);
                        degreeEditText.setText(degreeOfStudy);
                        yearofStudyTextView.setText(yearOfStudy);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //dropdown for Year of Study
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String choiceYOS = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(),choiceYOS, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}