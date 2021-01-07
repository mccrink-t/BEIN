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

public class Login extends AppCompatActivity {

    EditText mEMail, mPassword;
    Button mLoginBtn;
    TextView mRegisterTxt;
    ProgressBar progressBarLogin;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEMail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterTxt = findViewById(R.id.registerTxt);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        fAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEMail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //if email field is empty, display error text
                if(TextUtils.isEmpty(email)){
                    mEMail.setError("Please Enter Your Email");
                    return;
                }

                //if password field is empty, display error text
                if(TextUtils.isEmpty(password)) {
                    mPassword.setError("Please enter a Password");
                    return;
                }

                //set password length to be more than 6
                if(password.length() < 6){
                    mPassword.setError("Password must be more than 6 Characters");
                    return;
                }

                //Make the progress bar visible
                progressBarLogin.setVisibility(View.VISIBLE);


                //authenticating user

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //Hide Progress bar if user fails login
                            progressBarLogin.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        //Redirect to login page
        mRegisterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });


    }
}