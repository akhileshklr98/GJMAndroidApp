package com.example.testapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    public Button btnLogIn;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private TelephonyManager tMgr;
    private ActionBar actionBar;
    public WmsDB _loginCrdentials;
    private String imeiNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.usernameEdittext);
        editPassword = findViewById(R.id.passwordEdittext);
        btnLogIn = findViewById(R.id.loginButton);

        progressDialog = new ProgressDialog(this);
        alertDialogBuilder = new AlertDialog.Builder(this);

        try {
            actionBar = getSupportActionBar();
            actionBar.hide();
//            actionBar.setTitle("GJ Multiclaves");
//            actionBar.setDisplayShowTitleEnabled(true);
//            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ff06972c"));
//            actionBar.setBackgroundDrawable(colorDrawable);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        /* Get Imei Number */
        tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_PHONE_STATE
                }, 10);
                return;
            }
        }
        imeiNumber = tMgr.getDeviceId();
//        Toast.makeText(getApplicationContext(),imeiNumber,Toast.LENGTH_LONG).show();

        /* If Username is already saved */
        _loginCrdentials = new WmsDB(LoginActivity.this);
        String receivedUsename = null;
        String receivedPassword = null;
        try {
            /*get username and password saved in shared preferences*/
            receivedUsename = _loginCrdentials.getString("username");
            receivedPassword = _loginCrdentials.getString("userpassword");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((receivedUsename != null && !receivedUsename.equals("")) && (receivedPassword != null && !receivedPassword.equals(""))) {
            editUsername.setText(receivedUsename);
            editPassword.setText(receivedPassword);
        }

        /* Login Button Click */
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((editUsername != null && !editUsername.getText().toString().trim().equals("")) &&
                        (editPassword != null && !editPassword.getText().toString().trim().equals(""))){
                    String username = editUsername.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();

                    LoginNow(username,password);
                }else {
                    Toast.makeText(LoginActivity.this, "Username or Password Cannot Be Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void LoginNow(final String userName, final String passWord){
        progressDialog.setMessage("Logging Into GJ Multiclave...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("Success");
                            if(success == 2){
                                _loginCrdentials.putString("username", userName);
                                _loginCrdentials.putString("userpassword", passWord);
                                try {
                                    Toast.makeText(getApplicationContext(), "Admin", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
//                                    intent.putExtra("username", _loginCrdentials.getString("username"));
//                                    intent.putExtra("password", _loginCrdentials.getString("userpassword"));
//                                    startActivity(intent);
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else if (success == 1){
                                _loginCrdentials.putString("username", userName);
                                _loginCrdentials.putString("userpassword", passWord);
                                try {
                                    Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                                    intent.putExtra("username", _loginCrdentials.getString("username"));
                                    intent.putExtra("password", _loginCrdentials.getString("userpassword"));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                String message = jsonObject.getString("Message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", passWord);
                params.put("IMEINumber", imeiNumber);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed() {
        alertDialogBuilder.setTitle("Exit App");
        alertDialogBuilder.setMessage("Are you sure want to exit ?");
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                LoginActivity.super.onDestroy();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
