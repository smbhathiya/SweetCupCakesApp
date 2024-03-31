package com.example.sweetcupcakes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationPassword extends AppCompatActivity {
    TextView have_acc;
    Button next;
    EditText getPassword;
    EditText verifyPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_password);

        mAuth = FirebaseAuth.getInstance();

        have_acc = findViewById(R.id.have_acc);
        next = findViewById(R.id.next_btn);
        getPassword = findViewById(R.id.get_password);
        verifyPassword = findViewById(R.id.verify_password);

        have_acc.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationPassword.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        verifyPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyPasswordMatch(); // Check if passwords match
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        next.setOnClickListener(v -> {
            String password = getPassword.getText().toString().trim();
            String verify = verifyPassword.getText().toString().trim();

            if (TextUtils.isEmpty(password) || password.length() < 8) {
                Toast.makeText(RegistrationPassword.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(verify)) {
                // Change border color of verifyPassword EditText to red to indicate password mismatch
                verifyPassword.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                Toast.makeText(RegistrationPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Change border color of verifyPassword EditText to green to indicate password match
            verifyPassword.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);

            // Save the password using SharedPreferences
            savePasswordToSharedPreferences(password);

            // Proceed to the next step
            startActivity(new Intent(RegistrationPassword.this, RegistrationContactNo.class));
        });

        CheckBox showPasswordCheckbox = findViewById(R.id.show_password_checkbox);
        showPasswordCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                getPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                verifyPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password
                getPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                verifyPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });


    }

    private void savePasswordToSharedPreferences(String password) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", password);
        editor.apply();
    }


    private void verifyPasswordMatch() {
        String password = getPassword.getText().toString().trim();
        String verify = verifyPassword.getText().toString().trim();

        if (TextUtils.isEmpty(verify)) {
            // Reset border color of verifyPassword EditText if it's empty
            verifyPassword.getBackground().clearColorFilter();
        } else {
            if (password.equals(verify)) {
                // Change border color of verifyPassword EditText to green if passwords match
                verifyPassword.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);
            } else {
                // Change border color of verifyPassword EditText to red if passwords don't match
                verifyPassword.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
