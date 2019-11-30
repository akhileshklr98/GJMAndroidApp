package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    Button btnScheduleList, btnMapView, btnAttendance, btnViewTimeCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnAttendance = findViewById(R.id.AttendanceList);
        btnMapView = findViewById(R.id.WebView);
        btnScheduleList = findViewById(R.id.ScheduledList);
        btnViewTimeCard = findViewById(R.id.AttendanceMapView);

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "Attendance Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        btnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "Map View Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        btnScheduleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "Schedule List Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        btnViewTimeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "View Time Card Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
