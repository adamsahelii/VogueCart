package com.myproject.project_279;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private TextView profileNameTextView;
    private Button saveProfileButton;
    private ImageView profileImageView;

    private static final String PREFS_NAME = "user_profile";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PROFILE_IMAGE_URI = "profile_image_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);

        profileImageView = findViewById(R.id.profileImage);

        // 🔹 Find views
        profileNameTextView = findViewById(R.id.profileName);
        firstNameEditText = findViewById(R.id.firstNameLabel);
        lastNameEditText = findViewById(R.id.lastNameLabel);
        emailEditText = findViewById(R.id.emailLabel);
        phoneEditText = findViewById(R.id.phoneLabel);
        saveProfileButton = findViewById(R.id.saveProfile);

        // 🔹 Load saved data (if any)
        loadProfileData();

        // 🔹 Save button logic
        saveProfileButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(ProfilePage.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            saveProfileData(firstName, lastName, email, phone);

            // Update the big name under the profile picture
            profileNameTextView.setText(firstName);

            Toast.makeText(ProfilePage.this, "Profile saved", Toast.LENGTH_SHORT).show();
        });

        // 🔹 Edit profile button → go to UploadPage (for picture)
        Button editProfileButton = findViewById(R.id.editProfile);
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, UploadPage.class);
            startActivity(intent);
        });

        // 🔹 Change password
        Button changePasswordButton = findViewById(R.id.changePassword);
        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        // 🔹 Sign out (simple: go back to SignIn screen)
        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // 🔹 Delete account (for now just Toast – backend can be added later)
        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);
        deleteAccountButton.setOnClickListener(v -> {
            Toast.makeText(ProfilePage.this, "Delete account feature not implemented yet", Toast.LENGTH_SHORT).show();
        });

        // 🔹 Bottom nav: Favorites
        ImageButton favoriteButton = findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // 🔹 Bottom nav: Home
        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfilePage.this, MainPageActivity.class));
        });

        // 🔹 Bottom nav: Scan
        ImageButton scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfilePage.this, ProductPageActivity.class));
        });

        // 🔹 Bottom nav: Cart
        ImageButton shopButton = findViewById(R.id.shop_button);
        shopButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfilePage.this, CartFragment.class));
        });

        // We are already on Profile, so profile_button doesn't need to navigate
        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            // Do nothing or scroll to top, etc.
        });
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load text fields
        String firstName = prefs.getString(KEY_FIRST_NAME, firstNameEditText.getText().toString());
        String lastName = prefs.getString(KEY_LAST_NAME, lastNameEditText.getText().toString());
        String email = prefs.getString(KEY_EMAIL, emailEditText.getText().toString());
        String phone = prefs.getString(KEY_PHONE, phoneEditText.getText().toString());

        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        emailEditText.setText(email);
        phoneEditText.setText(phone);

        // Update big display name
        profileNameTextView.setText(firstName);

        // ✅ Load saved profile image (if any)
        String imageUriString = prefs.getString(KEY_PROFILE_IMAGE_URI, null);
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            profileImageView.setImageURI(imageUri);
        }
    }

    private void saveProfileData(String firstName, String lastName, String email, String phone) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.apply();
    }
}
