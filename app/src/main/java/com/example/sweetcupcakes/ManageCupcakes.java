package com.example.sweetcupcakes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ManageCupcakes extends AppCompatActivity {
    ImageView cupcakeImage;
    EditText getItemName;
    EditText getItemPrice;
    EditText getItemCategory;
    EditText getItemDescription;
    Button addItem;
    Button deleteItem;
    EditText searchItem;
    private ProgressDialog progressDialog;

    private static final int PICK_IMAGE = 100;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cupcakes);

        cupcakeImage = findViewById(R.id.cupcake_image);
        getItemName = findViewById(R.id.get_item_name);
        getItemCategory = findViewById(R.id.select_item_category);
        getItemPrice = findViewById(R.id.get_item_price);
        getItemDescription = findViewById(R.id.get_item_description);
        addItem = findViewById(R.id.add_items);
        deleteItem = findViewById(R.id.delete_item);
        searchItem = findViewById(R.id.search_item);


        cupcakeImage.setOnClickListener(v -> openGallery());
        deleteItem.setOnClickListener(v -> deleteItem());

        addItem.setOnClickListener(v -> {
            // Check if all fields are filled
            String itemName = getItemName.getText().toString().trim();
            String itemCategory = getItemCategory.getText().toString().trim();
            String itemPrice = getItemPrice.getText().toString().trim();
            String itemDescription = getItemDescription.getText().toString().trim();

            if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(itemCategory) ||
                    TextUtils.isEmpty(itemPrice) || TextUtils.isEmpty(itemDescription)) {
                // Show a toast message if any field is empty
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (imageUri != null) {
                // All fields are filled, proceed with uploading image to Firebase

                // Add progress dialog for adding data
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Adding item..."); // Set message
                progressDialog.setCancelable(false); // Set cancelable to false to prevent dismissal on outside touch
                progressDialog.setIndeterminate(true); // Set indeterminate mode to true
                progressDialog.show(); // Show progress dialog

                uploadImageToFirebase(imageUri);
            } else {
                // Show a toast message if no image is selected
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });






        // Set up the editor action listener for the search field
        searchItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Perform the search operation
                    String itemNameToSearch = searchItem.getText().toString().trim();
                    if (!TextUtils.isEmpty(itemNameToSearch)) {
                        searchItem(itemNameToSearch);
                        return true; // Consume the event
                    }
                }
                return false;
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                cupcakeImage.setImageURI(data.getData());
                imageUri = data.getData();
                // Load the picked image with rounded corners using Glide
                RequestOptions requestOptions = RequestOptions
                        .bitmapTransform(new RoundedCorners(40)); // 40px radius for rounded corners
                Glide.with(this)
                        .load(imageUri)
                        .apply(requestOptions)
                        .into(cupcakeImage);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Get reference to Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("cupcake_images");

        // Generate a unique name for the image file
        String imageName = System.currentTimeMillis() + ".jpg";

        // Upload the image to Firebase Storage
        storageReference.child(imageName).putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    storageReference.child(imageName).getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Save image URL along with other item details to database
                                saveDataToDatabase(uri);
                                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors
                                Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveDataToDatabase(Uri imageUrl) {
        // Convert Cupcake object to a Map
        Map<String, Object> cupcakeMap = new HashMap<>();
        cupcakeMap.put("itemName", getItemName.getText().toString().trim());
        cupcakeMap.put("itemCategory", getItemCategory.getText().toString().trim());
        cupcakeMap.put("itemPrice", getItemPrice.getText().toString().trim());
        cupcakeMap.put("itemDescription", getItemDescription.getText().toString().trim());
        cupcakeMap.put("imageUrl", imageUrl.toString()); // Save image URL

        // Get reference to Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cupcakes");

        // Generate a unique key for the cupcake
        String cupcakeId = databaseReference.push().getKey();

        // Set the Cupcake data in the database with the automatically generated ID
        databaseReference.child(cupcakeId).setValue(cupcakeMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cupcake added successfully", Toast.LENGTH_SHORT).show();
                    // Clear all EditText fields and set them to defaults after successful save
                    clearFields();
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(this, "Failed to add cupcake: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }

    private void searchItem(String itemName) {
        // Initialize progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching for item..."); // Set message
        progressDialog.setCancelable(false); // Set cancelable to false to prevent dismissal on outside touch
        progressDialog.setIndeterminate(true); // Set indeterminate mode to true
        progressDialog.show(); // Show progress dialog

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cupcakes");
        Query query = databaseReference.orderByChild("itemName").equalTo(itemName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss(); // Dismiss progress dialog after data retrieval
                if (dataSnapshot.exists()) {
                    // Assuming there's only one cupcake with the given name, get the first matching snapshot
                    DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                    String itemCategory = snapshot.child("itemCategory").getValue(String.class);
                    String itemPrice = snapshot.child("itemPrice").getValue(String.class);
                    String itemDescription = snapshot.child("itemDescription").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    // Check if image URL is not null or empty before using Glide
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Load the image using Glide with rounded corners
                        RequestOptions requestOptions = RequestOptions
                                .bitmapTransform(new RoundedCorners(40)); // 40px radius for rounded corners
                        Glide.with(ManageCupcakes.this)
                                .load(imageUrl)
                                .apply(requestOptions)
                                .placeholder(R.drawable.add_image) // Set a placeholder image
                                .error(R.drawable.error_image) // Set an error image
                                .into(cupcakeImage);
                    } else {
                        // Show message if no image URL is found
                        Toast.makeText(ManageCupcakes.this, "No image available for this cupcake", Toast.LENGTH_SHORT).show();
                        // You can also set a default image here using Glide's placeholder method
                    }

                    // Set item details to EditText fields
                    getItemName.setText(itemName);
                    getItemCategory.setText(itemCategory);
                    getItemPrice.setText(itemPrice);
                    getItemDescription.setText(itemDescription);
                } else {
                    // Show a message if no cupcake with the entered name is found
                    Toast.makeText(ManageCupcakes.this, "No cupcake found with that name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                progressDialog.dismiss(); // Dismiss progress dialog on error
                Log.e("FirebaseDatabase", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }


    // Method to delete item from the database
// Method to delete item from the database
    private void deleteItem() {
        String itemNameToDelete = getItemName.getText().toString().trim();
        if (!TextUtils.isEmpty(itemNameToDelete)) {
            // Initialize progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Deleting item..."); // Set message
            progressDialog.setCancelable(false); // Set cancelable to false to prevent dismissal on outside touch
            progressDialog.setIndeterminate(true); // Set indeterminate mode to true
            progressDialog.show(); // Show progress dialog

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this item?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cupcakes");
                Query query = databaseReference.orderByChild("itemName").equalTo(itemNameToDelete);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Get the image URL before deleting the item
                                String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                                // Remove the item from the database
                                snapshot.getRef().removeValue();

                                // Delete the corresponding image from Firebase Storage
                                if (imageUrl != null) {
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                                    storageReference.delete()
                                            .addOnSuccessListener(aVoid -> Log.d("DeleteImage", "Image deleted successfully"))
                                            .addOnFailureListener(e -> Log.e("DeleteImage", "Failed to delete image: " + e.getMessage()));
                                }
                            }
                            // Clear all EditText fields and set them to defaults
                            clearFields();
                            Toast.makeText(ManageCupcakes.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ManageCupcakes.this, "No item found with that name", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss(); // Dismiss progress dialog after operation completes
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        Log.e("FirebaseDatabase", "Error deleting item: " + databaseError.getMessage());
                        progressDialog.dismiss(); // Dismiss progress dialog on error
                    }
                });
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                progressDialog.dismiss(); // Dismiss progress dialog if user cancels operation
                // User clicked "No" button, do nothing
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            Toast.makeText(this, "Please enter an item name to delete", Toast.LENGTH_SHORT).show();
        }
    }




    // Method to clear all EditText fields and set them to defaults
    private void clearFields() {
        getItemName.setText("");
        getItemCategory.setText("");
        getItemPrice.setText("");
        getItemDescription.setText("");
        cupcakeImage.setImageResource(R.drawable.add_image); // Set default image
        imageUri = null; // Reset image URI
    }
}
