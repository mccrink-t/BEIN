package com.example.belfastinanutshell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //Variables required for registration form.
    private FirebaseAuth fAuth;
    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private ProgressBar progressBar;
    TextView mLoginTxt;
    Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
        mLoginTxt = (TextView) findViewById(R.id.loginTxt);

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
                                                String email = editTextEmail.getText().toString().trim();
                                                String password = editTextPassword.getText().toString().trim();
                                                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                                                String fullName = editTextFullName.getText().toString().trim();

//              Series of validation carried out on email and password fields
                                                //if email field is empty, display error text
//              Calls utils class to use isempty method
                                                if (TextUtils.isEmpty(email)) {
                                                    editTextEmail.setError("Please Enter Your Email");
                                                    return;
                                                }

                                                if (TextUtils.isEmpty(fullName)) {
                                                    editTextFullName.setError("Please Enter Your Full Name");
                                                    return;
                                                }

                                                //if password field is empty, display error text
                                                if (TextUtils.isEmpty(password)) {
                                                    editTextPassword.setError("Please enter a Password");
                                                    return;
                                                }

                                                //if confirm password field is empty, display error text
                                                if (TextUtils.isEmpty(confirmPassword)) {
                                                    editTextConfirmPassword.setError("Please confirm your password");
                                                    return;
                                                }

                                                //set password length to be more than 6
                                                if (password.length() < 6) {
                                                    editTextPassword.setError("Password must be more than 6 Characters");
                                                    return;
                                                }

                                                //Compares data in password field with data in confirm password field. If both are not the same then displays error
                                                if (password.equals(confirmPassword) != true) {
                                                    editTextConfirmPassword.setError("Passwords do not match");
                                                    return;
                                                }

//                checks if email address is valid -- aka .com .co.uk @ etc
//                                                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                                                    editTextEmail.setError("Please provide a valid Email");
//                                                    editTextEmail.requestFocus();
//                                                    return;
//                                                }

                                                //Make the progress bar visible
                                                progressBar.setVisibility(View.VISIBLE);

                                                //register the user into firebase
                                                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            User user = new User(fullName, email);

                                                            FirebaseDatabase.getInstance().getReference("Users")
                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.GONE);
                                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));


                                                                    } else {
                                                                        Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        //Hide Progress bar if user fails login
                                                                        progressBar.setVisibility(View.GONE);
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            //Hide Progress bar if user fails login
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });

                                            }
                                        });
                mLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}


        //Redirect to login page
//        mLoginTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Login.class));
//            }
//        });