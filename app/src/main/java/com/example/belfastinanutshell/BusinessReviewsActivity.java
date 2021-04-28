package com.example.belfastinanutshell;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BusinessReviewsActivity extends AppCompatActivity {

    private ImageView postBusinessReviewBtn;
    private EditText businessReviewInputText;
    private RecyclerView businessReviewList;

    private DatabaseReference usersRef, businessRef;

    private String Post_Key;
//    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_reviews);

        Post_Key = getIntent().getExtras().get("bID").toString();

//        mAuth = FirebaseAuth.getInstance();
//        current_user_id = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        businessRef = FirebaseDatabase.getInstance().getReference().child("Businesses").child(Post_Key).child("Reviews");

        businessReviewList = (RecyclerView) findViewById(R.id.businessReviewList);
        businessReviewList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        businessReviewList.setLayoutManager(linearLayoutManager);

        businessReviewInputText = (EditText) findViewById(R.id.businessReviewInput);
        postBusinessReviewBtn = (ImageView) findViewById(R.id.postBusinessReviewBtn);

        postBusinessReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String fullName = snapshot.child("fullName").getValue().toString();
                            String userID = snapshot.child("phone").getValue().toString();

                            ValidateBusinessReview(fullName, userID);

                            businessReviewInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void ValidateBusinessReview(String fullName, String userID) {
        String businessReviewText = businessReviewInputText.getText().toString();
        String saveCurrentDate, saveCurrentTime;
        String randomIdGeneration;

        if (TextUtils.isEmpty(businessReviewText)) {
            Toast.makeText(this, "Please Write Your Review to submit", Toast.LENGTH_SHORT).show();
        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            randomIdGeneration = saveCurrentDate + saveCurrentTime;

            HashMap businessReviewsMap = new HashMap();
                businessReviewsMap.put("uid", userID);
                businessReviewsMap.put("review", businessReviewText);
                businessReviewsMap.put("date", saveCurrentDate);
                businessReviewsMap.put("time", saveCurrentTime);
                businessReviewsMap.put("fullName", fullName);

            businessRef.child(randomIdGeneration).updateChildren(businessReviewsMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(BusinessReviewsActivity.this, "Review Added Successfully!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(BusinessReviewsActivity.this, "Error Occurred, Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}