package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
    private Button btnLogIn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.usernameEdittext);
        editPassword = findViewById(R.id.passwordEdittext);
        btnLogIn = findViewById(R.id.loginButton);

        progressDialog = new ProgressDialog(this);

        TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
        final String mPhoneNumber = tMgr.getDeviceId();
//        Toast.makeText(getApplicationContext(),mPhoneNumber,Toast.LENGTH_LONG).show();

//        //If Username is already saved
//        _loginCrdentials = new TinyDB(LoginActivity.this);
//        String receivedUsename = null;
//        String receivedPassword = null;
//        try {
//            /*get username and password saved in shared preferences*/
//            receivedUsename = _loginCrdentials.getString("username");
//            receivedPassword = _loginCrdentials.getString("userpassword");
//        } catch (Exception e) {
//        }
//        if ((receivedUsename != null && !receivedUsename.equals("")) && (receivedPassword != null && !receivedPassword.equals(""))) {
//            userName.setText(receivedUsename);
//            userPassword.setText(receivedPassword);
////            LoginNow(receivedUsename, receivedPassword);
//        }

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

        progressDialog.setMessage("Logging Into GJ Multiclave....");
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
                                Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                                startActivity(intent);
                            }else if (success == 1){
                                Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                                startActivity(intent);
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
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
                Map<String,String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", passWord);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
