package com.example.belfastinanutshell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.DialogRedirect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button loginBtn;
    private TextView registerTxt, forgetTxt;
    private ProgressBar progressBarLogin;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initalise login button
        loginBtn = (Button) findViewById(R.id.loginBtn);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);

        registerTxt = (TextView) findViewById(R.id.registerTxt);
        forgetTxt = (TextView)  findViewById(R.id.forgetPasswordTxt);

        fAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get user input and convert to string
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                //if email field is empty, display error text
                if(TextUtils.isEmpty(email)){
                    editTextEmail.setError("Please Enter Your Email");
                    return;
                }

                //if password field is empty, display error text
                if(TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Please enter a Password");
                    return;
                }

                //set password length to be more than 6
                if(password.length() < 6){
                    editTextPassword.setError("Password must be more than 6 Characters");
                    return;
                }

                //Make the progress bar visible
                progressBarLogin.setVisibility(View.VISIBLE);


                //authenticating user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //redirect to user profile
                            Toast.makeText(Login.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            //error
                            Toast.makeText(Login.this, "Error! Failed to Login" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            //Hide Progress bar if user fails login
                            progressBarLogin.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        //Redirect to Register page
        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        forgetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email to Receive Reset Password Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton(" Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get email and send reset link

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "The Reset link has been sent to your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset Link is Not Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Redirect back to login
                    }
                });

                passwordResetDialog.create().show();
            }
        });

    }
}