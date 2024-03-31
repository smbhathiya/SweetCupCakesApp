package com.example.sweetcupcakes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationEmail extends AppCompatActivity {
    TextView have_acc;
    Button next;
    EditText getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_email);

        have_acc = findViewById(R.id.have_acc);
        next = findViewById(R.id.next_btn);
        getEmail = findViewById(R.id.get_email);

        have_acc.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationEmail.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        next.setOnClickListener(v -> {
            String email = getEmail.getText().toString().trim();

            if (!isValidEmail(email)) {
                Toast.makeText(RegistrationEmail.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            } else {
                // Save email to SharedPreferences
                saveEmailToSharedPreferences(email);

                // Proceed to the next registration step
                Intent intent = new Intent(RegistrationEmail.this, RegistrationPassword.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();
    }
}
