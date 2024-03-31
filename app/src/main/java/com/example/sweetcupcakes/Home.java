package com.example.sweetcupcakes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.BaseRequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;




public class Home extends AppCompatActivity {
    Button message;
    Button cart;
    Button user;
    String userEmail;

    ImageView setImage;
    TextView setName;
    TextView setPrice;

    ImageView setImage2;
    TextView setName2;
    TextView setPrice2;

    ImageView setImage3;
    TextView setName3;
    TextView setPrice3;

    ImageView setImage4;
    TextView setName4;
    TextView setPrice4;

    View userContainer1;
    View userContainer2;
    View userContainer3;
    View userContainer4;

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

        // Initialize containers
        userContainer1 = findViewById(R.id.user_container);
        userContainer2 = findViewById(R.id.user_container_2);
        userContainer3 = findViewById(R.id.user_container_3);
        userContainer4 = findViewById(R.id.user_container_4);

        //item 1
        setImage = findViewById(R.id.set_item_image);
        setName = findViewById(R.id.set_item_name);
        setPrice = findViewById(R.id.set_item_price);

        //item 2
        setImage2 = findViewById(R.id.set_item_image_2);
        setName2 = findViewById(R.id.set_item_name_2);
        setPrice2 = findViewById(R.id.set_item_price_2);

        //item 3
        setImage3 = findViewById(R.id.set_item_image_3);
        setName3 = findViewById(R.id.set_item_name_3);
        setPrice3 = findViewById(R.id.set_item_price_3);

        //item 4
        setImage4 = findViewById(R.id.set_item_image_4);
        setName4 = findViewById(R.id.set_item_name_4);
        setPrice4 = findViewById(R.id.set_item_price_4);

        // Set up Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("cupcakes");

        // Load cupcakes data from Firebase
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
        // Listen for changes in the cupcake data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0; // Counter to track the number of cupcakes processed
                // Iterate through all cupcakes
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get cupcake details
                    String itemName = snapshot.child("itemName").getValue(String.class);
                    String itemPrice = snapshot.child("itemPrice").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String itemDescription = snapshot.child("itemDescription").getValue(String.class); // Retrieve item description

                    // Set click listener for each item and open ViewItem activity with corresponding item data
                    switch (count) {
                        case 0:
                            setImage.setOnClickListener(v -> openViewItemActivity(itemName, itemPrice, itemDescription, imageUrl));
                            break;
                        case 1:
                            setImage2.setOnClickListener(v -> openViewItemActivity(itemName, itemPrice, itemDescription, imageUrl));
                            break;
                        case 2:
                            setImage3.setOnClickListener(v -> openViewItemActivity(itemName, itemPrice, itemDescription, imageUrl));
                            break;
                        case 3:
                            setImage4.setOnClickListener(v -> openViewItemActivity(itemName, itemPrice, itemDescription, imageUrl));
                            break;
                    }

                    // Update UI with cupcake data for the current item
                    switch (count) {
                        case 0:
                            setName.setText(itemName);
                            setPrice.setText("Rs. "+itemPrice);
                            Glide.with(Home.this)
                                    .load(imageUrl)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(40, 0)))
                                    .into(setImage);
                            break;
                        case 1:
                            setName2.setText(itemName);
                            setPrice2.setText("Rs. "+itemPrice);
                            Glide.with(Home.this)
                                    .load(imageUrl)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(40, 0)))
                                    .into(setImage2);
                            break;
                        case 2:
                            setName3.setText(itemName);
                            setPrice3.setText("Rs. "+itemPrice);
                            Glide.with(Home.this)
                                    .load(imageUrl)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(40, 0)))
                                    .into(setImage3);
                            userContainer3.setVisibility(View.VISIBLE);
                            setImage3.setVisibility(View.VISIBLE);
                            setName3.setVisibility(View.VISIBLE);
                            setPrice3.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            setName4.setText(itemName);
                            setPrice4.setText("Rs. "+itemPrice);
                            Glide.with(Home.this)
                                    .load(imageUrl)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(40, 0)))
                                    .into(setImage4);
                            userContainer4.setVisibility(View.VISIBLE);
                            setImage4.setVisibility(View.VISIBLE);
                            setName4.setVisibility(View.VISIBLE);
                            setPrice4.setVisibility(View.VISIBLE);
                            break;
                    }
                    // Increment the counter
                    count++;
                }

                // Hide labels and images if no data available for them
                if (count < 2) {
                    userContainer2.setVisibility(View.GONE);
                    setImage2.setVisibility(View.GONE);
                    setName2.setVisibility(View.GONE);
                    setPrice2.setVisibility(View.GONE);
                }
                if (count < 3) {
                    userContainer3.setVisibility(View.GONE);
                    setImage3.setVisibility(View.GONE);
                    setName3.setVisibility(View.GONE);
                    setPrice3.setVisibility(View.GONE);
                }
                if (count < 4) {
                    userContainer4.setVisibility(View.GONE);
                    setImage4.setVisibility(View.GONE);
                    setName4.setVisibility(View.GONE);
                    setPrice4.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    // Method to open ViewItem activity with item details
    private void openViewItemActivity(String itemName, String itemPrice, String itemDescription, String imageUrl) {
        Intent intent = new Intent(Home.this, ViewItem.class);
        intent.putExtra("item_name", itemName);
        intent.putExtra("item_price", itemPrice);
        intent.putExtra("item_description", itemDescription);
        intent.putExtra("item_image_url", imageUrl);
        startActivity(intent);
    }




}
