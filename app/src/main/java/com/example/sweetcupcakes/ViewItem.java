package com.example.sweetcupcakes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ViewItem extends AppCompatActivity {
    TextView quantityText;
    TextView increaseButton;
    TextView decreaseButton;
    TextView itemName;
    TextView itemPrice;
    TextView itemDescription;
    ImageView itemImage;

    TextView totalPrice;

    private int quantity = 1;
    private double pricePerItem; // Assuming item price is a double value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        quantityText = findViewById(R.id.quantity_show);
        increaseButton = findViewById(R.id.increase_button);
        decreaseButton = findViewById(R.id.decrease_button);

        itemImage = findViewById(R.id.item_image);
        itemName = findViewById(R.id.item_name);
        itemDescription = findViewById(R.id.item_description);
        itemPrice = findViewById(R.id.item_price);
        totalPrice = findViewById(R.id.total_price);

        quantityText.setText(String.valueOf(quantity));

        // Retrieve item details from intent extras
        String itemNameExtra = getIntent().getStringExtra("item_name");
        String itemPriceExtra = getIntent().getStringExtra("item_price");
        String itemDescriptionExtra = getIntent().getStringExtra("item_description");
        String imageUrlExtra = getIntent().getStringExtra("item_image_url");

        // Parse item price to double
        pricePerItem = Double.parseDouble(itemPriceExtra);

        // Set item details to views
        itemName.setText(itemNameExtra);
        itemPrice.setText(String.format("Rs.%s", itemPriceExtra));
        itemDescription.setText(itemDescriptionExtra); // Set item description

        // Load image using Glide with rounded corners
        RequestOptions requestOptions = new RequestOptions().transform(new RoundedCorners(40));
        Glide.with(this)
                .load(imageUrlExtra)
                .apply(requestOptions)
                .into(itemImage);

        // Set onClickListener for increase button
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment quantity by one
                quantity++;
                // Update TextView to display new quantity
                quantityText.setText(String.valueOf(quantity));
                // Update total price
                updateTotalPrice();
            }
        });

        // Set onClickListener for decrease button
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement quantity by one if it's greater than 1
                if (quantity > 1) {
                    quantity--;
                    // Update TextView to display new quantity
                    quantityText.setText(String.valueOf(quantity));
                    // Update total price
                    updateTotalPrice();
                }
            }
        });

        // Initialize total price
        updateTotalPrice();
    }

    // Method to update total price based on quantity
    private void updateTotalPrice() {
        double total = quantity * pricePerItem;
        totalPrice.setText(String.format(" Rs.%.2f", total));
    }
}
