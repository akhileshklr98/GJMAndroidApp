package com.example.testapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VisitedActivity extends AppCompatActivity {
    private String _myScheduleId, _username, _password, _purpose1, _purpose2, _affNo;
    String checkBoxString, dateUP, trainingUP, remarkDia, remarkTraining, StartTime, EndTime, NoOfAttendance, Sessions, spinnerAssistance, selectedRadioValueF,
            CommonRemarks, VerificationRemarks, token, selectedRadioValueA, selectedRadioValueB, selectedRadioValueC, selectedRadioValueD, selectedRadioValueE,
            selectedRadioValueG, SegregationRemarks, formattedDate;

    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private SimpleDateFormat df;

    EditText txtFollowDate, trainingDate, txtToken, txtRemarkTraining, txtVerificationRemarks, txtCommonRemarks,
            txtStartTime, txtEndTime, txtNoOFAttendance, txtSessions, txtSegregationRemarks;
    RadioButton a1, a2, b1, b2, c1, c2, d1, d2, e1, e2, f1, f2, g1, g2;
    CheckBox checkTraining, checkReTraining, checkVerification, checkSegregation, checkBForm, checkOfficeWork, checkInChargeDuty, checkQRCodeSupply, checkDistRepMeeting,
            checkMarketing, checkPaymentFollowup, checkMedicineCollection, checkSupply, checkSupervisor, checkMeeting, checkTrainingAssistant, checkReVerification, checkOthers;
    Spinner spinner;
//    CheckBox checkAffiliation;

    final Calendar c = Calendar.getInstance();

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);

        df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());

        actionBar = getSupportActionBar();
        progressDialog = new ProgressDialog(VisitedActivity.this);
        alertDialogBuilder = new AlertDialog.Builder(VisitedActivity.this);

        try {
            actionBar.setTitle("Visited Reason");
            actionBar.setDisplayShowTitleEnabled(true);
            ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#ff06972c"));
            actionBar.setBackgroundDrawable(colorDrawable);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Intent intent = getIntent();
            this._myScheduleId = intent.getStringExtra("MyScheduleID");
            this._username = intent.getStringExtra("username");
            this._password = intent.getStringExtra("password");
            _purpose1 = intent.getStringExtra("purpose1");
            _purpose2 = intent.getStringExtra("purpose2");
            _affNo = intent.getStringExtra("affNo");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        checkAffiliation = findViewById( R.id.checkAffiliation );
        checkTraining = findViewById(R.id.checkTraining);
        checkReTraining = findViewById(R.id.checkRetraining);
        checkVerification = findViewById(R.id.checkVerification);
        checkReVerification = findViewById(R.id.checkReVerification);
        checkSegregation = findViewById(R.id.checkSegregation);
        checkBForm = findViewById(R.id.checkBForm);
        checkOfficeWork = findViewById(R.id.checkOfficeWork);
        checkMarketing = findViewById(R.id.checkMarketing);
        checkPaymentFollowup = findViewById(R.id.checkPaymentFollowup);
        checkMedicineCollection = findViewById(R.id.checkMedicineCollection);
        checkSupply = findViewById(R.id.checkSupply);
        checkSupervisor = findViewById(R.id.checkSupervisor);
        checkMeeting = findViewById(R.id.checkMeeting);
        checkTrainingAssistant = findViewById(R.id.checkTrainingAssistant);
        checkInChargeDuty = findViewById(R.id.checkInchargeDuty);
        checkQRCodeSupply = findViewById(R.id.checkQRCodeSupply);
        checkDistRepMeeting = findViewById(R.id.checkDistRepMeeting);
        checkOthers = findViewById(R.id.checkOthers);

//        if (Integer.parseInt(_purpose1) == 7 || Integer.parseInt(_purpose2) == 7){
//            checkVerification.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 18 || Integer.parseInt(_purpose2) == 18){
//            checkReVerification.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 2 || Integer.parseInt(_purpose2) == 2){
//            checkTraining.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 3 || Integer.parseInt(_purpose2) == 3){
//            checkReTraining.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 4 || Integer.parseInt(_purpose2) == 4){
//            checkSegregation.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 5 || Integer.parseInt(_purpose2) == 5){
//            checkMedicineCollection.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 6 || Integer.parseInt(_purpose2) == 6){
//            checkBForm.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 8 || Integer.parseInt(_purpose2) == 8){
//            checkPaymentFollowup.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 10 || Integer.parseInt(_purpose2) == 10){
//            checkOfficeWork.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 11 || Integer.parseInt(_purpose2) == 11){
//            checkMarketing.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 12 || Integer.parseInt(_purpose2) == 12){
//            checkSupply.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 13 || Integer.parseInt(_purpose2) == 13){
//            checkSupervisor.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 14 || Integer.parseInt(_purpose2) == 14){
//            checkMeeting.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 15 || Integer.parseInt(_purpose2) == 15){
//            checkTrainingAssistant.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 16 || Integer.parseInt(_purpose2) == 16){
//            checkDistRepMeeting.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 17 || Integer.parseInt(_purpose2) == 17){
//            checkQRCodeSupply.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 19 || Integer.parseInt(_purpose2) == 19){
//            checkInChargeDuty.setVisibility(View.VISIBLE);
//        }
//        if (Integer.parseInt(_purpose1) == 9 || Integer.parseInt(_purpose2) == 9){
//            checkOthers.setVisibility(View.VISIBLE);
//        }

//        checkAffiliation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    initiatePopupWindow(buttonView);
//                }
//            }
//        });

        checkTraining.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkTraining";
                    trainingPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkReTraining.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBoxString="checkRetraining";
                    trainingPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkVerification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkVerification";
                    verificationPopupWindow(buttonView, checkBoxString, _affNo);
                }
            }
        });

        checkReVerification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkReVerification";
                    verificationPopupWindow(buttonView, checkBoxString, _affNo);
                }
            }
        });

        checkSegregation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    segregationPopupWindow(buttonView);
                }
            }
        });

        checkBForm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkBForm";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkOfficeWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkOfficeWork";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkMarketing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkMarketing";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkPaymentFollowup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkPaymentFollowup";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkMedicineCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkMedicineCollection";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkSupply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkSupply";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkSupervisor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkSupervisor";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkMeeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkMeeting";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkTrainingAssistant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkTrainingAssistant";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkInChargeDuty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkInChargeDuty";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkQRCodeSupply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkQRCodeSupply";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkDistRepMeeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkDistRepMeeting";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkOthers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxString="checkOthers";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh:
                try {
                    Intent intent = new Intent(VisitedActivity.this, VisitedActivity.class);
                    intent.putExtra("MyScheduleID",_myScheduleId);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    startActivity(intent);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.action_logout:
                try {
                    Intent intent = new Intent(VisitedActivity.this, LoginActivity.class);
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
                    Intent intent = new Intent(VisitedActivity.this, WorkReport.class);
                    intent.putExtra("username",_username);
                    intent.putExtra("password",_password);
                    startActivity(intent);

                } catch (Exception e) {
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

    private void verificationPopupWindow(CompoundButton buttonView, final String checkBoxString, final String AffNo) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(VisitedActivity.this);
            View promptView = layoutInflater.inflate(R.layout.verification_layout, null);

            alertDialogBuilder.setView(promptView);
            alertDialogBuilder.setTitle("Verification Details");

            txtVerificationRemarks = promptView.findViewById(R.id.txtVerificationRemarks);
            txtToken = promptView.findViewById(R.id.txttoken);
            txtToken.setText(AffNo);

            alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    VerificationRemarks = txtVerificationRemarks.getText().toString();
                    token = txtToken.getText().toString();
                    SaveVerificationReason(checkBoxString);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    if (checkBoxString.equals("checkVerification")){
                        checkVerification.toggle();
                    }
                    if (checkBoxString.equals("checkReVerification")){
                        checkReVerification.toggle();
                    }
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SaveVerificationReason(final String checkBoxString) {
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
                            if (response != null && !response.equals("")){
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    String message = "Reason Saved Successfully";
                                    finish();
                                    if (success == 1){
                                        Intent intent = new Intent(VisitedActivity.this, ScheduleActivity.class);
                                        intent.putExtra("MyScheduleID",_myScheduleId);
                                        intent.putExtra("username",_username);
                                        intent.putExtra("password",_password);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
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
                    params.put("MyScheduleID", _myScheduleId);
                    params.put("status", "Visited");
                    params.put("customerRemarks", VerificationRemarks);
                    params.put("TokenNo", token);
                    params.put("UserName", _username);
                    if(checkBoxString.equals("checkVerification")){
                        params.put("checkVerification", "7");
                    }
                    if(checkBoxString.equals("checkReVerification")){
                        params.put("checkReVerification", "18");
                    }
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void segregationPopupWindow(CompoundButton buttonView) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(VisitedActivity.this);
            View promptView = layoutInflater.inflate(R.layout.segregation_layout, null);

            alertDialogBuilder.setView(promptView);
            alertDialogBuilder.setTitle("Segregation Details");

            txtSegregationRemarks = promptView.findViewById(R.id.txtSegregationRemarks);

            a1 = promptView.findViewById(R.id.chkQ1Y);
            a2 = promptView.findViewById(R.id.chkQ1N);

            b1 = promptView.findViewById(R.id.chkQ2Y);
            b2 = promptView.findViewById(R.id.chkQ2N);

            c1 = promptView.findViewById(R.id.chkQ3Y);
            c2 = promptView.findViewById(R.id.chkQ3N);

            d1 = promptView.findViewById(R.id.chkQ4Y);
            d2 = promptView.findViewById(R.id.chkQ4N);

            e1 = promptView.findViewById(R.id.chkQ5Y);
            e2 = promptView.findViewById(R.id.chkQ5N);

            f1 = promptView.findViewById(R.id.chkQ6Y);
            f2 = promptView.findViewById(R.id.chkQ6N);

            g1 = promptView.findViewById(R.id.chkQ7Y);
            g2 = promptView.findViewById(R.id.chkQ7N);

            alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if ((a1.isChecked() || a2.isChecked()) && (b1.isChecked() || b2.isChecked()) && (c1.isChecked() || c2.isChecked()) && (f1.isChecked() || f2.isChecked()) &&
                            (d1.isChecked() || d2.isChecked()) && (e1.isChecked() || e2.isChecked()) && (g1.isChecked() || g2.isChecked())){
                        if (a1.isChecked() || a2.isChecked()) {
                            if (a1.isChecked()) {
                                selectedRadioValueA = "0";
                            } else if (a2.isChecked()) {
                                selectedRadioValueA = "1";
                            }
                        }

                        if (b1.isChecked() || b2.isChecked()) {
                            if (b1.isChecked()) {
                                selectedRadioValueB = "0";
                            } else if (b2.isChecked()) {
                                selectedRadioValueB = "1";
                            }
                        }

                        if (c1.isChecked() || c2.isChecked()) {
                            if (c1.isChecked()) {
                                selectedRadioValueC = "0";
                            } else if (c2.isChecked()) {
                                selectedRadioValueC = "1";
                            }
                        }

                        if (d1.isChecked() || d2.isChecked()) {
                            if (d1.isChecked()) {
                                selectedRadioValueD = "0";
                            } else if (d2.isChecked()) {
                                selectedRadioValueD = "1";
                            }
                        }

                        if (e1.isChecked() || e2.isChecked()) {
                            if (e1.isChecked()) {
                                selectedRadioValueE = "0";
                            } else if (e2.isChecked()) {
                                selectedRadioValueE = "1";
                            }
                        }

                        if (f1.isChecked() || f2.isChecked()) {
                            if (f1.isChecked()) {
                                selectedRadioValueF = "0";
                            } else if (f2.isChecked()) {
                                selectedRadioValueF = "1";
                            }
                        }

                        if (g1.isChecked() || g2.isChecked()) {
                            if (g1.isChecked()) {
                                selectedRadioValueG = "0";
                            } else if (g2.isChecked()) {
                                selectedRadioValueG = "1";
                            }
                        }

                        SegregationRemarks = txtSegregationRemarks.getText().toString();

                        checkSegregation.setEnabled(false);
                        SaveSegregationReason();
                    }else {
                        Toast.makeText(getApplicationContext(), "Fill All Details", Toast.LENGTH_SHORT).show();
                        checkSegregation.toggle();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    checkSegregation.toggle();
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SaveSegregationReason() {
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
                            if (response != null && !response.equals("")){
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    String message = "Reason Saved Successfully";
                                    finish();
                                    if (success == 1){
                                        Intent intent = new Intent(VisitedActivity.this, ScheduleActivity.class);
                                        intent.putExtra("MyScheduleID",_myScheduleId);
                                        intent.putExtra("username",_username);
                                        intent.putExtra("password",_password);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
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
                            Toast.makeText(getApplicationContext(),"Failed To Save Data! Network Error", Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("MyScheduleID", _myScheduleId);
                    params.put("status", "Visited");
                    params.put("customerRemarks", SegregationRemarks);
                    params.put("UserName", _username);
                    params.put("checkSegregation", "4");
                    params.put("chkQ1", selectedRadioValueA);
                    params.put("chkQ2", selectedRadioValueB);
                    params.put("chkQ3", selectedRadioValueC);
                    params.put("chkQ4", selectedRadioValueD);
                    params.put("chkQ5", selectedRadioValueE);
                    params.put("chkQ6", selectedRadioValueF);
                    params.put("chkQ7", selectedRadioValueG);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void commonPopupWindow(CompoundButton buttonView, final String checkBoxString) {
        LayoutInflater layoutInflater = LayoutInflater.from(VisitedActivity.this);
        View promptView = layoutInflater.inflate(R.layout.commonreason_layout, null);

        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("Reason");

        txtCommonRemarks = promptView.findViewById(R.id.txtCommonRemarks);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonRemarks = txtCommonRemarks.getText().toString();
                if (checkBoxString.equals("checkBForm")){
                    checkBForm.setEnabled(false);
                    SaveCommonReason(checkBoxString);
                }else {
                    if (checkBoxString.equals("checkOfficeWork")){
                        checkOfficeWork.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkMarketing")){
                        checkMarketing.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkPaymentFollowup")){
                        checkPaymentFollowup.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkMedicineCollection")){
                        checkMedicineCollection.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkSupply")){
                        checkSupply.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkSupervisor")){
                        checkSupervisor.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkMeeting")){
                        checkMeeting.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkTrainingAssistant")){
                        checkTrainingAssistant.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkInChargeDuty")){
                        checkInChargeDuty.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkQRCodeSupply")){
                        checkQRCodeSupply.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkDistRepMeeting")){
                        checkDistRepMeeting.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                    if (checkBoxString.equals("checkOthers")){
                        checkOthers.setEnabled(false);
                        SaveCommonReason(checkBoxString);
                        return;
                    }
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (checkBoxString.equals("checkBForm")){
                    checkBForm.toggle();
                }else {
                    if (checkBoxString.equals("checkOfficeWork")){
                        checkOfficeWork.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkMarketing")){
                        checkMarketing.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkPaymentFollowup")){
                        checkPaymentFollowup.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkMedicineCollection")){
                        checkMedicineCollection.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkSupply")){
                        checkSupply.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkSupervisor")){
                        checkSupervisor.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkMeeting")){
                        checkMeeting.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkTrainingAssistant")){
                        checkTrainingAssistant.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkInChargeDuty")){
                        checkInChargeDuty.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkQRCodeSupply")){
                        checkQRCodeSupply.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkDistRepMeeting")){
                        checkDistRepMeeting.toggle();
                        return;
                    }
                    if (checkBoxString.equals("checkOthers")){
                        checkOthers.toggle();
                        return;
                    }
                }
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void SaveCommonReason(final String checkBoxString) {
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
                            if (response != null && !response.equals("")){
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int success = jsonObject.getInt("success");
                                    String message = "Reason Saved Successfully";
                                    finish();
                                    if (success == 1){
                                        Intent intent = new Intent(VisitedActivity.this, ScheduleActivity.class);
                                        intent.putExtra("MyScheduleID",_myScheduleId);
                                        intent.putExtra("username",_username);
                                        intent.putExtra("password",_password);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
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
                    params.put("MyScheduleID", _myScheduleId);
                    params.put("status", "Visited");
                    params.put("customerRemarks", CommonRemarks);
                    params.put("UserName", _username);
                    if(checkBoxString.equals("checkBForm")){
                        params.put("checkBForm", "6");
                    }
                    if(checkBoxString.equals("checkOfficeWork")){
                        params.put("checkOfficeWork", "10");
                    }
                    if(checkBoxString.equals("checkMarketing")){
                        params.put("checkMarketing", "11");
                    }
                    if(checkBoxString.equals("checkPaymentFollowup")){
                        params.put("checkPaymentFollowup", "8");
                    }
                    if(checkBoxString.equals("checkMedicineCollection")){
                        params.put("checkMedicineCollection", "5");
                    }
                    if(checkBoxString.equals("checkSupply")){
                        params.put("checkSupply", "12");
                    }
                    if(checkBoxString.equals("checkSupervisor")){
                        params.put("checkSupervisor", "13");
                    }

                    if(checkBoxString.equals("checkMeeting")){
                        params.put("checkMeeting", "14");
                    }
                    if(checkBoxString.equals("checkTrainingAssistant")){
                        params.put("checkTrainingAssistant", "15");
                    }
                    if(checkBoxString.equals("checkInChargeDuty")){
                        params.put("checkInchargeDuty", "19");
                    }
                    if(checkBoxString.equals("checkQRCodeSupply")){
                        params.put("checkQRCodeSupply", "17");
                    }
                    if(checkBoxString.equals("checkDistRepMeeting")){
                        params.put("checkDistRepMeeting", "16");
                    }
                    if(checkBoxString.equals("checkOthers")){
                        params.put("checkOthers", "9");
                    }
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void trainingPopupWindow(CompoundButton buttonView, final String checkBoxString) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(VisitedActivity.this);
            View promptView = layoutInflater.inflate(R.layout.training_layout, null);
            alertDialogBuilder.setView(promptView);
            alertDialogBuilder.setTitle("Training Details");

            spinner = promptView.findViewById(R.id.spinnerAssistance);
            txtRemarkTraining = promptView.findViewById(R.id.RemarkTraining);
            txtStartTime = promptView.findViewById(R.id.StartTime);
            txtEndTime = promptView.findViewById(R.id.EndTime);
            txtNoOFAttendance = promptView.findViewById(R.id.NoofAttendee);
            txtSessions = promptView.findViewById(R.id.Sessions);

            try {
                LoadAssistanceList();
            } catch (Exception e) {
                e.printStackTrace();
            }

            arrayList = new ArrayList<>();
            arrayList.add("Select Assistance ");
            arrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, this.arrayList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(this.arrayAdapter);

            txtFollowDate = promptView.findViewById(R.id.txtfollowdate);
            dateUP = formattedDate;
            txtFollowDate.setText(dateUP);

            /* create an Training dialog */
            trainingDate = promptView.findViewById(R.id.trainingdate);
            trainingUP = formattedDate;
            trainingDate.setText(trainingUP);
            // Show a datePicker when the dateButton is clicked
//            trainingDate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Calendar now = Calendar.getInstance();
//                    final Calendar c = Calendar.getInstance();
//                    DatePickerDialog dpd = new DatePickerDialog(VisitedActivity.this, new DatePickerDialog.OnDateSetListener() {
//                                @Override
//                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                    trainingDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                    trainingUP = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//                                }
//                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
//                    dpd.show();
//                }
//            });

            /* Show a timePicker when the start time edit text is clicked */
            txtStartTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(VisitedActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    txtStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                }
                            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                    timePickerDialog.show();
                }
            });

            /* Show a timePicker when the end time edit text is clicked */
            txtEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(VisitedActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    txtEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                }
                            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                    timePickerDialog.show();
                }
            });

            alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    remarkTraining = txtRemarkTraining.getText().toString();
                    StartTime = txtStartTime.getText().toString();
                    EndTime = txtEndTime.getText().toString();
                    NoOfAttendance = txtNoOFAttendance.getText().toString();
                    Sessions = txtSessions.getText().toString();
                    spinnerAssistance = spinner.getSelectedItem().toString();

                    if (NoOfAttendance !=null && !NoOfAttendance.trim().equals("") && trainingUP !=null && StartTime !=null && !StartTime.trim().equals("") && EndTime !=null && !EndTime.trim().equals("") && Sessions !=null && !Sessions.trim().equals("")){
                        if (checkBoxString.equals("checkTraining")){
                            checkTraining.setEnabled(false);
                            SaveTrainingReason(checkBoxString);
                        }else if (checkBoxString.equals("checkRetraining")){
                            checkReTraining.setEnabled(false);
                            SaveTrainingReason(checkBoxString);
                            return;
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Fill All Details", Toast.LENGTH_SHORT).show();
                        if (checkBoxString.equals("checkTraining")){
                            checkTraining.toggle();
                            return;
                        }
                        if (checkBoxString.equals("checkRetraining")){
                            checkReTraining.toggle();
                            return;
                        }
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    if (checkBoxString.equals("checkTraining")){
                        checkTraining.toggle();
                    }else if (checkBoxString.equals("checkRetraining")){
                        checkReTraining.toggle();
                        return;
                    }
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SaveTrainingReason(final String checkBoxString) {
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
                        if (response != null && !response.equals("")){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                finish();
                                if (success == 1){
                                    Intent intent = new Intent(VisitedActivity.this, ScheduleActivity.class);
                                    intent.putExtra("MyScheduleID",_myScheduleId);
                                    intent.putExtra("username",_username);
                                    intent.putExtra("password",_password);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Reason Saved Successfully",Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
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
                params.put("MyScheduleID", _myScheduleId);
                params.put("status", "Visited");
                params.put("UserName", _username);
                params.put("FollowDate",dateUP);
                params.put("txtTrainingDate",trainingUP);
                params.put("txtStartTime", StartTime);
                params.put("txtFinishTime", EndTime);
                params.put("txtSessions", NoOfAttendance);
                params.put("txtAttendee", Sessions);
                params.put("txtAssistance", spinnerAssistance);
                params.put("txtTrainingRemark", remarkTraining);
                if(checkBoxString.equals("checkTraining")){
                    params.put("checkTraining", "2");
                }
                if(checkBoxString.equals("checkRetraining")){
                    params.put("checkRetraining", "3");
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void LoadAssistanceList() {
        try {
            if (arrayList != null && arrayList.size() > 0) {
                int count = arrayList.size();
                for (int i = 1; i <count;  i++) {
                    arrayList.remove(count-i);
                }
                spinner.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
            progressDialog.setMessage("Loading ...");
            progressDialog.dismiss();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constant.URL_GET_ASSISTANCE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                PopulateAssistanceName(response);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Failed To Load Data! Network Error", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void PopulateAssistanceName(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray;
            int success = jsonObject.getInt("success");
            if (success == 1){
                jsonArray = jsonObject.getJSONArray("AssistanceList");
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject temp = jsonArray.getJSONObject(i);
                    String employeeName = temp.getString("Assistance");
                    arrayList.add(employeeName);
                }
            }else {
                String message = jsonObject.getString("message");
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error "+e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

//    private void initiatePopupWindow(CompoundButton buttonView) {
//        try {
//            LayoutInflater layoutInflater = LayoutInflater.from(VisitedActivity.this);
//            View promptView = layoutInflater.inflate(R.layout.affiliation_layout, null);
//            alertDialogBuilder.setView(promptView);
//            alertDialogBuilder.setTitle("Affiliation Details");
//
//            txtFollowDate = promptView.findViewById(R.id.txtfollowdate);
//            txtRemarks = promptView.findViewById(R.id.txtRemarks);
//
//            txtFollowDate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DatePickerDialog dpd = new DatePickerDialog(VisitedActivity.this,
//                            new DatePickerDialog.OnDateSetListener() {
//                                @Override
//                                public void onDateSet(DatePicker view, int year,
//                                                      int monthOfYear, int dayOfMonth) {
//                                    txtFollowDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                    dateUP = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//
//                                }
//                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
//                    dpd.show();
//                }
//            });
//
//            alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    remarkDia = txtRemarks.getText().toString();
//                    checkBoxString="checkAffiliation";
//                    if (dateUP !=null ){
//                        checkAffiliation.setEnabled(false);
//                        SaveReason(checkBoxString);
//                    }else {
//                        Toast.makeText(getApplicationContext(), "Fill All Details", Toast.LENGTH_SHORT).show();
//                        checkAffiliation.toggle();
//                    }
//                }
//            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                    checkAffiliation.toggle();
//                }
//            });
//            alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    private void SaveReason(final String checkBoxString) {
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
                                progressDialog.dismiss();
                                JSONObject jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = "Reason Saved Successfully";
                                finish();
                                if (success == 1){
                                    Intent intent = new Intent(VisitedActivity.this, ScheduleActivity.class);
                                    intent.putExtra("MyScheduleID",_myScheduleId);
                                    intent.putExtra("username",_username);
                                    intent.putExtra("password",_password);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
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
                    params.put("MyScheduleID", _myScheduleId);
                    params.put("status", "Visited");
                    params.put("customerRemarks", remarkDia);
                    params.put("FollowDate",dateUP);
                    params.put("UserName", _username);
                    if(checkBoxString.equals("checkAffiliation")){
                        params.put("checkAffiliation", "1");
                    }
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        alertDialogBuilder.setTitle("Confirm Submit...");
        alertDialogBuilder.setMessage("This Schedule will not be further available..");
        alertDialogBuilder.setCancelable(false).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(VisitedActivity.this, ScheduleActivity.class);
                intent.putExtra("MyScheduleID", _myScheduleId);
                intent.putExtra("username", _username);
                intent.putExtra("password", _password);
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

