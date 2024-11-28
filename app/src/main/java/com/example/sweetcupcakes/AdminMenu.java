package com.example.sweetcupcakes;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMenu extends AppCompatActivity {
    Button manageCupcakes;
    Button manageOrders;
    Button ordersHistory;
    Button sign_out;
    Button addCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        manageCupcakes = findViewById(R.id.manage_cupcakes);
        manageOrders = findViewById(R.id.manage_orders);
        ordersHistory = findViewById(R.id.orders_history);
        sign_out = findViewById(R.id.logout_button);
        addCategory = findViewById(R.id.add_category);

        // Set click listener for the "Manage Orders" button
        manageOrders.setOnClickListener(v -> {
            // Navigate to the ManageOrders activity
            startActivity(new Intent(AdminMenu.this, ManageOrders.class));
        });
        manageCupcakes.setOnClickListener(v -> {
            // Navigate to the ManageOrders activity
            startActivity(new Intent(AdminMenu.this, ManageCupcakes.class));
        });

        addCategory.setOnClickListener(v -> {
            // Navigate to the ManageOrders activity
            startActivity(new Intent(AdminMenu.this, ManageCategory.class));
        });




        // Set click listener for the "Logout" button
        sign_out.setOnClickListener(v -> showLogoutConfirmationDialog());
    }


    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", (dialog, which) -> {
            // Call logout method
            logout();
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Close App")
                .setMessage("Are you sure you want to close the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Close the app
                    finishAffinity();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
