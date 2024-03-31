package com.example.sweetcupcakes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationContactNo extends AppCompatActivity {
    TextView have_acc;
    Button next;
    EditText getContactNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_contact_no);

        have_acc = findViewById(R.id.have_acc);
        next = findViewById(R.id.next_btn);
        getContactNo = findViewById(R.id.get_contact_no);

        have_acc.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationContactNo.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        next.setOnClickListener(v -> {
            String contactNo = getContactNo.getText().toString().trim();

            if (isValidContactNo(contactNo)) {
                saveContactNoToSharedPreferences(contactNo);
                startActivity(new Intent(RegistrationContactNo.this, RegistrationAddress.class));
            } else {
                Toast.makeText(RegistrationContactNo.this, "Please enter a valid contact number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidContactNo(String contactNo) {
        return !TextUtils.isEmpty(contactNo) && (contactNo.length() == 9 || contactNo.length() == 10);
    }

    private void saveContactNoToSharedPreferences(String contactNo) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("contactNo", contactNo);
        editor.apply();
    }
}
