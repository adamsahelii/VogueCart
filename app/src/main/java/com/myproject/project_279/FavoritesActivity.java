package com.myproject.project_279;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private View emptyImageView;
    private TextView emptyTextView;
    private TextView subTextView;
    private Button returnButton;

    private List<Item> favoriteItems = new ArrayList<>();
    private FavoritesAdapter adapter;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesRecyclerView = findViewById(R.id.favorites_list);
        emptyImageView = findViewById(R.id.imageView);
        emptyTextView = findViewById(R.id.textView4);
        subTextView = findViewById(R.id.textView5);
        returnButton = findViewById(R.id.return_button);

        userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", MockDataHelper.MOCK_USER_ID); // Use mock user ID as default

        adapter = new FavoritesAdapter(favoriteItems, this, item -> removeItemFromFavorites(item));
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoritesRecyclerView.setAdapter(adapter);

        loadFavoritesFromBackend();

        returnButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainPageActivity.class));
        });

        findViewById(R.id.home_button).setOnClickListener(v -> {
            startActivity(new Intent(this, MainPageActivity.class));
        });

        findViewById(R.id.shop_button).setOnClickListener(v -> {
            startActivity(new Intent(this, CartFragment.class));
        });

        findViewById(R.id.scan_button).setOnClickListener(v -> {
            startActivity(new Intent(this, ProductPageActivity.class));
        });

        findViewById(R.id.profile_button).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfilePage.class));
        });
    }

    private void loadFavoritesFromBackend() {
        // USE MOCK DATA FOR FRONTEND DEVELOPMENT
        favoriteItems.clear();
        favoriteItems.addAll(MockDataHelper.getMockFavorites());
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void removeItemFromFavorites(Item item) {
        // MOCK BEHAVIOR FOR FRONTEND DEVELOPMENT
        favoriteItems.remove(item);
        adapter.notifyDataSetChanged();
        updateEmptyState();
        Toast.makeText(FavoritesActivity.this,
                item.getName() + " removed (mock)",
                Toast.LENGTH_SHORT).show();
    }

    private void updateEmptyState() {
        if (!favoriteItems.isEmpty()) {
            favoritesRecyclerView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
            subTextView.setVisibility(View.GONE);
            returnButton.setVisibility(View.GONE);
        } else {
            favoritesRecyclerView.setVisibility(View.GONE);
            emptyImageView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            subTextView.setVisibility(View.VISIBLE);
            returnButton.setVisibility(View.VISIBLE);
        }
    }
}
