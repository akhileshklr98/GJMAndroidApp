package com.example.testapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private String latitude,longitude;
    String PunchDate;
    String PunchTime;
    String TypeOfPunch;
    String formatTime;

    TextView txtView,txtDate, txtTime, txtLat, txtLon, viewLat, viewLon;
    Button btnPunchIn,btnPunchOut,btnShowSchedules;
//    public WmsDB _loginCrdentials;
    Location gpsLocation,networkLocation,passiveLocation;

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
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserActivity.this);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        txtDate.setText(formattedDate);

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.getAllProviders();
        final ConnectivityManager connectivityManager =  (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean connecton = connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

        try {
            Intent intent = getIntent();
            _userName = intent.getStringExtra("username");
            _password = intent.getStringExtra("password");
        }catch (Exception e) {
            e.printStackTrace();
        }

        gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        passiveLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (gpsLocation != null){
            double lat = gpsLocation.getLatitude();
            double lon = gpsLocation.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(lon);
            Toast.makeText(getApplicationContext(),"GPS "+latitude+" "+longitude,Toast.LENGTH_SHORT).show();
        } else if (networkLocation != null){
            double lat = networkLocation.getLatitude();
            double lon = networkLocation.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(lon);
            Toast.makeText(getApplicationContext(),"Network "+latitude+" "+longitude,Toast.LENGTH_SHORT).show();
        } else if (passiveLocation != null){
            double lat = passiveLocation.getLatitude();
            double lon = passiveLocation.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(lon);
            Toast.makeText(getApplicationContext(),"Paasive "+latitude+" "+longitude,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Can't Get Your Location", Toast.LENGTH_SHORT).show();
        }
//        txtView.setText(_userName);
        viewLat.setText("Latitude : "+latitude);
        viewLon.setText("Longitude : "+longitude);

        CheckPunchedInOut("","");

//        Toast.makeText(getApplicationContext(), _userName, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), _password, Toast.LENGTH_SHORT).show();

        btnPunchIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean location = locationManager.isLocationEnabled();
                if(!location){
                    Toast.makeText(getApplicationContext(), "Please Turn On Location", Toast.LENGTH_SHORT).show();
                }else {
//                    Toast.makeText(getApplicationContext(), "Punch In", Toast.LENGTH_SHORT).show();
                    String punchType = "Punch In";
                    String mySchedule = "";
                    CheckPunchedInOut(punchType, mySchedule);
                }
            }
        });

        btnShowSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"My Today Schedule List",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(UserActivity.this,ScheduleActivity.class);
//                startActivity(intent);
            }
        });

        btnPunchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String punchType = "Punch Out";
                String mySchedule = "";
                CheckPunchedInOut(punchType, mySchedule);
//                Toast.makeText(getApplicationContext(),"Punch Out", Toast.LENGTH_SHORT).show();
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
                                        txtView.setText("Todays Attendance Completed at "+ PunchTime);
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
                                    Toast.makeText(getApplicationContext(), "Todays Schedule Completed!!!" , Toast.LENGTH_SHORT).show();
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

    private void SaveWorkReport(String punchType) {
        Toast.makeText(getApplicationContext(), punchType, Toast.LENGTH_SHORT).show();
    }

}
