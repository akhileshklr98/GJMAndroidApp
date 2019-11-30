package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserActivity extends AppCompatActivity {

    Button btnPunchIn,btnPunchOut,btnShowSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnPunchIn = findViewById(R.id.punchin);
        btnPunchOut = findViewById(R.id.punchout);
        btnShowSchedules = findViewById(R.id.myschedules);

        btnPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Puch In Successfull",Toast.LENGTH_LONG).show();
            }
        });

        btnShowSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"My Today Schedule List",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UserActivity.this,ScheduleActivity.class);
                startActivity(intent);
            }
        });

        btnPunchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Punch Out", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
