package com.example.belfastinanutshell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.Admin.AdminCategory;
import com.example.belfastinanutshell.Model.Admins;
import com.example.belfastinanutshell.Model.Users;
import com.example.belfastinanutshell.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    private EditText enteredPhone, enteredPassword;
    private Button loginBtn;
    private TextView registerTxt, forgetPassword;
    //    private ProgressBar progressBarLogin;
    private FirebaseAuth fAuth;
    private ProgressDialog loadingBar;
    private TextView redirectRegister;
    private CheckBox rememberMeBox;
    private String parentDbName = "Users";
    private TextView adminLink, notAdminLink;
    private TextView titleTxtView, subHeadingTxtView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        enteredPhone = (EditText) findViewById(R.id.phone_input);
        enteredPassword = (EditText) findViewById(R.id.password_input);
        loadingBar = new ProgressDialog(this);
        redirectRegister = (TextView) findViewById(R.id.registerTxt);
        forgetPassword = (TextView) findViewById(R.id.forgetPasswordTxt);
        rememberMeBox = (CheckBox) findViewById(R.id.checkBoxLog);
        Paper.init(this);
        adminLink = (TextView) findViewById(R.id.adminLoginPanel_link);
        notAdminLink = (TextView) findViewById(R.id.nonAdminLoginPanel_link);
        titleTxtView = (TextView) findViewById(R.id.beinTitleTxt);
        subHeadingTxtView = (TextView) findViewById(R.id.beinSubHeadingTxt);

//        fAuth = FirebaseAuth.getInstance();
//
//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),Home.class));
//            finish();
//        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setText("Login as Admin");
                titleTxtView.setText("BEIN Admin Login");
                subHeadingTxtView.setText("Please Log in using Admin Credentials");
                rememberMeBox.setVisibility(View.INVISIBLE);
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                redirectRegister.setVisibility(View.INVISIBLE);
                forgetPassword.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setText("Login");
                titleTxtView.setText("BEIN");
                subHeadingTxtView.setText("Belfast In A Nutshell");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                redirectRegister.setVisibility(View.VISIBLE);
                forgetPassword.setVisibility(View.VISIBLE);
                parentDbName = "Users";
            }
        });

        redirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void loginUser() {
        String phone = enteredPhone.getText().toString().trim();
        String password = enteredPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            enteredPhone.setError("Please Enter Your Phone Number");
            return;
        }

        //if password field is empty, display error text
        else if (TextUtils.isEmpty(password)) {
            enteredPassword.setError("Please enter Your Password");
            return;
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait whilst we check your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    public void resetPassword() {


    }

    private void AllowAccessToAccount(String phone, String password) {
        if (rememberMeBox.isChecked()) {
            Paper.book().write(Prevalent.UsersPhoneNumber, phone);
            Paper.book().write(Prevalent.UsersPasswordKey, password);
            Paper.book().write(Prevalent.AdminsPhoneNumber, phone);
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    Admins adminData = dataSnapshot.child(parentDbName).child(phone).getValue(Admins.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(Login.this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Login.this, AdminCategory.class);
                                Prevalent.CurrentOnlineAdmins = adminData;
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(Login.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(Login.this, Home.class);
                                Prevalent.CurrentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else
                            {
                            loadingBar.dismiss();
                            Toast.makeText(Login.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                else {
                    Toast.makeText(Login.this, "Account  with Phone Number: " + phone + " does not exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
//                    Toast.makeText(Login.this, "Please Create a new Account.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


//
//        //initalise login button
//        loginBtn = (Button) findViewById(R.id.loginBtn);
//
//        inputEmail = (EditText) findViewById(R.id.email_input);
//        inputPassword = (EditText) findViewById(R.id.password_input);
////        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
//
//        registerTxt = (TextView) findViewById(R.id.registerTxt);
//        forgetTxt = (TextView)  findViewById(R.id.forgetPasswordTxt);
//
//        fAuth = FirebaseAuth.getInstance();
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                get user input and convert to string
//                String email = inputEmail.getText().toString().trim();
//                String password = inputPassword.getText().toString().trim();
//
//                //if email field is empty, display error text
//                if(TextUtils.isEmpty(email)){
//                    inputEmail.setError("Please Enter Your Email");
//                    return;
//                }
//
//                //if password field is empty, display error text
//                if(TextUtils.isEmpty(password)) {
//                    inputPassword.setError("Please enter a Password");
//                    return;
//                }
//
//                //set password length to be more than 6
//                if(password.length() < 6){
//                    inputPassword.setError("Password must be more than 6 Characters");
//                    return;
//                }
//
//                //Make the progress bar visible
////                progressBarLogin.setVisibility(View.VISIBLE);
//
//
//                //authenticating user
//                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            //redirect to user profile
//                            Toast.makeText(Login.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        }
//                        else{
//                            //error
//                            Toast.makeText(Login.this, "Error! Failed to Login" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                            //Hide Progress bar if user fails login
////                            progressBarLogin.setVisibility(View.GONE);
//                        }
//
//                    }
//                });
//
//            }
//        });
//
//        //Redirect to Register page
//        registerTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Register.class));
//            }
//        });
//
//        forgetTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText resetMail = new EditText(v.getContext());
//                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
//                passwordResetDialog.setTitle("Reset Password?");
//                passwordResetDialog.setMessage("Enter your Email to Receive Reset Password Link");
//                passwordResetDialog.setView(resetMail);
//
//                passwordResetDialog.setPositiveButton(" Reset", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //get email and send reset link
//
//                        String mail = resetMail.getText().toString();
//                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(Login.this, "The Reset link has been sent to your Email.", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Login.this, "Error! Reset Link is Not Sent." + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//
//                passwordResetDialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Redirect back to login
//                    }
//                });
//
//                passwordResetDialog.create().show();
//            }
//        });
//
//    }
//}