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

public class SearchResultsActivity extends AppCompatActivity {

    private ListView listView;
    private TextView noResultsTextView;

    private List<Item> itemsList = new ArrayList<>();
    private SearchItemAdapter adapter;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        listView = findViewById(R.id.ItemsListView1);
        noResultsTextView = findViewById(R.id.noResultsTextView);

        userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", -1);

        adapter = new SearchItemAdapter(itemsList);
        listView.setAdapter(adapter);

        String searchQuery = getIntent().getStringExtra("SEARCH_QUERY");
        if (searchQuery == null) {
            searchQuery = "";
        }

        if (!searchQuery.isEmpty()) {
            searchItems(searchQuery);
        } else {
            Toast.makeText(this, "Search query is empty", Toast.LENGTH_SHORT).show();
            showNoResults();
        }
    }

    private void searchItems(String query) {
        // Uses Retrofit searchItems -> products.php?query=
        Call<List<Item>> call = ApiClient.retrofitService.searchItems(query);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemsList.clear();
                    itemsList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (itemsList.isEmpty()) {
                        showNoResults();
                    } else {
                        showResults();
                    }
                } else {
                    Toast.makeText(SearchResultsActivity.this,
                            "Failed to load items: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                    showNoResults();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(SearchResultsActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                showNoResults();
            }
        });
    }

    private void showNoResults() {
        noResultsTextView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private void showResults() {
        noResultsTextView.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private class SearchItemAdapter extends BaseAdapter {

        private final List<Item> items;

        public SearchItemAdapter(List<Item> items) {
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
                view = LayoutInflater.from(SearchResultsActivity.this)
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

            Glide.with(SearchResultsActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.add1)
                    .into(itemImageView);

            // FAVORITES
            heartIcon.setOnCheckedChangeListener(null);
            heartIcon.setChecked(false); // we’re not preloading favorite state in search screen

            heartIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (userId == -1) {
                    Toast.makeText(SearchResultsActivity.this,
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
                            Toast.makeText(SearchResultsActivity.this,
                                    item.getName() + " added to favorites",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            Toast.makeText(SearchResultsActivity.this,
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
                            Toast.makeText(SearchResultsActivity.this,
                                    item.getName() + " removed from favorites",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            Toast.makeText(SearchResultsActivity.this,
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
                    Toast.makeText(SearchResultsActivity.this,
                            "Please sign in to add to cart",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Call<SimpleResponse> call = ApiClient.retrofitService
                        .addToCart(userId, item.getId(), 1);

                call.enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        Toast.makeText(SearchResultsActivity.this,
                                item.getName() + " added to cart",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        Toast.makeText(SearchResultsActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });

            return view;
        }
    }
}
