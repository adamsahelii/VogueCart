package com.myproject.project_279;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ProductPageActivity extends AppCompatActivity {

    private Button activeTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_page);

        TextView productContent = findViewById(R.id.productContent);
        TextView quantityText = findViewById(R.id.quantityText);
        final int[] quantity = {1};
        Button descriptionTab = findViewById(R.id.descriptionTab);
        Button ingredientsTab = findViewById(R.id.ingredientsTab);

        activeTab = descriptionTab;
        setTabActive(descriptionTab);

        descriptionTab.setOnClickListener(v -> {
            productContent.setText("La French Rose - Moisturizing Hand & Nail Crème is a luxurious blend designed to deeply hydrate and rejuvenate your hands and nails with the enchanting essence of French rose. This lightweight, non-greasy formula absorbs quickly, delivering essential nourishment to dry skin and cuticles while strengthening nails and leaving your hands feeling soft, smooth, and refreshed. Enriched with antioxidants, it helps reduce signs of aging, promoting a youthful glow, and its delicate floral fragrance provides an uplifting, sensory experience with every application. Perfectly sized at 30 mL (1.0 fl. oz), it's an ideal companion for on-the-go hydration and self-care indulgence.");
            setTabActive(descriptionTab);
        });

        ingredientsTab.setOnClickListener(v -> {
            productContent.setText("Aqua (Water), Glycerin, Rosa Damascena Flower Extract, Shea Butter, Cetearyl Alcohol, Sweet Almond Oil, Tocopherol (Vitamin E), Panthenol (Provitamin B5), Allantoin, Carbomer, Phenoxyethanol, Fragrance (Parfum), Citric Acid, Sodium Hydroxide, Dimethicone, Stearic Acid, Ethylhexylglycerin, Caprylyl Glycol, Limonene, Linalool, Benzyl Alcohol, Citral, Geraniol, Hexyl Cinnamal, Benzyl Salicylate, CI 77288 (Chromium Oxide Green), CI 15985 (Yellow 6 Lake), CI 45380 (Red 27 Lake).");
            setTabActive(ingredientsTab);
        });

        findViewById(R.id.decrementButton).setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityText.setText(String.valueOf(quantity[0]));
            }
        });
        findViewById(R.id.incrementButton).setOnClickListener(v -> {
            quantity[0]++;
            quantityText.setText(String.valueOf(quantity[0]));
        });

        findViewById(R.id.backButton).setOnClickListener(v -> {
            startActivity(new Intent(this, MainPageActivity.class));
        });
        findViewById(R.id.addToCartButton).setOnClickListener(v -> {
            startActivity(new Intent(this, CartFragment.class));
        });
    }

    private void setTabActive(Button selectedTab) {
        activeTab.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_orange));
        selectedTab.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.orange));
        activeTab = selectedTab;
    }
}

