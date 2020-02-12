package com.example.testapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WorkReport extends AppCompatActivity {
    private EditText txtRemarks;
    private TextView txtViewLat, txtViewLon, _affNoView, _hospitalnameView, _employeeID, _myscheduleIDDisplay, Purpose1, Purpose2;
    private Button punchin, report, notVisited;

    private String _myscheduleId, _username, _scheduleId, _password, gpsLat, gpsLon, remarks;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private ActionBar actionBar;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);

        progressDialog = new ProgressDialog(WorkReport.this);

        txtViewLat = findViewById(R.id.tvLatitude);
        txtViewLon = findViewById(R.id.tvLongitude);

        try {
            actionBar = getSupportActionBar();
            actionBar.setTitle("Work Report Status");
            actionBar.setDisplayHomeAsUpEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#075E54"));
            actionBar.setBackgroundDrawable(colorDrawable);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            Intent intent = getIntent();
            _myscheduleId = intent.getStringExtra("MyScheduleID");
            _username = intent.getStringExtra("username");
            _password = intent.getStringExtra("password");
        }catch (Exception e){
            e.printStackTrace();
        }

        _affNoView = findViewById(R.id.affNoView);
        _hospitalnameView = findViewById(R.id.hospitalnameView);
        _employeeID = findViewById(R.id.employeeIDView);
        _myscheduleIDDisplay = findViewById(R.id.scheduleIDView);
        txtViewLat = findViewById(R.id.tvLatitude);
        txtViewLon = findViewById(R.id.tvLongitude);
        Purpose1 = findViewById(R.id.purpose1);
        Purpose2 = findViewById(R.id.purpose2);

        punchin = findViewById(R.id.punchIn);
        report = findViewById(R.id.report);
        notVisited = findViewById(R.id.notVisited);

        punchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String status = "Visited";
                    CheckVisitedStatus(status, "");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String Status="Visited";
                    CheckVisitedStatus(Status,"Report");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        notVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InitiatePopupWindow(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        locationManager.getAllProviders();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                txtViewLat.setText(String.valueOf(location.getLatitude()));
                txtViewLon.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Location","status");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Location","enable");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 100);

                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10.0F, locationListener);
    }

    private void InitiatePopupWindow(View v) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.pop_up_notvisited, null);

            alertDialog = new AlertDialog.Builder(this);
            alertDialog.setView(promptView);
            alertDialog.setTitle("Not Visited Reason");

            txtRemarks = promptView.findViewById(R.id.remarksTextViewTxt);

            alertDialog.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    remarks = txtRemarks.getText().toString();
                    if (remarks != null && !remarks.equals("")){
                        String Status = "NotVisited";
                        CheckVisitedStatus(Status, "");
                    }else {
                        Toast.makeText(getApplicationContext(), "Please enter not visited reason", Toast.LENGTH_SHORT).show();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDia = alertDialog.create();
            alertDia.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SaveReason() {
        try {
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Saving Data...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.URL_INSERT_REASON,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message ="Work Report Saved Successfully";
                                finish();
                                if (success == 1){
                                    Intent intent = new Intent(WorkReport.this, ScheduleActivity.class);
                                    intent.putExtra("MyScheduleID",_myscheduleId);
                                    intent.putExtra("username",_username);
                                    intent.putExtra("password",_password);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Failed To Save Data! Network Error", Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("MyScheduleID", _myscheduleId);
                    params.put("status", "NotVisited");
                    params.put("Remarks", remarks);
                    params.put("UserName", _username);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SaveWorkReport(final String status) {
        gpsLat = txtViewLat.getText().toString();
        gpsLon = txtViewLon.getText().toString();
        if(gpsLat !="" && !gpsLat.equals("") && gpsLon !="" && !gpsLon.equals("")){
            try {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Saving Data...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constant.URL_INSERT_WORK,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    finish();
                                    if (success == 1){
                                        if (status.equals("Visited")){
                                            Toast.makeText(getApplicationContext(), "You are successfully visited", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(WorkReport.this, WorkReport.class);
                                            intent.putExtra("MyScheduleID",_myscheduleId);
                                            intent.putExtra("username",_username);
                                            intent.putExtra("password",_password);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getApplicationContext(), "You are successfully completed", Toast.LENGTH_SHORT).show();
                                        }
                                    }else if (success == 2){
                                        Toast.makeText(getApplicationContext(), "You are already completed this Schedule", Toast.LENGTH_SHORT).show();
                                    }else if (success == 3){
                                        Toast.makeText(getApplicationContext(), "You are successfully exit", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(WorkReport.this, VisitedActivity.class);
                                        intent.putExtra("MyScheduleID",_myscheduleId);
                                        intent.putExtra("username",_username);
                                        intent.putExtra("password",_password);
                                        intent.putExtra("purpose1",Purpose1.getText().toString());
                                        intent.putExtra("purpose2",Purpose2.getText().toString());
                                        intent.putExtra("affNo",_affNoView.getText().toString());
                                        startActivity(intent);
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Failed To Save Data! Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("MyScheduleID", _myscheduleId);
                        params.put("GpsLatitude", gpsLat);
                        params.put("GpsLongitude", gpsLon);
                        params.put("Status", status);
                        params.put("UserName", _username);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Fetching Current Location ...Please Wait!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading Work Report...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.URL_WORK_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray;
                            int success = jsonObject.getInt("success");
                            if (success == 1){
                                jsonArray = jsonObject.getJSONArray("WorkReport");
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject temp = jsonArray.getJSONObject(i);
                                    if(temp.getString("AffNo")!=null && !temp.getString("AffNo").equals("null")) {
                                        _affNoView.setText(temp.getString("AffNo"));
                                    }
                                    if(temp.getString("Hospital")!=null && !temp.getString("Hospital").equals("null")) {
                                        _hospitalnameView.setText(temp.getString("Hospital"));
                                    }
                                    if(temp.getString("EmployeeID")!=null && !temp.getString("EmployeeID").equals("null")) {
                                        _employeeID.setText(temp.getString("EmployeeID"));
                                    }
                                    if(temp.getString("MyScheduleID")!=null && !temp.getString("MyScheduleID").equals("null")) {
                                        _myscheduleIDDisplay.setText(temp.getString("MyScheduleID"));
                                    }
                                    if(temp.getString("Purpose1")!=null && !temp.getString("Purpose1").equals("null")) {
                                        Purpose1.setText(temp.getString("Purpose1"));
                                    }
                                    if(temp.getString("Purpose2")!=null && !temp.getString("Purpose2").equals("null")) {
                                        Purpose2.setText(temp.getString("Purpose2"));
                                    }else {
                                        Purpose2.setText("0");
                                    }
                                }
                            }else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed To Load Data! Network Error", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MyScheduleID", _myscheduleId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        locationManager.getAllProviders();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                txtViewLat.setText(String.valueOf(location.getLatitude()));
                txtViewLon.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Location","status");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Location","enable");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);

                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10.0F, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        alertDialog = new AlertDialog.Builder(this);
        int id = item.getItemId();

        //menu items
        if (id == R.id.action_refresh){
            try {
                onStart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id == R.id.action_logout) {
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Are you sure you want to logout?");

            alertDialog.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(WorkReport.this, LoginActivity.class);
                        intent.putExtra("username",_password);
                        intent.putExtra("password",_password);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.create().show();
            return true;
        }
        else if (id == R.id.action_about) {
            try {
                Intent intent = new Intent(WorkReport.this, AboutActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        else {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void CheckVisitedStatus(final String Status, final String Report) {
        super.onStart();
        alertDialog = new AlertDialog.Builder(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.URL_EMOB_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1){
                                if (Report != null && Report.equals("Report")){
                                    Toast.makeText(getApplicationContext(), "You can't view report without visit", Toast.LENGTH_SHORT).show();
                                }else {
                                    alertDialog.setTitle("Confirm Status...");
                                    alertDialog.setMessage("Are you sure you want to save..?");

                                    alertDialog.setCancelable(false).setPositiveButton("Save",
                                            new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            if(Status.equals("NotVisited")){
//                                                String Status = "NotVisited";
                                                SaveWorkReport(Status);
                                                SaveReason();
                                            }
                                            else {
                                                SaveWorkReport(Status);
                                            }
                                        }
                                    }).setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    alertDialog.create().show();
                                }
                            }else if (success == 2){
                                if (Status != null && Status.equals("Visited") && Report == ""){
                                    Toast.makeText(getApplicationContext(), "You have already entered", Toast.LENGTH_SHORT).show();
                                }else if (Status !=null && Status.equals("NotVisited") && Report == ""){
                                    Toast.makeText(getApplicationContext(), "Not allowed, already entered", Toast.LENGTH_SHORT).show();
                                }else {
                                    SaveWorkReport("Report");
                                }
                            }else if (success == 3){
                                if (Status != null && Status.equals("NotVisited") && Report == ""){
                                    Toast.makeText(getApplicationContext(), "You have already updated visited reason", Toast.LENGTH_SHORT).show();
                                }
                                if (Status != null && Status.equals("Visited") && Report !=null && Report.equals("Report")){
                                    SaveWorkReport("Report");
                                }
                            }else if (success == 4){
                                Toast.makeText(getApplicationContext(), "You have pending report", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Failed To Load Data! Network Error", Toast.LENGTH_SHORT).show();
                        WorkReport.this.finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserName", _username);
                params.put("MyScheduleID", _myscheduleId);
                params.put("Status", Status);
                params.put("Report", Report);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
