package com.myproject.project_279;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdsPagerAdapter extends RecyclerView.Adapter<AdsPagerAdapter.AdsViewHolder> {
    private List<Integer> adList;

    public AdsPagerAdapter(List<Integer> adList) {
        this.adList = adList;
    }

    public static class AdsViewHolder extends RecyclerView.ViewHolder {
        ImageView adImageView;

        public AdsViewHolder(View itemView) {
            super(itemView);
            adImageView = itemView.findViewById(R.id.adImageView);
        }
    }

    @Override
    public AdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, parent, false);
        return new AdsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdsViewHolder holder, int position) {
        holder.adImageView.setImageResource(adList.get(position));
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }
}

