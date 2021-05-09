package com.example.belfastinanutshell.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belfastinanutshell.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    //Variables required for registration form.
//    private FirebaseAuth fAuth;
    private EditText inputFullName, inputEmail, inputPhone, inputPassword, inputConfirmPassword;
    //    private ProgressBar inputProgressBar;
    private TextView redirectLogin;
    private Button createAccountButton;
    private ProgressDialog loadingBar;


    //String to store email pattern
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //password pattern gained from https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-expressions-in-android/
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[@#$%^&+=!])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button) findViewById(R.id.registerBtn);
        inputFullName = (EditText) findViewById(R.id.register_fullName);
        inputEmail = (EditText) findViewById(R.id.register_email);
        inputPhone = (EditText) findViewById(R.id.register_phone);
        inputPassword = (EditText) findViewById(R.id.register_password);
        inputConfirmPassword = (EditText) findViewById(R.id.register_confirmPassword);
        loadingBar = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        redirectLogin = (TextView) findViewById(R.id.loginTxt);

//      When register button is clicked
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        redirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });
    }

    private void CreateAccount() {
        String fullName = inputFullName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();


//      Series of validation carried out on email and password fields
//      if email field is empty, display error text
//      Calls utils class to use isempty method
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Please Enter Your Email");
            return;
        }
        //if the entered email address does not match the email pattern
        else if (!email.matches(emailPattern)) {
            inputEmail.setError("Invalid email Address Layout");
        } else if (TextUtils.isEmpty(phone)) {
            inputPhone.setError("Please Enter Your Phone Number");
            return;
        } else if (phone.length() < 11 || phone.length() > 11) {
            inputPhone.setError("Please Enter a valid UK Mobile Number");
            return;
        } else if (TextUtils.isEmpty(fullName)) {
            inputFullName.setError("Please Enter Your Full Name");
            return;
        }
        //if password field is empty, display error text
        else if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Please enter a Password");
            return;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            inputPassword.setError("Password is too weak! \nPassword Must contain: " +
                    "\nAt least 6 characters, " +
                    "\nAt least 1 Special Character, " +
                    "\nAt least 1 Upper Case Letter," +
                    "\nAt least 1 Lower Case Letter," +
                    "\nAt least 1 Number," +
                    "\nAnd not include spaces");
        }

        //if confirm password field is empty, display error text
        else if (TextUtils.isEmpty(confirmPassword)) {
            inputConfirmPassword.setError("Please confirm your password");
            return;
        }

        //set password length to be more than 6
        else if (password.length() < 6) {
            inputPassword.setError("Password must be more than 6 Characters");
            return;
        }

        //Compares data in password field with data in confirm password field. If both are not the same then displays error
        else if (password.equals(confirmPassword) != true) {
            inputConfirmPassword.setError("Passwords do not match");
            return;
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait whilst we check your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhone(fullName, email, phone, password);
        }
    }

    private void ValidatePhone(String fullName, String email, String phone, String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("email", email);
                    userDataMap.put("fullName", fullName);
                    userDataMap.put("password", password);

                    rootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(Register.this, "Network Error, Please try again later..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Register.this, "Account with Phone Number: " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(Register.this, "Please try again using a different phone number.", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}