package com.example.belfastinanutshell.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.MainActivity;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.R;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    private EditText fNameSettings, institutionSettings, emailSettings, degreeSettings, yosSettings;
    private TextView changeProfilePicBtn, closeBtn;
    private Button updateProfileBtn;
    private CircleImageView profilePicView;

    //variables for image
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePicRef;
    private String checker = "";
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fNameSettings = (EditText) findViewById(R.id.settings_fName_TV);
        institutionSettings = (EditText) findViewById(R.id.settings_university_TV);
        emailSettings = (EditText) findViewById(R.id.settings_email_TV);
        degreeSettings = (EditText) findViewById(R.id.settings_degree_TV);
        yosSettings = (EditText) findViewById(R.id.settings_yos_TV);

        profilePicView = (CircleImageView) findViewById(R.id.settings_profile_image);
        changeProfilePicBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeBtn = (TextView) findViewById(R.id.close_settings_btn);
        updateProfileBtn = (Button) findViewById(R.id.update_settings_btn);

        storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");


        //Call to display all of the users current data on their profile
        userInfoDisplay(profilePicView, fNameSettings, institutionSettings, emailSettings, yosSettings);

//        Button to close the activity
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//      Button to call methods to update users info
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {
//                    call to update all of their new data - including new profile pic
                    userInfoUpdated();
                } else {
                    //call to update all of their new data - NOT including a profile pic
                    //checker used to check if users wants to update the profile pics as well as the data
                    updateOnlyUserData();
                }
            }
        });


        changeProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(Settings.this);
            }
        });
    }

    private void userInfoDisplay(CircleImageView profilePicView, EditText fNameSettings, EditText institutionSettings, EditText emailSettings, EditText yosSettings) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    //if a child image exists
                    if (snapshot.child("image").exists()) {
                        //if it exists - fetch and display on the profile
                        String image = snapshot.child("image").getValue().toString();
                        String institution = snapshot.child("institution").getValue().toString();
                        String fullName = snapshot.child("fullName").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String degreeOfStudy = snapshot.child("degreeOfStudy").getValue().toString();
                        String yearOfStudy = snapshot.child("yearOfStudy").getValue().toString();

                        Picasso.get().load(image).into(profilePicView);
                        institutionSettings.setText(institution);
                        fNameSettings.setText(fullName);
                        emailSettings.setText(email);
                        degreeSettings.setText(degreeOfStudy);
                        //may need to fix this
                        yosSettings.setText(yearOfStudy);
                    }
                    else
                    {
                        String institution = snapshot.child("institution").getValue().toString();
                        String fullName = snapshot.child("fullName").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String degreeOfStudy = snapshot.child("degreeOfStudy").getValue().toString();
                        String yearOfStudy = snapshot.child("yearOfStudy").getValue().toString();

                        institutionSettings.setText(institution);
                        fNameSettings.setText(fullName);
                        emailSettings.setText(email);
                        degreeSettings.setText(degreeOfStudy);
                        //may need to fix this
                        yosSettings.setText(yearOfStudy);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateOnlyUserData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", fNameSettings.getText().toString());
        userMap.put("email", emailSettings.getText().toString());
        userMap.put("institution", institutionSettings.getText().toString());
        userMap.put("degreeOfStudy", degreeSettings.getText().toString());
        userMap.put("yearOfStudy", yosSettings.getText().toString());

        ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(Settings.this, MainActivity.class));
        Toast.makeText(Settings.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profilePicView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Settings.this, Settings.class));
            finish();
        }
    }


    private void userInfoUpdated() {
        if (TextUtils.isEmpty(fNameSettings.getText().toString())) {
            Toast.makeText(this, "Please Enter Your Name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(emailSettings.getText().toString())) {
            Toast.makeText(this, "Please Enter Your Email Address.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(institutionSettings.getText().toString())) {
            Toast.makeText(this, "Please Enter Your Institution Name.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(degreeSettings.getText().toString())) {
            Toast.makeText(this, "Please Enter The Degree You Study.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(yosSettings.getText().toString())) {
            Toast.makeText(this, "Please Enter Your Year of Study.", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }


    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicRef
                    .child(Prevalent.CurrentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("fullName", fNameSettings.getText().toString());
                                userMap.put("email", emailSettings.getText().toString());
                                userMap.put("institution", institutionSettings.getText().toString());
                                userMap.put("degreeOfStudy", degreeSettings.getText().toString());
                                userMap.put("yearOfStudy", yosSettings.getText().toString());
                                userMap.put("image", myUrl);
                                ref.child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(Settings.this, MainActivity.class));
                                Toast.makeText(Settings.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Settings.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }
}
