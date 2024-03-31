package com.example.sweetcupcakes;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class User extends AppCompatActivity {
    Button message;
    Button home;
    Button cart;
    String userEmail;
    String firstName;
    String lastName;
    String fullName;
    LinearLayout callUs;
    LinearLayout messageUs;
    LinearLayout emailUs;
    LinearLayout viewOrders;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user);

            message = findViewById(R.id.message_icon_btn);
            home = findViewById(R.id.home_icon_btn);
            cart = findViewById(R.id.cart_icon_btn);
            callUs = findViewById(R.id.call_us);
            messageUs = findViewById(R.id.message_us);
            emailUs = findViewById(R.id.email_us);
            
            callUs.setOnClickListener(v -> {
                // Create intent to dial phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));

                // Start the activity (phone dialer app)
                startActivity(intent);

                // Close the app
                finish();
            });

            messageUs = findViewById(R.id.message_us);
            messageUs.setOnClickListener(v -> {
                // Create intent to send a message
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:0123456789"));

                // Start the activity (messaging app)
                startActivity(intent);

                // Close the app
                finish();
            });

            emailUs.setOnClickListener(v -> {
                // Create intent to send email
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:sweetCupcakes@email.com"));

                // Set subject
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");

                // Start the email app
                startActivity(intent);
            });



            // Get user's email from intent extras
            userEmail = getIntent().getStringExtra("user_email");
            firstName = getIntent().getStringExtra("first_name");
            lastName = getIntent().getStringExtra("last_name");

            // If email is not passed in the intent, retrieve it from SharedPreferences
            if (userEmail == null || userEmail.isEmpty()) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                userEmail = preferences.getString("user_email", "");
                firstName = preferences.getString("firstName", "");
                lastName = preferences.getString("lastName", "");
            }

            fullName = firstName + " " + lastName;

            // Initialize TextView to display user data
            TextView userEmailTextView = findViewById(R.id.user_email_textview);
            TextView userNameTextView = findViewById(R.id.user_full_name);
            userEmailTextView.setText(userEmail);
            userNameTextView.setText(fullName);

            // Set click listeners
            message.setOnClickListener(v -> {
                Intent intent = new Intent(User.this, Messages.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            });

            home.setOnClickListener(v -> {
                Intent intent = new Intent(User.this, Home.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            });

            cart.setOnClickListener(v -> {
                Intent intent = new Intent(User.this, Cart.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            });



            // Initialize logout button
            Button logoutButton = findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(v -> {
                // Show confirmation dialog before logout
                showLogoutConfirmationDialog();
            });
        }

    // Method to show confirmation dialog before logout
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call logout method
                logout();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Method to handle logout functionality
    private void logout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Redirect user to MainActivity
        Intent intent = new Intent(User.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
