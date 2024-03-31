package com.example.sweetcupcakes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class Cart extends AppCompatActivity {

    Button message;
    Button home;
    Button user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize ImageViews
        message = findViewById(R.id.message_icon_btn);
        home = findViewById(R.id.home_icon_btn);
        user = findViewById(R.id.user_icon_btn);

        // Set click listeners
        message.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Messages.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Home.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        user.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, User.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Redirect user to Home activity
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
