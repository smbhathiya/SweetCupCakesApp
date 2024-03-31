package com.example.sweetcupcakes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView no_acc;
    Button login;
    EditText checkEmail;
    EditText checkPassword;
    TextView resetPassword;
    FirebaseAuth mAuth;

    // Declare ProgressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        no_acc = findViewById(R.id.no_acc);
        login = findViewById(R.id.login_btn);
        checkEmail = findViewById(R.id.check_email);
        checkPassword = findViewById(R.id.check_password);
        resetPassword = findViewById(R.id.forgot_pw);

        no_acc.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistrationName.class)));
        resetPassword.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PasswordReset.class)));

        login.setOnClickListener(v -> loginUser());

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false); // Prevent dismissing on touch outside
    }

    private void loginUser() {
        progressDialog.show(); // Show ProgressDialog

        String email = checkEmail.getText().toString().trim();
        String password = checkPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            progressDialog.dismiss(); // Dismiss ProgressDialog if input fields are empty
            Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss(); // Dismiss ProgressDialog after authentication

                    if (task.isSuccessful()) {
                        // Save user's email to SharedPreferences
                        saveUserEmail(email);

                        // Save login state
                        saveLoginState();

                        // Fetch user data and save to SharedPreferences
                        fetchAndSaveUserData();

                        // Redirect to appropriate activity based on email
                        if (email.equals("admin@email.com")) {
                            // Redirect user to Admin activity
                            Intent intent = new Intent(MainActivity.this, AdminMenu.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show validation popup for other users
                            showValidationPopup(true);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        showValidationPopup(false);
                    }
                });
    }


    private void showValidationPopup(boolean success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);

        // Set title and message
        if (success) {
            builder.setTitle("Success");
            builder.setMessage("Login successful!");
        } else {
            builder.setTitle("Error");
            builder.setMessage("Incorrect Email or Password");
        }

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Dismiss the dialog after 2 seconds
        new Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                if (success) {
                    // Redirect user to Home activity
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    // Method to save user's email to SharedPreferences
    public void saveUserEmail(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_email", email);
        editor.apply();
    }

    private void saveLoginState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void fetchAndSaveUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(currentUser.getEmail()).replace(".", ","));
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check if the user exists in the database
                    if (snapshot.exists()) {
                        // Retrieve user's information
                        String firstName = snapshot.child("firstName").getValue(String.class);
                        String lastName = snapshot.child("lastName").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String contactNo = snapshot.child("contactNo").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);

                        // Save user's information to SharedPreferences
                        saveUserInfoToSharedPreferences(firstName, lastName, email, contactNo, address);
                    } else {
                        Toast.makeText(MainActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveUserInfoToSharedPreferences(String firstName, String lastName, String email, String contactNo, String address) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putString("contactNo", contactNo);
        editor.putString("address", address);
        editor.apply();
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
