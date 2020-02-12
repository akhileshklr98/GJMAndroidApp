package com.example.testapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

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

public class ScheduleActivity extends AppCompatActivity {

    private MyScheduleDetailListAdapter _adapter;
    private String _username;
    private String _password;
    boolean GpsStatus ;

    private ActionBar actionBar;
    private Intent intent;
    private ProgressDialog _progressDialog;
    private AlertDialog.Builder alertDialog;
    Context context;
    protected android.content.Context Context;

    private ListView scheduleDetailsLstView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        alertDialog = new AlertDialog.Builder(ScheduleActivity.this);

        try {
            actionBar = getSupportActionBar();
            actionBar.setTitle("Scheduled Hospitals");
            actionBar.setDisplayHomeAsUpEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#075E54"));
            actionBar.setBackgroundDrawable(colorDrawable);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            intent = getIntent();
            _username = intent.getStringExtra("username");
            _password = intent.getStringExtra("password");
        }catch (Exception e){
            e.printStackTrace();
        }

        scheduleDetailsLstView = findViewById(R.id.myScheduleListDetailsView);
        this._adapter = new MyScheduleDetailListAdapter(ScheduleActivity.this, R.layout.myschedule_list_layout);
        scheduleDetailsLstView.setAdapter(this._adapter);

        /* Defining an itemClick event listener for the autoCompleteTextView */
        scheduleDetailsLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                String scheduleID="";
                MyScheduleDetailsPopulate myScheduleDetailsPopulate = (MyScheduleDetailsPopulate) _adapter.getItem(position);
                if(myScheduleDetailsPopulate!=null && myScheduleDetailsPopulate.myScheduleID!="") {
                    Intent intent = new Intent(ScheduleActivity.this, WorkReport.class);
                    intent.putExtra("MyScheduleID",myScheduleDetailsPopulate.myScheduleID);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    startActivity(intent);
                }
            }
        });
        /* Load details */
        LoadMyScheduleList();
        this._adapter.notifyDataSetChanged();
    }

    private void LoadMyScheduleList() {
        _progressDialog = new ProgressDialog(ScheduleActivity.this);
        _progressDialog.setMessage("Loading Scheduled Hospital List...");
        _progressDialog.setIndeterminate(false);
        _progressDialog.setCancelable(false);
        _progressDialog.setCanceledOnTouchOutside(false);
        _progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.URL_HOSPITAL_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        _progressDialog.dismiss();
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            JSONArray jsonArray;
                            int success = jsonObject.getInt("success");
                            if (success == 1){
                                jsonArray = jsonObject.getJSONArray("myscheduleList");
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject temp = jsonArray.getJSONObject(i);
                                    MyScheduleDetailsPopulate mySheduleDetailsPopulate = new MyScheduleDetailsPopulate();
                                    mySheduleDetailsPopulate.AffNo = temp.getString("AffNo");
                                    mySheduleDetailsPopulate.hospotalName = temp.getString("HospitalName");
                                    mySheduleDetailsPopulate.purpose = temp.getString("PurposeName");
                                    mySheduleDetailsPopulate.scheduleType = temp.getString("ScheduleType");
                                    mySheduleDetailsPopulate.status = temp.getString("Status");
                                    mySheduleDetailsPopulate.myScheduleID = temp.getString("myScheduleID");
                                    _adapter.add(mySheduleDetailsPopulate);
                                }
                            }else if (success == 2){
                                String message = "No Details Found";
                                Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
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
                        _progressDialog.hide();
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Check Internet Connection...! & Try Again", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap<>();
                params.put("UserName", _username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.txtSearch).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                _adapter.filter(text);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //menu items
        if (id == R.id.action_refresh){
            try {
                _adapter.Clear();
                LoadMyScheduleList();
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
                        Intent intent = new Intent(ScheduleActivity.this, LoginActivity.class);
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
                Intent intent = new Intent(ScheduleActivity.this, AboutActivity.class);
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
}
