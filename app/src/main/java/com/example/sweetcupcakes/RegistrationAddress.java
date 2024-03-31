package com.example.sweetcupcakes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationAddress extends AppCompatActivity {
    Button signup;
    EditText address;
    TextView have_acc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_address);

        signup = findViewById(R.id.sign_up_btn);
        address = findViewById(R.id.get_address);
        have_acc = findViewById(R.id.have_acc);

        signup.setOnClickListener(v -> createAccount());
        have_acc.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationAddress.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void createAccount() {
        String userAddress = address.getText().toString().trim();

        if (TextUtils.isEmpty(userAddress)) {
            Toast.makeText(this, "Address is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading animation
        showLoadingAnimation();

        // Process account creation
        processAccountCreation(userAddress);
    }

    private void showLoadingAnimation() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void processAccountCreation(String userAddress) {
        // Get user data from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = preferences.getString("email", "");
        String firstName = preferences.getString("firstName", "");
        String lastName = preferences.getString("lastName", "");
        String contactNo = preferences.getString("contactNo", "");
        SharedPreferences passwordPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String password = passwordPreferences.getString("password", "");

        // Authenticate user with Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Account creation successful, add user information to Realtime Database
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        DatabaseReference currentUserRef = usersRef.child(email.replace(".", ","));
                        currentUserRef.child("firstName").setValue(firstName);
                        currentUserRef.child("lastName").setValue(lastName);
                        currentUserRef.child("contactNo").setValue(contactNo);
                        currentUserRef.child("address").setValue(userAddress);

                        // Clear SharedPreferences
                        preferences.edit().clear().apply();
                        passwordPreferences.edit().clear().apply();

                        // Show success message
                        Toast.makeText(RegistrationAddress.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                        // Hide loading animation
                        progressDialog.dismiss();

                        // Redirect user to the main activity
                        Intent intent = new Intent(RegistrationAddress.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Account creation failed, show error message
                        Toast.makeText(RegistrationAddress.this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();

                        // Hide loading animation
                        progressDialog.dismiss();
                    }
                });
    }
}
