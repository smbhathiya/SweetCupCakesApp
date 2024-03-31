package com.example.sweetcupcakes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ManageCategory extends AppCompatActivity {
    EditText categoryName;
    Button addCategory;
    Button deleteCategory;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        categoryName = findViewById(R.id.category_name);
        addCategory = findViewById(R.id.add_category);
        deleteCategory = findViewById(R.id.delete_category);

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("categories");

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the category name from the EditText
                String category = categoryName.getText().toString().trim();

                if (!category.isEmpty()) {
                    // Add the category to the Firebase database
                    databaseReference.push().setValue(category);

                    // Show success message
                    Toast.makeText(ManageCategory.this, "Category added successfully", Toast.LENGTH_SHORT).show();

                    // Clear the EditText
                    categoryName.setText("");
                } else {
                    // Show error message if category name is empty
                    Toast.makeText(ManageCategory.this, "Please enter a category", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the category name from the EditText
                String category = categoryName.getText().toString().trim();

                if (!category.isEmpty()) {
                    // Check if the category exists in the database
                    Query query = databaseReference.orderByValue().equalTo(category);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Remove the category from the database
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                }

                                // Show success message
                                Toast.makeText(ManageCategory.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Show message if the category does not exist
                                Toast.makeText(ManageCategory.this, "No category found with that name", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                } else {
                    // Show error message if category name is empty
                    Toast.makeText(ManageCategory.this, "Please enter a category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
