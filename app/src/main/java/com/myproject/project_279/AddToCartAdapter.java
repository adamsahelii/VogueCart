package com.myproject.project_279;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.CartViewHolder> {
    private List<Item> cartItems;
    private Context context;
    private OnRemoveFromCartClickListener onRemoveFromCartClick;

    public interface OnRemoveFromCartClickListener {
        void onRemoveFromCartClick(Item item);
    }

    public AddToCartAdapter(List<Item> cartItems, Context context, OnRemoveFromCartClickListener onRemoveFromCartClick) {
        this.cartItems = cartItems;
        this.context = context;
        this.onRemoveFromCartClick = onRemoveFromCartClick;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        ImageView itemImage;
        Button removeButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemImage = itemView.findViewById(R.id.item_image);
            removeButton = itemView.findViewById(R.id.remove_from_cart_button);
        }

        public void bind(Item item) {
            itemName.setText(item.getName());
            itemPrice.setText("$" + item.getPrice());

            String imageUrl = "http://10.0.2.2:8000" + item.getImageUrl();
            Glide.with(context)
                    .load(imageUrl)
                    .into(itemImage);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRemoveFromCartClick.onRemoveFromCartClick(item);
                }
            });
        }
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}

