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

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<Item> itemList;
    private Context context;
    private OnFavoriteClickListener onFavoriteClick;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Item item);
    }

    public FavoritesAdapter(List<Item> itemList, Context context, OnFavoriteClickListener onFavoriteClick) {
        this.itemList = itemList;
        this.context = context;
        this.onFavoriteClick = onFavoriteClick;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        ImageView itemImage;
        Button removeButton;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemImage = itemView.findViewById(R.id.item_image);
            removeButton = itemView.findViewById(R.id.remove_from_favorites_button);
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
                    onFavoriteClick.onFavoriteClick(item);
                }
            });
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

