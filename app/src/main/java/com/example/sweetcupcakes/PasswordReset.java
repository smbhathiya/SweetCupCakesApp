package com.example.sweetcupcakes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {

    Button resetPassword;
    EditText emailToResetPassword;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        resetPassword = findViewById(R.id.reset_password_btn);
        emailToResetPassword = findViewById(R.id.email_to_reset_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending reset email...");
        progressDialog.setCancelable(false);

        resetPassword.setOnClickListener(v -> {
            // Get the email entered by the user
            String email = emailToResetPassword.getText().toString().trim();

            // Check if email is empty
            if (TextUtils.isEmpty(email)) {
                emailToResetPassword.setError("Enter your email");
                return;
            }

            // Show progress dialog
            progressDialog.show();

            // Send password reset email
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        // Dismiss progress dialog
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Password reset email sent successfully
                            showResetSuccessDialog();
                        } else {
                            // Failed to send password reset email
                            Toast.makeText(PasswordReset.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // Method to show a dialog indicating successful password reset email sent
    private void showResetSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Password reset email sent. Please check your email.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Redirect user to MainActivity
                    Intent intent = new Intent(PasswordReset.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finish the PasswordReset activity to prevent going back to it when pressing back button from MainActivity
                })
                .show();
    }
}
