package com.example.testapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VisitedActivity extends AppCompatActivity {
    private String _myScheduleId, _username, _password;
    String checkBoxString, dateUP, trainingUP, remarkDia, remarkTraining, StartTime, EndTime, NoOfAttendance, Sessions, spinnerAssistance, selectedRadioValueF,
            CommonRemarks, VerificationRemarks, token, selectedRadioValueA, selectedRadioValueB, selectedRadioValueC, selectedRadioValueD, selectedRadioValueE,
            RemarkQ1, RemarkQ2, RemarkQ3, RemarkQ4, RemarkQ5, RemarkQ6, SegregationRemarks, SpinAsssis;

    private PopupWindow popupWindow;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    EditText txtFollowDate, trainingDate, txtRemarks, txtToken, txtRemarkTraining, txtVerificationRemarks, txtCommonRemarks,
            txtStartTime, txtEndTime, txtNoOFAttendance, txtSessions, txtSpinnerAssistance, txtSegregationRemarks, txtQ1, txtQ2,
            txtQ3, txtQ4, txtQ5, txtQ6;
    RadioButton a1, a2, b1, b2, c1, c2, d1, d2, e1, e2, f1, f2;
    CheckBox checkAffiliation, checkTraining, checkReTraining, checkVerification, checkSegregation, checkBForm, checkOfficeWork, checkInChargeDuty,
            checkMarketing, checkPaymentFollowup, checkMedicineCollection, checkSupply, checkSupervisor, checkMeeting, checkTrainingAssistant, checkReVerification;

    Context context;
    Spinner spinner;

    final Calendar c = Calendar.getInstance();
    private int mYear, mMonth, mDay;

    DatePickerDialog.OnDateSetListener dateListener;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);

        actionBar = getSupportActionBar();
        alertDialogBuilder = new AlertDialog.Builder(VisitedActivity.this);
        alertDialog = alertDialogBuilder.create();

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
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        checkAffiliation = findViewById( R.id.checkAffiliation );
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

        checkAffiliation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Affiliation", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    initiatePopupWindow(buttonView);
                }
            }
        });

        checkTraining.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Training", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkRetraining";
                    trainingPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkReTraining.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Re Training", Toast.LENGTH_SHORT).show();
                if(isChecked) {
                    checkBoxString="checkRetraining";
                    trainingPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkVerification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Verification", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkVerification";
                    verificationPopupWindow(buttonView, checkBoxString);
                }
            }
        });

        checkReVerification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Re Verification", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkReVerification";
                    verificationPopupWindow(buttonView, checkBoxString);
                }
            }
        });

        checkSegregation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Segregation", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    segregationPopupWindow(buttonView);
                }
            }
        });

        checkBForm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "B From", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkBForm";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkOfficeWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Office Work", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkOfficeWork";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkMarketing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Marketing", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkMarketing";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkPaymentFollowup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Payment Follow Up", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkPaymentFollowup";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkMedicineCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Medicine Collection", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkMedicineCollection";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkSupply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Supply", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkSupply";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkSupervisor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Supervisor", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkSupervisor";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkMeeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Meeting", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkMeeting";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkTrainingAssistant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "Training Assistant", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkTrainingAssistant";
                    commonPopupWindow(buttonView,checkBoxString);
                }
            }
        });

        checkInChargeDuty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(getApplicationContext(), "In-Charge Duty", Toast.LENGTH_SHORT).show();
                if (isChecked){
                    checkBoxString="checkInChargeDuty";
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

    private void verificationPopupWindow(CompoundButton buttonView, String checkBoxString) {
    }

    private void segregationPopupWindow(CompoundButton buttonView) {
    }

    private void commonPopupWindow(CompoundButton buttonView, String checkBoxString) {
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

            /* Show a datepicker when the dateButton is clicked */
            txtFollowDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dpd = new DatePickerDialog(VisitedActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    txtFollowDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    dateUP = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                    dpd.show();
                }
            });

            /* create an Training dialog */
            trainingDate = promptView.findViewById(R.id.trainingdate);
            // Show a datePicker when the dateButton is clicked
            trainingDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Calendar now = Calendar.getInstance();
                    final Calendar c = Calendar.getInstance();
                    DatePickerDialog dpd = new DatePickerDialog(VisitedActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override

                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    trainingDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    trainingUP = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                    dpd.show();
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

                    SaveTrainingReason(checkBoxString);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void SaveTrainingReason(String checkBoxString) {
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

    private void initiatePopupWindow(CompoundButton buttonView) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(VisitedActivity.this);
            View promptView = layoutInflater.inflate(R.layout.affiliation_layout, null);

            alertDialogBuilder.setView(promptView);

            txtFollowDate = promptView.findViewById(R.id.txtfollowdate);
            txtRemarks = promptView.findViewById(R.id.txtRemarks);

            txtFollowDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dpd = new DatePickerDialog(VisitedActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    txtFollowDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    dateUP = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                    dpd.show();
                }
            });

            alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    remarkDia = txtRemarks.getText().toString();
                    checkBoxString="checkAffiliation";
                    SaveReason(checkBoxString);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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
                                    Intent intent = new Intent(VisitedActivity.this, WorkReport.class);
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
}