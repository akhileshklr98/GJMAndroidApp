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
import android.view.MenuInflater;
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
    private TextView txtViewLat, txtViewLon, _affNoView, _hospitalnameView, _employeeID, _myscheduleIDDisplay;
    private Button punchin, report, notVisited;

    private String _myscheduleId, _username, _password, _scheduleId, gpsLat, gpsLon, remarks;

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
        alertDialog = new AlertDialog.Builder(this);

        txtViewLat = findViewById(R.id.tvLatitude);
        txtViewLon = findViewById(R.id.tvLongitude);

        try {
            actionBar = getSupportActionBar();
            actionBar.setTitle("Work Report Status");
            actionBar.setDisplayShowTitleEnabled(true);
            ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#ff06972c"));
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

        punchin = findViewById(R.id.punchIn);
        report = findViewById(R.id.report);
        notVisited = findViewById(R.id.notVisited);

        punchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Enter", Toast.LENGTH_LONG).show();
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
//                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_LONG).show();
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
//                Toast.makeText(getApplicationContext(), "No visit", Toast.LENGTH_LONG).show();
                try {
                    InitiatePopupWindow(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
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
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
    }

    private void InitiatePopupWindow(View v) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.pop_up_notvisited, null);

            alertDialog.setView(promptView);
            alertDialog.setTitle("Not Visited Reason");

            txtRemarks = promptView.findViewById(R.id.remarksTextViewTxt);

            alertDialog.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    remarks = txtRemarks.getText().toString();
                    String Status="NotVisited";
                    CheckVisitedStatus(Status,null);
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

    private void CheckVisitedStatus(final String Status, final String Report) {
        super.onStart();
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
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1){
                                Toast.makeText(getApplicationContext(), "You are already updated details", Toast.LENGTH_SHORT).show();
                            }else if (success == 2){
                                if (Report != "" && Report.equals("Report")){
                                    Toast.makeText(getApplicationContext(), "You can't view report without visit", Toast.LENGTH_SHORT).show();
                                }else {
                                    alertDialog.setTitle("Confirm Status...");
                                    alertDialog.setMessage("Are you sure you want to save..?");

                                    alertDialog.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            if(Status.equals("NotVisited")){
                                                SaveWorkReport("NotVisited");
                                                SaveReason();
                                            } else {
                                                SaveWorkReport(Status);
                                            }
                                        }
                                    }).setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    alertDialog.show();
                                }
                            }else if (success == 3){
                                SaveWorkReport("Report");
                            }else if (success == 4){
                                Toast.makeText(getApplicationContext(), "You are already updated visited reason", Toast.LENGTH_SHORT).show();
                            }else {
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
//                        finish();
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
//        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Fetching GPS Coordinates...Please Wait!!", Toast.LENGTH_SHORT).show();
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
                                    if(temp.getString("AffNo")!=null && !temp.getString("AffNo").equals("null"))
                                    {
                                        _affNoView.setText(temp.getString("AffNo"));
                                    }
                                    if(temp.getString("Hospital")!=null && !temp.getString("Hospital").equals("null"))
                                    {
                                        _hospitalnameView.setText(temp.getString("Hospital"));
                                    }

                                    if(temp.getString("EmployeeID")!=null && !temp.getString("EmployeeID").equals("null"))
                                    {
                                        _employeeID.setText(temp.getString("EmployeeID"));
                                    }
                                    if(temp.getString("MyScheduleID")!=null && !temp.getString("MyScheduleID").equals("null"))
                                    {
                                        _myscheduleIDDisplay.setText(temp.getString("MyScheduleID"));
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.refresh:
                try {
                    onStart();
//                    getLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.action_logout:
                try {
                    Intent intent = new Intent(WorkReport.this, LoginActivity.class);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.action_back:
                try {
                    Intent intent = new Intent(WorkReport.this, ScheduleActivity.class);
                    intent.putExtra("ScheduleID",_scheduleId);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    startActivity(intent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.home:
                try {
                    finish();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
