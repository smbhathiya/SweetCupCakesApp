package com.example.sweetcupcakes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    Button message;
    Button cart;
    Button user;
    String userEmail;

    RecyclerView recyclerView;
    List<Cupcake> cupcakesList;
    CupcakeAdapter adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userEmail = getIntent().getStringExtra("user_email");

        // Initialize ImageViews
        message = findViewById(R.id.message_icon_btn);
        cart = findViewById(R.id.cart_icon_btn);
        user = findViewById(R.id.user_icon_btn);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Set GridLayoutManager with span count 2
        cupcakesList = new ArrayList<>();
        adapter = new CupcakeAdapter(cupcakesList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("cupcakes");
        loadCupcakesData();



        // Set click listeners
        message.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Messages.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        cart.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Cart.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        user.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, User.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Close App")
                .setMessage("Are you sure you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the app
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadCupcakesData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cupcakesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cupcake cupcake = snapshot.getValue(Cupcake.class);
                    cupcakesList.add(cupcake);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
