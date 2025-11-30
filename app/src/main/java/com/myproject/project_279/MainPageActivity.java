package com.myproject.project_279;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Arrays;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    private RecyclerView searchResultsRecyclerView;
    private SearchAdapter searchAdapter;
    private EditText searchInput;
    private Button searchButton;

    private EditText searchEditText;
    private ImageView searchIcon;
    private ViewPager2 adsViewPager;
    private AdsPagerAdapter adsPagerAdapter;
    private ImageView[] dots;
    private CheckBox heartCheckBox1;
    private CheckBox heartCheckBox2;
    private ImageButton buttonHome;
    private ImageButton buttonFavorite;
    private ImageButton buttonScan;
    private ImageButton buttonCart;
    private ImageButton buttonProfile;
    private List<Integer> adList = Arrays.asList(R.drawable.add1, R.drawable.add2, R.drawable.add3, R.drawable.add4);

    private String searchText;
    private boolean isHeart1Checked = false;
    private boolean isHeart2Checked = false;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        searchEditText = findViewById(R.id.searchEditText);
        searchIcon = findViewById(R.id.searchIcon);
        adsViewPager = findViewById(R.id.adsViewPager);
        heartCheckBox1 = findViewById(R.id.heartIcon1);
        heartCheckBox2 = findViewById(R.id.heartIcon2);
        buttonHome = findViewById(R.id.button_home);
        buttonFavorite = findViewById(R.id.button_favorite);
        buttonScan = findViewById(R.id.button_scan);
        buttonCart = findViewById(R.id.button_cart);
        buttonProfile = findViewById(R.id.button_profile);

        adsPagerAdapter = new AdsPagerAdapter(adList);
        adsViewPager.setAdapter(adsPagerAdapter);

        setupDotsIndicator();

        adsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDotsIndicator(position);
            }
        });

        setupListeners();

        heartCheckBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int color = isChecked ?
                    ContextCompat.getColor(this, R.color.orange) :
                    ContextCompat.getColor(this, R.color.dark_icon);
            heartCheckBox1.setButtonTintList(ColorStateList.valueOf(color));
            heartCheckBox1.setBackgroundResource(isChecked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
            isHeart1Checked = isChecked;
        });

        heartCheckBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int color = isChecked ?
                    ContextCompat.getColor(this, R.color.orange) :
                    ContextCompat.getColor(this, R.color.dark_icon);
            heartCheckBox2.setButtonTintList(ColorStateList.valueOf(color));
            heartCheckBox2.setBackgroundResource(isChecked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
            isHeart2Checked = isChecked;
        });

        buttonHome.setOnClickListener(v -> {
            // Home button clicked
        });
        buttonFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
        buttonScan.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, ProductPageActivity.class);
            startActivity(intent);
        });
        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, CartFragment.class);
            startActivity(intent);
        });
        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, ProfilePage.class);
            startActivity(intent);
        });
        ImageView fashionImageView = findViewById(R.id.category_fashion);
        fashionImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, FashionCategoryActivity.class);
            startActivity(intent);
        });
        ImageView ShoesImageView = findViewById(R.id.category_shoes);
        ShoesImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, ShoesCategoryActivity.class);
            startActivity(intent);
        });
        ImageView ElectronicImageView = findViewById(R.id.category_electronics);
        ElectronicImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, ElectronicsCategoryActivity.class);
            startActivity(intent);
        });
        ImageView SportImageView = findViewById(R.id.category_sports);
        SportImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, SportsCategoryActivity.class);
            startActivity(intent);
        });
        ImageView CosmeticImageView = findViewById(R.id.category_cosmetics);
        CosmeticImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainPageActivity.this, CosmeticsCategoryActivity.class);
            startActivity(intent);
        });

        searchIcon.setOnClickListener(v -> {
            String searchText = searchEditText.getText().toString();
            if (!searchText.isEmpty()) {
                Intent intent = new Intent(this, SearchResultsActivity.class);
                intent.putExtra("SEARCH_QUERY", searchText);
                startActivity(intent);
            }
        });
    }

    private void searchItems(String query) {
        Call<List<Item>> call = ApiClient.retrofitService.searchItems(query);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body();

                    if (!items.isEmpty()) {
                        if (searchAdapter != null) {
                            searchAdapter.updateItems(items);
                        }
                    } else {
                        android.widget.Toast.makeText(
                                MainPageActivity.this,
                                "No items found",
                                android.widget.Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    android.widget.Toast.makeText(
                            MainPageActivity.this,
                            "Error: " + response.message(),
                            android.widget.Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                android.widget.Toast.makeText(
                        MainPageActivity.this,
                        "Failed: " + t.getMessage(),
                        android.widget.Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SEARCH_TEXT", searchEditText.getText().toString());
        outState.putBoolean("HEART1_CHECKED", heartCheckBox1.isChecked());
        outState.putBoolean("HEART2_CHECKED", heartCheckBox2.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchText = savedInstanceState.getString("SEARCH_TEXT");
        isHeart1Checked = savedInstanceState.getBoolean("HEART1_CHECKED");
        isHeart2Checked = savedInstanceState.getBoolean("HEART2_CHECKED");
        searchEditText.setText(searchText);
        heartCheckBox1.setChecked(isHeart1Checked);
        heartCheckBox2.setChecked(isHeart2Checked);
    }

    private void setupDotsIndicator() {
        LinearLayout dotsIndicator = findViewById(R.id.dotsIndicator);
        dots = new ImageView[adList.size()];

        for (int i = 0; i < adList.size(); i++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.rectangle_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            dot.setLayoutParams(params);
            dotsIndicator.addView(dot);
            dots[i] = dot;
        }

        updateDotsIndicator(0);
    }

    private void updateDotsIndicator(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageResource(i == position ? R.drawable.rectangle_active : R.drawable.rectangle_inactive);
        }
    }

    private void setupListeners() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = searchEditText.getText().toString();
            System.out.println("Search text: " + searchText);
            return true;
        });
    }
}

