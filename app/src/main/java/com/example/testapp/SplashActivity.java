package com.example.testapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ActionBar actionBar;
    private Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            actionBar = getSupportActionBar();
            actionBar.hide();
        }catch (Exception e){
            e.printStackTrace();
        }

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location", "Changed");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Location", "Status Changed");
            }

            @Override
            public void onProviderEnabled(String provider) {
                navigateNextPage();
            }

            @Override
            public void onProviderDisabled(String provider) {
                intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, INTERNET, READ_PHONE_NUMBERS, READ_PHONE_STATE
                }, 100);
                return;
            } else {
                navigateNextPage();
            }
        }
        else {
            navigateNextPage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateNextPage();
                else {
                    Toast.makeText(getApplicationContext(), "Please Accept The Permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }

    private void navigateNextPage() {
        /* Splash Screen And Navigate Next Page */
//        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
                intent=new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }
}
