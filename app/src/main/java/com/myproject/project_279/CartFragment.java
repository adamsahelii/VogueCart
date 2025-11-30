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

public class CartFragment extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private View emptyImageView;
    private TextView emptyTextView;
    private TextView subTextView;
    private Button returnButton;
    private Button proceedButton;
    private List<Item> cartItems = new ArrayList<>();
    private AddToCartAdapter adapter;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_fragment);

        cartRecyclerView = findViewById(R.id.cart_list);
        emptyImageView = findViewById(R.id.empty_cart_image);
        emptyTextView = findViewById(R.id.cart_empty_text);
        subTextView = findViewById(R.id.cart_empty_subtext);
        returnButton = findViewById(R.id.button_return_home);
        proceedButton = findViewById(R.id.button_proceed_to_checkout);

        userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Please sign in first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        adapter = new AddToCartAdapter(cartItems, this, item -> removeFromCart(item));
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(adapter);

        loadCartFromBackend();

        proceedButton.setOnClickListener(v -> {
            Intent checkoutIntent = new Intent(this, CartActivity.class);
            checkoutIntent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems));
            startActivity(checkoutIntent);
        });

        returnButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainPageActivity.class));
        });

        findViewById(R.id.home_button).setOnClickListener(v -> {
            startActivity(new Intent(this, MainPageActivity.class));
        });

        findViewById(R.id.favorite_button).setOnClickListener(v -> {
            startActivity(new Intent(this, FavoritesActivity.class));
        });

        findViewById(R.id.scan_button).setOnClickListener(v -> {
            startActivity(new Intent(this, ProductPageActivity.class));
        });

        findViewById(R.id.profile_button).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfilePage.class));
        });
    }

    private void loadCartFromBackend() {
        Call<CartResponse> call = ApiClient.retrofitService.getCart(userId);

        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    cartItems.clear();
                    if (response.body().getItems() != null) {
                        cartItems.addAll(response.body().getItems());
                    }
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                } else {
                    Toast.makeText(CartFragment.this,
                            "Failed to load cart",
                            Toast.LENGTH_SHORT).show();
                    updateEmptyState();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(CartFragment.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                updateEmptyState();
            }
        });
    }

    private void removeFromCart(Item item) {
        Call<SimpleResponse> call = ApiClient.retrofitService.removeFromCart(userId, item.getId());

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                cartItems.remove(item);
                adapter.notifyDataSetChanged();
                updateEmptyState();
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(CartFragment.this,
                        "Error removing from cart: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmptyState() {
        if (!cartItems.isEmpty()) {
            cartRecyclerView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
            subTextView.setVisibility(View.GONE);
            returnButton.setVisibility(View.GONE);
            proceedButton.setVisibility(View.VISIBLE);
        } else {
            cartRecyclerView.setVisibility(View.GONE);
            emptyImageView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            subTextView.setVisibility(View.VISIBLE);
            returnButton.setVisibility(View.VISIBLE);
            proceedButton.setVisibility(View.GONE);
        }
    }
}
