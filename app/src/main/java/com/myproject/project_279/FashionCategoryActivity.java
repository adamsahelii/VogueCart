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
import android.widget.ImageButton;


public class FashionCategoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<Item> itemsList = new ArrayList<>();
    private FashionItemAdapter adapter;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion_category);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        listView = findViewById(R.id.fashionItemsListView);

        userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", -1);

        adapter = new FashionItemAdapter(itemsList);
        listView.setAdapter(adapter);

        String category = getIntent().getStringExtra("category");
        if (category == null) {
            category = "fashion";
        }
        fetchItemsByCategory(category);
    }

    private void fetchItemsByCategory(String category) {
        // USE MOCK DATA FOR FRONTEND DEVELOPMENT
        itemsList.clear();
        itemsList.addAll(MockDataHelper.getMockProductsByCategory(category));
        adapter.notifyDataSetChanged();

        if (itemsList.isEmpty()) {
            Toast.makeText(FashionCategoryActivity.this,
                    "No items found",
                    Toast.LENGTH_SHORT).show();
        }

        /* ORIGINAL API CODE - uncomment when backend is available
        Call<List<Item>> call = ApiClient.retrofitService.searchItems(category);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemsList.clear();
                    itemsList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (itemsList.isEmpty()) {
                        Toast.makeText(FashionCategoryActivity.this,
                                "No items found",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FashionCategoryActivity.this,
                            "Failed to load items: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(FashionCategoryActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    private class FashionItemAdapter extends BaseAdapter {

        private final List<Item> items;

        public FashionItemAdapter(List<Item> items) {
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
                view = LayoutInflater.from(FashionCategoryActivity.this)
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

            Glide.with(FashionCategoryActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.add1)
                    .into(itemImageView);

            // FAVORITES
            heartIcon.setOnCheckedChangeListener(null);
            heartIcon.setChecked(false);

            heartIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // MOCK BEHAVIOR FOR FRONTEND DEVELOPMENT
                if (isChecked) {
                    Toast.makeText(FashionCategoryActivity.this,
                            item.getName() + " added to favorites (mock)",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FashionCategoryActivity.this,
                            item.getName() + " removed from favorites (mock)",
                            Toast.LENGTH_SHORT).show();
                }

                /* ORIGINAL API CODE - uncomment when backend is available
                if (userId == -1) {
                    Toast.makeText(FashionCategoryActivity.this,
                            "Please sign in to use favorites",
                            Toast.LENGTH_SHORT).show();
                    buttonView.setChecked(false);
                    return;
                }

                if (isChecked) {
                    Call<SimpleResponse> call = ApiClient.retrofitService
                            .addFavorite(userId, item.getId());

                    call.enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            Toast.makeText(FashionCategoryActivity.this,
                                    item.getName() + " added to favorites",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            Toast.makeText(FashionCategoryActivity.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            buttonView.setChecked(false);
                        }
                    });
                } else {
                    Call<SimpleResponse> call = ApiClient.retrofitService
                            .removeFavorite(userId, item.getId());

                    call.enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            Toast.makeText(FashionCategoryActivity.this,
                                    item.getName() + " removed from favorites",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            Toast.makeText(FashionCategoryActivity.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            buttonView.setChecked(true);
                        }
                    });
                }
                */
            });

            // CART
            addToCartButton.setOnClickListener(v -> {
                // MOCK BEHAVIOR FOR FRONTEND DEVELOPMENT
                Toast.makeText(FashionCategoryActivity.this,
                        item.getName() + " added to cart (mock)",
                        Toast.LENGTH_SHORT).show();

                /* ORIGINAL API CODE - uncomment when backend is available
                if (userId == -1) {
                    Toast.makeText(FashionCategoryActivity.this,
                            "Please sign in to add to cart",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Call<SimpleResponse> call = ApiClient.retrofitService
                        .addToCart(userId, item.getId(), 1);

                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        Toast.makeText(FashionCategoryActivity.this,
                                item.getName() + " added to cart",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        Toast.makeText(FashionCategoryActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                */
            });

            return view;
        }
    }
}
