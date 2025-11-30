package com.myproject.project_279;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Item> items;
    private final OnAddToCartClickListener onAddToCartClicked;

    public interface OnAddToCartClickListener {
        void onAddToCartClicked(Item item);
    }

    public SearchAdapter(List<Item> items, OnAddToCartClickListener onAddToCartClicked) {
        this.items = items;
        this.onAddToCartClicked = onAddToCartClicked;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fashion, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Item item = items.get(position);

        holder.itemNameTextView.setText(item.getName());
        holder.itemPriceTextView.setText("$" + item.getPrice());

        String imageUrl = "http://10.0.2.2:8000" + item.getImageUrl();

        Glide.with(holder.itemImageView.getContext())
                .load(imageUrl)
                .into(holder.itemImageView);

        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCartClicked.onAddToCartClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<Item> newItems) {
        items = newItems;
        notifyDataSetChanged();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemPriceTextView;
        ImageView itemImageView;
        Button addToCartButton;

        public SearchViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemName);
            itemPriceTextView = itemView.findViewById(R.id.itemPrice);
            itemImageView = itemView.findViewById(R.id.itemImg);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}

