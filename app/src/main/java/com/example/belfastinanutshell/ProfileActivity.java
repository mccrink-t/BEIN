package com.example.belfastinanutshell;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;


public class ProfileActivity extends AppCompatActivity {

    private TextView email, fullName;
    private EditText etemail;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private FirebaseUser user;
    private static final String TAG = "ProfileActivity";
    //    private static final String USERS = "users";
//    String email;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_profile);

//            emailTextView = findViewById(R.id.email_textview);
//            fullNameTextView = findViewById(R.id.fullName_textview);
//
//            Intent intent = getIntent();
//            email = intent.getStringExtra("email");

            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
//            userRef = database.getReference(USERS);
            userRef = database.getReference();
            user = mAuth.getCurrentUser();
            userID = user.getUid();

            Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
            findViews();

//            userRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    loop through every result returned by the database -- getChildren returns interval containing all objects in database
//                    for(DataSnapshot ds : dataSnapshot.getChildren()){
//                        if(ds.child("email").getValue().equals(email)){
//                            fullNameTextView.setText(ds.child("fullName").getValue(String.class));
//                            emailTextView.setText(ds.child("email").getValue(String.class));
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toastMessage("error connecting to Database");
            }
        }) ;
        }

        public void toastMessage(String message){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        private void findViews(){
            email = findViewById(R.id.email_textview);
            fullName = findViewById(R.id.fullName_textview);
            etemail = findViewById(R.id.editTextEmail);
        }

        public void showData(DataSnapshot dataSnapshot){
            for(DataSnapshot ds: dataSnapshot.getChildren()){
                User uInfo = new User();
                uInfo.setFullName(ds.child(userID).getValue(User.class).getFullName());
                uInfo.setEmail(ds.child(userID).getValue(User.class).getEmail());

                Log.d(TAG,"showData: fullName " + uInfo.getFullName());
                Log.d(TAG,"showData: email " + uInfo.getEmail());

                email.setText(uInfo.getEmail());
                fullName.setText(uInfo.getFullName());
                etemail.setText(uInfo.getEmail());

            }
        }
}
//    private FirebaseUser user;
//    private DatabaseReference reference;
//    private String userID;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.nav_header);
//
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//        userID = user.getUid();
//
////        final TextView emailTextView = (TextView) findViewById(R.id.emailAddress1);
////        final TextView fullNameTextView = (TextView) findViewById(R.id.fullName1);
//
////        get data from realtime database
//        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User userProfile = snapshot.getValue(User.class);
////              check if user profile exists
//                if (userProfile != null) {
//                    String fullName = userProfile.fullName;
//                    String email = userProfile.email;
//
//                    emailTextView.setText(email);
//                    fullNameTextView.setText(fullName);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ProfileActivity.this, "something wrong happened ", Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

