package com.example.belfastinanutshell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    //Variables required for registration form.
    EditText mFullName, mEMail, mPassword, mConfirmPassword;
    Button mRegisterBtn;
    TextView mLoginTxt;
    ProgressBar progressBar;
//    instance of firebase auth class in order to register user
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//      Variables linked to the xml resources
        mFullName = findViewById(R.id.fullName);
        mEMail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginTxt = findViewById(R.id.loginTxt);
        progressBar = findViewById(R.id.progressBar);
//      Retrieving current instance of the database from firebase
        fAuth = FirebaseAuth.getInstance();

//      Checks if user is already logged into the application, if they are it sends them to the MainActivity
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

//      When register button is clicked
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEMail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();

//              Series of validation carried out on email and password fields
                //if email field is empty, display error text
//              Calls utils class to use isempty method
                if(TextUtils.isEmpty(email)){
                    mEMail.setError("Please Enter Your Email");
                    return;
                }

                //if password field is empty, display error text
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Please enter a Password");
                    return;
                }

                //if confirm password field is empty, display error text
                if(TextUtils.isEmpty(confirmPassword)) {
                    mConfirmPassword.setError("Please confirm your password");
                    return;
                }

                //set password length to be more than 6
                if(password.length() < 6){
                    mPassword.setError("Password must be more than 6 Characters");
                    return;
                }

                //Compares data in password field with data in confirm password field. If both are not the same then displays error
                if(password.equals(confirmPassword) != true){
                    mConfirmPassword.setError("Passwords do not match");
                    return;
                }

                //Make the progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                //register the user into firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //Hide Progress bar if user fails login
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        //Redirect to login page
        mLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}