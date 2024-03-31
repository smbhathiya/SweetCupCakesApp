package com.example.sweetcupcakes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationName extends AppCompatActivity {
    TextView have_acc;
    Button next;
    EditText firstName;
    EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_name);

        have_acc = findViewById(R.id.have_acc);
        next = findViewById(R.id.next_btn);
        firstName = findViewById(R.id.get_first_name);
        lastName = findViewById(R.id.get_last_name);

        firstName.setText("");
        lastName.setText("");


        have_acc.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationName.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        next.setOnClickListener(v -> {
            String fName = firstName.getText().toString().trim();
            String lName = lastName.getText().toString().trim();

            if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName)) {
                Toast.makeText(RegistrationName.this, "Please enter your first and last names", Toast.LENGTH_SHORT).show();
            } else {
                // Save first and last names to SharedPreferences
                saveNamesToSharedPreferences(fName, lName);

                // Proceed to the next registration step
                Intent intent = new Intent(RegistrationName.this, RegistrationEmail.class);
                startActivity(intent);
            }
        });

        // Retrieve saved first name and last name
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String savedFirstName = preferences.getString("firstName", "");
        String savedLastName = preferences.getString("lastName", "");

        // Set the EditText fields with the retrieved names
        firstName.setText(savedFirstName);
        lastName.setText(savedLastName);
    }

    private void saveNamesToSharedPreferences(String firstName, String lastName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.apply();
    }
}
