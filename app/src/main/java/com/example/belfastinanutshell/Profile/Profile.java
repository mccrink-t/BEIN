package com.example.belfastinanutshell.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private TextView profileName, profilePhone, profileEmail, profileInstitution, profileDegree, profileYOS;
    private CircleImageView profilePicView;
    private Button updateProfileBtn;
    private TextView homeProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = (TextView) findViewById(R.id.profile_fName_TV);
        profilePhone = (TextView) findViewById(R.id.profile_phone_TextView);
        profileEmail = (TextView) findViewById(R.id.profile_email_TextView);
        profileInstitution = (TextView) findViewById(R.id.profile_institution_TextView);
        profileDegree = (TextView) findViewById(R.id.profile_degree_TextView);
        profileYOS = (TextView) findViewById(R.id.profile_year_of_study_TextView);
        profilePicView = (CircleImageView) findViewById(R.id.profile_profile_image);
        updateProfileBtn = (Button) findViewById(R.id.redirect_updateProfile_Btn);
        homeProfileBtn = (TextView) findViewById(R.id.home_profile_btn);

        //Call to display all of the users current data on their profile
        userInfoDisplay(profilePicView, profileName, profilePhone, profileEmail, profileInstitution, profileDegree, profileYOS);

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Settings.class);
                startActivity(intent);
            }
        });

        //        Button to close the activity
        homeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

    }

    //Method to retrieve user data and overwrite textviews to show user data on activity
    private void userInfoDisplay(CircleImageView profilePicView, TextView profileName, TextView profilePhone, TextView profileEmail, TextView profileInstitution, TextView profileDegree, TextView profileYOS)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot)
            {

                if (snapshot.exists())
                {
                    //if a child image exists
                    if (snapshot.child("image").exists())
                    {
                        //if it exists - fetch and display on the profile
                        String image = snapshot.child("image").getValue().toString();
                        String fullName = snapshot.child("fullName").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String institution = snapshot.child("institution").getValue().toString();
                        String degreeOfStudy = snapshot.child("degreeOfStudy").getValue().toString();
                        String yearOfStudy = snapshot.child("yearOfStudy").getValue().toString();

                        Picasso.get().load(image).into(profilePicView);
                        profileName.setText(fullName);
                        profilePhone.setText(phone);
                        profileEmail.setText(email);
                        profileInstitution.setText(institution);
                        profileDegree.setText(degreeOfStudy);
                        profileYOS.setText(yearOfStudy);
                    }
                    else{
                        String fullName = snapshot.child("fullName").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();

                        profileName.setText(fullName);
                        profilePhone.setText(phone);
                        profileEmail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}