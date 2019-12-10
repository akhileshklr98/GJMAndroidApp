package com.example.testapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private String _userName;
    private String _password;
    private double latitude, longitude;
    String PunchDate;
    String PunchTime;
    String TypeOfPunch;

    TextView txtView, txtDate, txtTime, txtLat, txtLon, viewLat, viewLon;
    Button btnPunchIn, btnPunchOut, btnShowSchedules;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txtView = findViewById(R.id.TextViewNotify);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        btnPunchIn = findViewById(R.id.punchIn);
        btnPunchOut = findViewById(R.id.punchOut);
        btnShowSchedules = findViewById(R.id.mySchedules);
        txtLat = findViewById(R.id.txtLatitude);
        txtLon = findViewById(R.id.txtLongitude);
        viewLat = findViewById(R.id.txtViewLat);
        viewLon = findViewById(R.id.txtViewLon);
        Calendar c = Calendar.getInstance();

        progressDialog = new ProgressDialog(this);
        alertDialog = new AlertDialog.Builder(UserActivity.this);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        txtDate.setText(formattedDate);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.getAllProviders();

        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Main Screen");

            actionBar.setDisplayShowTitleEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ff06972c"));
            actionBar.setBackgroundDrawable(colorDrawable);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            _userName = intent.getStringExtra("username");
            _password = intent.getStringExtra("password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Check Punch in or Out */
        CheckPunchedInOut("","");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                viewLat.setText("Latitude : "+latitude);
                viewLon.setText("Longitude : "+longitude);
//                Toast.makeText(getApplicationContext(),location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Location", "Status Changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
//                Log.d("Location", "Enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
//                }, 101);
                return;
            }
        }
        locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);

        /* Click Punch In Button */
        btnPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String punchType = "Punch In";
                String mySchedule = "";
                if (locationManager.isProviderEnabled("gps")){
                    if (txtLat.getText().toString().equals("") && txtLon.getText().toString().equals("") &&
                            txtLat.getText().toString() == "" && txtLon.getText().toString() == ""){
                        Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                    }else {
                        CheckPunchedInOut(punchType, mySchedule);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please Turn On Location And Try Again...!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* Click Schedule Button */
        btnShowSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String punchType="Punch In";
                String mySchedule="MySchedule";
                if (locationManager.isProviderEnabled("gps")){
                    CheckPunchedInOut(punchType, mySchedule);
                }else {
                    Toast.makeText(getApplicationContext(), "Please Turn On Location And Try Again...!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* Click Punch Out Button */
        btnPunchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String punchType = "Punch Out";
                String mySchedule = "";
                if (locationManager.isProviderEnabled("gps")){
//                    CheckPunchedInOut(punchType, mySchedule);
                    if (txtLat.getText().toString().equals("") && txtLon.getText().toString().equals("") &&
                            txtLat.getText().toString() == "" && txtLon.getText().toString() == ""){
                        Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                    }else {
                        CheckPunchedInOut(punchType, mySchedule);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please Turn On Location And Try Again...!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CheckPunchedInOut(final String PunchType, final String MySchedule) {
        super.onStart();
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.URL_USER_MAIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray;
                            int success = jsonObject.getInt("success");
                            if (success == 1){
                                //already punch in
//                                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                                jsonArray = jsonObject.getJSONArray("CheckPunch");
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject temp = jsonArray.getJSONObject(i);
                                    if (temp.getString("PunchDate") != null && !temp.getString("PunchDate").equals("null")) {
                                        PunchDate = temp.getString("PunchDate");
                                    }
                                    if (temp.getString("PunchTime") != null && !temp.getString("PunchTime").equals("null")) {
                                        PunchTime = temp.getString("PunchTime");
                                    }
                                    if (temp.getString("TypeOfPunch") != null && !temp.getString("TypeOfPunch").equals("null")) {
                                        TypeOfPunch = temp.getString("TypeOfPunch");
                                    }
                                    if(PunchType!=null && !PunchType.equals("")){
                                        if(MySchedule!=null && MySchedule.equals("MySchedule")){
                                            Intent intent = new Intent(UserActivity.this, ScheduleActivity.class);
                                            intent.putExtra("username",_userName);
                                            intent.putExtra("password",_password);
                                            startActivity(intent);
                                        }

                                        else{
                                            Toast.makeText(getApplicationContext(), "You Are Already Punched In at " + PunchTime, Toast.LENGTH_SHORT).show();
                                            txtView.setText("You Are Already Punched In at " + PunchTime);
                                            progressDialog.dismiss();
                                        }
                                    }
                                    else{
                                        txtView.setText("You are Punched In at "+ PunchTime);
                                    }
                                }
                            }
                            if (success == 2){
//                                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
                                jsonArray = jsonObject.getJSONArray("CheckPunch");
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject temp = jsonArray.getJSONObject(i);
                                    if (temp.getString("PunchDate") != null && !temp.getString("PunchDate").equals("null")) {
                                        PunchDate = temp.getString("PunchDate");
                                    }

                                    if (temp.getString("PunchTime") != null && !temp.getString("PunchTime").equals("null")) {
                                        PunchTime = temp.getString("PunchTime");
                                    }

                                    if (temp.getString("TypeOfPunch") != null && !temp.getString("TypeOfPunch").equals("null")) {
                                        TypeOfPunch = temp.getString("TypeOfPunch");
                                    }
                                    if(PunchType!=null && !PunchType.equals("")){
                                        Toast.makeText(getApplicationContext(), "You Are Already Punched Out at " + PunchTime, Toast.LENGTH_SHORT).show();
                                        txtView.setText("You Are Already Punched Out at " + PunchTime);
                                    }
                                    else{
                                        txtView.setText("Today Attendance Completed at "+ PunchTime);
                                    }
                                }
                            }
                            if (success == 3){
                                //no punch in
//                                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
                                if(MySchedule!=null && MySchedule.equals("MySchedule" )){
                                    Toast.makeText(getApplicationContext(), "You Can't View Without Punch In!!!" , Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(PunchType != null && !PunchType.equals("")) {
                                        alertDialog.setTitle("Confirm Attendance...");
                                        alertDialog.setMessage("Are you sure you want to " + PunchType + "?");
                                        alertDialog.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                SaveWorkReport(PunchType);
//                                                Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                                            }
                                        }).setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        alertDialog.show();
                                    }

                                }
                            }
                            if (success == 4){
                                //no punch in --punch out
//                                Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "You Can't Punch Out Without Punch In!!!" , Toast.LENGTH_SHORT).show();

                                txtView.setText("You Can't Punch Out Without Punch In!!!");
                            }
                            if (success == 5){
                                //complete attendance
//                                Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_LONG).show();
                                if(PunchType!=null && !PunchType.equals("")){
                                    Toast.makeText(getApplicationContext(), "Today Schedule Completed!!!" , Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    txtView.setText("Todays Attendance Completed !!");
                                }
                            }else {
                                String message=jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
//                            if (success == 0){
//                                //no connect server
//                                Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_LONG).show();
//                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
                Map<String, String> params = new HashMap<>();
                params.put("UserName", _userName);
                params.put("type", PunchType);
                params.put("MySchedule", MySchedule);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SaveWorkReport(final String punchType) {
//        Toast.makeText(getApplicationContext(), punchType, Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Saving Attendance...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.URL_USER_PUNCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 2){
                                if (punchType.equals("Punch Out")){
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "You Are Successfully Punched Out",Toast.LENGTH_SHORT).show();
                                }else {
                                    try {
                                        Intent intent = new Intent(UserActivity.this, ScheduleActivity.class);
                                        intent.putExtra("username",_userName);
                                        intent.putExtra("password",_password);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "You Are Successfully Punched In",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else if (success == 1){
                                try{
                                    Intent intent = new Intent(UserActivity.this, UserActivity.class);
                                    intent.putExtra("username",_userName);
                                    intent.putExtra("password",_password);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "You Are Already " + punchType,Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
                Map<String, String> params = new HashMap<>();
                params.put("GpsLatitude", viewLat.getText().toString());
                params.put("GpsLongitude", viewLon.getText().toString());
                params.put("UserName", _userName);
                params.put("Password", _password);
                params.put("PunchType", punchType);
                params.put("Date", txtDate.getText().toString());
                params.put("Time", txtTime.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
