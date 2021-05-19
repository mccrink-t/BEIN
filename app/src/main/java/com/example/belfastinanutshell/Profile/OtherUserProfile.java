package com.example.belfastinanutshell.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserProfile extends AppCompatActivity {

    private TextView otherProfileName, otherProfileEmail, otherProfileInstitution, otherProfileDegree, otherProfileYOS;
    private CircleImageView otherProfilePicView;
    private TextView closeOtherProfileBtn;

    private String otherUserProfileID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        otherProfileName = (TextView) findViewById(R.id.other_profile_fName_TV);
        otherProfileEmail = (TextView) findViewById(R.id.other_profile_email_TextView);
        otherProfileInstitution = (TextView) findViewById(R.id.other_profile_institution_TextView);
        otherProfileDegree = (TextView) findViewById(R.id.other_profile_degree_TextView);
        otherProfileYOS = (TextView) findViewById(R.id.other_profile_year_of_study_TextView);
        otherProfilePicView = (CircleImageView) findViewById(R.id.other_profile_profile_image);
        closeOtherProfileBtn = (TextView) findViewById(R.id.close_other_profile_btn);

        otherUserProfileID = getIntent().getExtras().get("userID").toString();

        //Call to display all of the users current data on their profile
        userInfoDisplay(otherProfilePicView, otherProfileName, otherProfileEmail, otherProfileInstitution, otherProfileDegree, otherProfileYOS);

        closeOtherProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //Method to retrieve user data and overwrite textviews to show user data on activity
    private void userInfoDisplay(CircleImageView otherProfilePicView, TextView otherProfileName, TextView otherProfileEmail, TextView otherProfileInstitution, TextView otherProfileDegree, TextView otherProfileYOS)
    {
        DatabaseReference OtherUsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserProfileID);

        OtherUsersRef.addValueEventListener(new ValueEventListener() {
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
                        String email = snapshot.child("email").getValue().toString();
                        String institution = snapshot.child("institution").getValue().toString();
                        String degreeOfStudy = snapshot.child("degreeOfStudy").getValue().toString();
                        String yearOfStudy = snapshot.child("yearOfStudy").getValue().toString();

                        Picasso.get().load(image).into(otherProfilePicView);
                        otherProfileName.setText(fullName);
                        otherProfileEmail.setText(email);
                        otherProfileInstitution.setText(institution);
                        otherProfileDegree.setText(degreeOfStudy);
                        otherProfileYOS.setText(yearOfStudy);
                    }
                    else{
                        String fullName = snapshot.child("fullName").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();

                        otherProfileName.setText(fullName);
                        otherProfileEmail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}