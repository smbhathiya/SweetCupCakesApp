package com.example.sweetcupcakes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(() -> {
            // Check login state and admin status
            if (isLoggedIn()) {
                if (isAdmin()) {
                    // If the user is an admin, redirect to AdminMenu
                    startActivity(new Intent(SplashActivity.this, AdminMenu.class));
                } else {
                    // If the user is not an admin, redirect to HomeActivity
                    startActivity(new Intent(SplashActivity.this, Home.class));
                }
            } else {
                // If user is not logged in, redirect to MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }, 2000);
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getBoolean("isLoggedIn", false);
    }

    private boolean isAdmin() {
        // Get the user's email from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userEmail = preferences.getString("user_email", "");

        // Define the admin email
        String adminEmail = "admin@email.com";

        // Check if the user's email matches the admin email
        return userEmail.equals(adminEmail);
    }
}
