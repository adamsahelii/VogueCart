package com.myproject.project_279;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CartViewHolder> {
    private ArrayList<Item> cartItems;
    private Context context;
    private OnQuantityChangedListener onQuantityChanged;
    private DecimalFormat priceFormat = new DecimalFormat("#.##");

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }

    public CheckoutAdapter(ArrayList<Item> cartItems, Context context, OnQuantityChangedListener onQuantityChanged) {
        this.cartItems = cartItems;
        this.context = context;
        this.onQuantityChanged = onQuantityChanged;
    }

    private double calculateTotalPrice() {
        double total = 0.0;
        for (Item item : cartItems) {
            try {
                total += Double.parseDouble(item.getPrice()) * item.getQuantity();
            } catch (NumberFormatException e) {
                // Handle invalid price format
            }
        }
        return total;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Item cartItem = cartItems.get(position);
        holder.productName.setText(cartItem.getName());
        
        try {
            double price = Double.parseDouble(cartItem.getPrice());
            holder.productPrice.setText("$" + priceFormat.format(price));
        } catch (NumberFormatException e) {
            holder.productPrice.setText("$" + cartItem.getPrice());
        }

        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        String imageUrl = "http://10.0.2.2:8000" + cartItem.getImageUrl();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.productImage);

        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
                onQuantityChanged.onQuantityChanged();
                notifyItemChanged(position);
            }
        });

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
                    onQuantityChanged.onQuantityChanged();
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView quantityText;
        ImageButton increaseButton;
        ImageButton decreaseButton;
        ImageView productImage;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            quantityText = itemView.findViewById(R.id.product_quantity);
            increaseButton = itemView.findViewById(R.id.increase_quantity);
            decreaseButton = itemView.findViewById(R.id.decrease_quantity);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}

