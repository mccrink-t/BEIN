package com.example.belfastinanutshell.Posts;

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

import com.example.belfastinanutshell.Home;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.example.belfastinanutshell.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewPost extends AppCompatActivity {

    private String postTitle, postDescription, postText;
    private ImageView inputPostImage;
    private EditText inputPostTitle, inputPostDescription, inputPostText;
    private Button submitPostBtn;
    private DatabaseReference postRef, usersRef;
    private StorageReference postImagesRef;
    private String saveCurrentDate, saveCurrentTime;
    private static final int imageChoice =1;
    private Uri imageURI;
    private String downloadImageUrl;
    private String postRandomKey;
    private ProgressDialog loadingBar;
    private TextView closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        postImagesRef = FirebaseStorage.getInstance().getReference().child("Post Images");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        inputPostImage = (ImageView) findViewById(R.id.select_post_image);
        inputPostTitle = (EditText) findViewById(R.id.post_title);
        inputPostDescription = (EditText) findViewById(R.id.description_post_text);
        inputPostText = (EditText) findViewById(R.id.post_text);
        submitPostBtn = (Button) findViewById(R.id.submit_Post_btn);
        loadingBar = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        closeBtn = (TextView) findViewById(R.id.close_add_new_post_btn);

        inputPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postReviewIntent = new Intent(AddNewPost.this, Home.class);
                startActivity(postReviewIntent);
            }
        });

        submitPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String fullName = snapshot.child("fullName").getValue().toString();
                            String userID = snapshot.child("phone").getValue().toString();

                            validatePostData(fullName, userID);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void validatePostData(String fullName, String userID)
    {
        postTitle = inputPostTitle.getText().toString();
        postDescription = inputPostDescription.getText().toString();
        postText = inputPostText.getText().toString();

        if(imageURI == null)
        {
            Toast.makeText(this, "Post Image required...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(postTitle))
        {
            Toast.makeText(this, "Please add post title...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(postDescription))
        {
            Toast.makeText(this, "Please add short post description...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(postText))
        {
            Toast.makeText(this, "Please write out your post...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            storePostInformation(fullName, userID);
        }
    }

    private void storePostInformation(String fullName, String userID)
    {
        loadingBar.setTitle("Submit Post");
        loadingBar.setMessage("Please wait whilst we submit your post");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //creating unique random key by combing current date and current time for each post
        postRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = postImagesRef.child(imageURI.getLastPathSegment() + postRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AddNewPost.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddNewPost.this, "Post Submitted Successfully!", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(AddNewPost.this, "Post Image URL Successfully add to the Database!", Toast.LENGTH_SHORT).show();

                            savePostToDatabase(fullName, userID);
                        }
                    }
                });
            }
        });
    }

    private void savePostToDatabase(String fullName, String userID)
    {
        int number = 0;
        String rating = String.valueOf(number);

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("postID", postRandomKey);
        postMap.put("date", saveCurrentDate);
        postMap.put("time", saveCurrentTime);
        postMap.put("image", downloadImageUrl);
        postMap.put("postTitle", postTitle);
        postMap.put("postDescription", postDescription);
        postMap.put("postText", postText);
        postMap.put("rating", rating);
        postMap.put("usersFullName", fullName);
        postMap.put("userID", userID);


        postRef.child("UserPosts").child(postRandomKey).updateChildren(postMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(AddNewPost.this, All_Posts.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AddNewPost.this, "Post submitted Successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddNewPost.this, "Error " +message, Toast.LENGTH_SHORT).show();
                        }
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
            inputPostImage.setImageURI(imageURI);
        }
    }

}