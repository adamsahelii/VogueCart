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
                .getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Please sign in first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

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
        Call<FavoritesResponse> call = ApiClient.retrofitService.getFavorites(userId);

        call.enqueue(new Callback<FavoritesResponse>() {
            @Override
            public void onResponse(Call<FavoritesResponse> call, Response<FavoritesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    favoriteItems.clear();
                    if (response.body().getItems() != null) {
                        favoriteItems.addAll(response.body().getItems());
                    }
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                } else {
                    Toast.makeText(FavoritesActivity.this,
                            "Failed to load favorites",
                            Toast.LENGTH_SHORT).show();
                    updateEmptyState();
                }
            }

            @Override
            public void onFailure(Call<FavoritesResponse> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                updateEmptyState();
            }
        });
    }

    private void removeItemFromFavorites(Item item) {
        Call<SimpleResponse> call = ApiClient.retrofitService.removeFavorite(userId, item.getId());

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                // Regardless of server response, update UI
                favoriteItems.remove(item);
                adapter.notifyDataSetChanged();
                updateEmptyState();
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(FavoritesActivity.this,
                        "Error removing favorite: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
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
