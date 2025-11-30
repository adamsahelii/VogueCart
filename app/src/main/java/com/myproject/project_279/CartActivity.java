package com.myproject.project_279;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ArrayList<Item> cartItems;
    private CheckoutAdapter checkoutAdapter;
    private TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalPriceTextView = findViewById(R.id.total_amount);

        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        checkoutAdapter = new CheckoutAdapter(cartItems, this, () -> updateTotalPrice());

        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(checkoutAdapter);

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

        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (Item item : cartItems) {
            try {
                total += Double.parseDouble(item.getPrice()) * item.getQuantity();
            } catch (NumberFormatException e) {
                // Handle invalid price format
            }
        }
        totalPriceTextView.setText(String.format("%.2f", total));
    }
}

