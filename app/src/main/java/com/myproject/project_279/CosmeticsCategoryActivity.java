package com.myproject.project_279;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ImageButton;


public class CosmeticsCategoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<Item> itemsList = new ArrayList<>();
    private CosmeticsItemAdapter adapter;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosmetics_category);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        listView = findViewById(R.id.cosmeticsItemsListView);

        // get logged-in user id (set in SignInActivity)
        userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", -1);

        adapter = new CosmeticsItemAdapter(itemsList);
        listView.setAdapter(adapter);

        // load cosmetics items from backend
        fetchItemsByCategory("cosmetics");
    }

    private void fetchItemsByCategory(String category) {
        // We reuse searchItems() on products.php?query=cosmetics
        Call<List<Item>> call = ApiClient.retrofitService.searchItems(category);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemsList.clear();
                    itemsList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (itemsList.isEmpty()) {
                        Toast.makeText(CosmeticsCategoryActivity.this,
                                "No items found for category",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CosmeticsCategoryActivity.this,
                            "Failed to load items: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(CosmeticsCategoryActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CosmeticsItemAdapter extends BaseAdapter {

        private final List<Item> items;

        public CosmeticsItemAdapter(List<Item> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(CosmeticsCategoryActivity.this)
                        .inflate(R.layout.list_item_fashion, parent, false);
            }

            Item item = items.get(position);

            TextView itemNameTextView = view.findViewById(R.id.itemName);
            TextView itemPriceTextView = view.findViewById(R.id.itemPrice);
            ImageView itemImageView = view.findViewById(R.id.itemImg);
            CheckBox heartIcon = view.findViewById(R.id.heartIcon);
            Button addToCartButton = view.findViewById(R.id.add_to_cart_button);

            itemNameTextView.setText(item.getName());
            itemPriceTextView.setText("$" + item.getPrice());

            // PHP backend should return full image_url or relative; here we assume it's full URL
            Glide.with(CosmeticsCategoryActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.add1)
                    .into(itemImageView);

            // FAVORITES: only allow if user is logged in
            heartIcon.setOnCheckedChangeListener(null); // avoid old listeners on recycle
            heartIcon.setChecked(false); // we’re not preloading favorite state from backend for now

            heartIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (userId == -1) {
                    Toast.makeText(CosmeticsCategoryActivity.this,
                            "Please sign in to use favorites",
                            Toast.LENGTH_SHORT).show();
                    buttonView.setChecked(false);
                    return;
                }

                if (isChecked) {
                    // add to favorites via backend
                    Call<SimpleResponse> call = ApiClient.retrofitService
                            .addFavorite(userId, item.getId());

                    call.enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            Toast.makeText(CosmeticsCategoryActivity.this,
                                    item.getName() + " added to favorites",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            Toast.makeText(CosmeticsCategoryActivity.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            buttonView.setChecked(false);
                        }
                    });
                } else {
                    // remove from favorites via backend
                    Call<SimpleResponse> call = ApiClient.retrofitService
                            .removeFavorite(userId, item.getId());

                    call.enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            Toast.makeText(CosmeticsCategoryActivity.this,
                                    item.getName() + " removed from favorites",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            Toast.makeText(CosmeticsCategoryActivity.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            buttonView.setChecked(true);
                        }
                    });
                }
            });

            // CART
            addToCartButton.setOnClickListener(v -> {
                if (userId == -1) {
                    Toast.makeText(CosmeticsCategoryActivity.this,
                            "Please sign in to add to cart",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Call<SimpleResponse> call = ApiClient.retrofitService
                        .addToCart(userId, item.getId(), 1);

                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        Toast.makeText(CosmeticsCategoryActivity.this,
                                item.getName() + " added to cart",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        Toast.makeText(CosmeticsCategoryActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });

            return view;
        }
    }
}
