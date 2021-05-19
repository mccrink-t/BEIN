package com.example.belfastinanutshell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentWellbeing extends AppCompatActivity {

    private TextView homeWellBeingBtn;
    private TextView ulsterWellBeing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_wellbeing);

        homeWellBeingBtn = (TextView) findViewById(R.id.home_WellBeing_btn);
//        ulsterWellBeing = (TextView) findViewById(R.id.uuWellBeing_link);
//        ulsterWellBeing.setMovementMethod(LinkMovementMethod.getInstance());


        homeWellBeingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to start Home Activity
                Intent intent = new Intent(StudentWellbeing.this, Home.class);
                startActivity(intent);
            }
        });
    }
}