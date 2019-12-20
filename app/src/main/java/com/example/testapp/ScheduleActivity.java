package com.example.testapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class ScheduleActivity extends AppCompatActivity {

    private MyScheduleDetailListAdapter _adapter;
    private String _username;
    private String _password;
    boolean GpsStatus ;

    private ActionBar actionBar;
    private Intent intent;
    private ProgressDialog _progressDialog;
    Context context;
    protected android.content.Context Context;

    private ListView scheduleDetailsLstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        try {
            actionBar = getSupportActionBar();
            actionBar.setTitle("Scheduled Hospitals");
            actionBar.setDisplayShowTitleEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ff06972c"));
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
                if(myScheduleDetailsPopulate!=null && myScheduleDetailsPopulate.myScheduleID!="")
                {
                    Intent intent = new Intent(ScheduleActivity.this, WorkReport.class);
                    intent.putExtra("MyScheduleID",myScheduleDetailsPopulate.myScheduleID);
//                        intent.putExtra("ScheduleID",_scheduleId);
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
                    _adapter.Clear();
                    LoadMyScheduleList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.action_logout:
                try {
                    Intent intent = new Intent(ScheduleActivity.this, LoginActivity.class);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    startActivity(intent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.action_back:
                try {
                    Intent intent = new Intent(ScheduleActivity.this, UserActivity.class);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case R.id.home:
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        return true;
    }
}
