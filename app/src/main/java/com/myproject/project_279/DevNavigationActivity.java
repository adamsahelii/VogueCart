package com.myproject.project_279;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;

public class DevNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a scrollable linear layout programmatically
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        // Add title
        Button title = new Button(this);
        title.setText("🚀 DEVELOPER NAVIGATION");
        title.setEnabled(false);
        title.setTextSize(18);
        layout.addView(title);

        // Add buttons for each activity
        addNavigationButton(layout, "Welcome Screen", MainActivity.class);
        addNavigationButton(layout, "Sign In", SignInActivity.class);
        addNavigationButton(layout, "Sign Up", SignUpActivity.class);
        addNavigationButton(layout, "Main Page (Home)", MainPageActivity.class);
        addNavigationButton(layout, "Product Page", ProductPageActivity.class);
        addNavigationButton(layout, "Search Results", SearchResultsActivity.class);
        addNavigationButton(layout, "Cart", CartFragment.class);
        addNavigationButton(layout, "Cart Activity", CartActivity.class);
        addNavigationButton(layout, "Favorites", FavoritesActivity.class);
        addNavigationButton(layout, "Profile Page", ProfilePage.class);
        addNavigationButton(layout, "Order Detail", OrderDetailActivity.class);
        addNavigationButton(layout, "Upload Page", UploadPage.class);
        addNavigationButton(layout, "Change Password", ChangePasswordActivity.class);

        // Category pages
        addNavigationButton(layout, "Category - Fashion", FashionCategoryActivity.class);
        addNavigationButton(layout, "Category - Shoes", ShoesCategoryActivity.class);
        addNavigationButton(layout, "Category - Electronics", ElectronicsCategoryActivity.class);
        addNavigationButton(layout, "Category - Sports", SportsCategoryActivity.class);
        addNavigationButton(layout, "Category - Cosmetics", CosmeticsCategoryActivity.class);

        scrollView.addView(layout);
        setContentView(scrollView);
    }

    private void addNavigationButton(LinearLayout layout, String label, Class<?> activityClass) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        button.setLayoutParams(params);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, activityClass);
            // Add mock data for activities that need it
            if (activityClass == ProductPageActivity.class) {
                intent.putExtra("ITEM_ID", 1);
            } else if (activityClass == SearchResultsActivity.class) {
                intent.putExtra("SEARCH_QUERY", "test");
            }
            startActivity(intent);
        });

        layout.addView(button);
    }
}
