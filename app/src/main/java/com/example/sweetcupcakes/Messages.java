package com.example.sweetcupcakes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Messages extends AppCompatActivity {
    Button cart;
    Button home;
    Button user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Initialize ImageViews
        cart = findViewById(R.id.cart_icon_btn);
        home = findViewById(R.id.home_icon_btn);
        user = findViewById(R.id.user_icon_btn);

        // Set click listeners
        cart.setOnClickListener(v -> {
            Intent intent = new Intent(Messages.this, Cart.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Messages.this, Home.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        user.setOnClickListener(v -> {
            Intent intent = new Intent(Messages.this, User.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
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
