package com.myproject.project_279;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class UploadPage extends AppCompatActivity {

    private ImageView profilePreview;
    private Uri currentImageUri;

    private static final String PREFS_NAME = "user_profile";
    private static final String KEY_PROFILE_IMAGE_URI = "profile_image_uri";

    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_page);

        profilePreview = findViewById(R.id.profilePreview);
        Button pickImageButton = findViewById(R.id.pickImageButton);
        Button saveImageButton = findViewById(R.id.saveImageButton);

        // Load existing image if saved
        loadSavedImage();

        // Activity Result launcher for gallery picker
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        currentImageUri = uri;
                        profilePreview.setImageURI(uri);
                    }
                }
        );

        // Open gallery
        pickImageButton.setOnClickListener(v ->
                pickImageLauncher.launch("image/*")
        );

        // Save selected image URI to SharedPreferences
        saveImageButton.setOnClickListener(v -> {
            if (currentImageUri == null) {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit()
                    .putString(KEY_PROFILE_IMAGE_URI, currentImageUri.toString())
                    .apply();

            Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show();

            // Go back to ProfilePage
            Intent intent = new Intent(this, ProfilePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Bottom navigation
        findViewById(R.id.FavoriteButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.homeButton).setOnClickListener(v -> {
            startActivity(new Intent(this, MainPageActivity.class));
        });

        findViewById(R.id.ScanButton).setOnClickListener(v -> {
            startActivity(new Intent(this, ProductPageActivity.class));
        });

        findViewById(R.id.CartButton).setOnClickListener(v -> {
            startActivity(new Intent(this, CartFragment.class));
        });
    }

    private void loadSavedImage() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String uriString = prefs.getString(KEY_PROFILE_IMAGE_URI, null);
        if (uriString != null && !uriString.isEmpty()) {
            Uri uri = Uri.parse(uriString);
            currentImageUri = uri;
            profilePreview.setImageURI(uri);
        }
    }
}
