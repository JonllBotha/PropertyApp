package com.example.ek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.bumptech.glide.Glide;

public class AgentListingsAdapter extends RecyclerView.Adapter<ListingViewHolder>{

    private final List<ListingItem> listingItems;

    public AgentListingsAdapter(List<ListingItem> listingItems) {
        this.listingItems = listingItems;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_dashboard_item_view, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        ListingItem listingItem = listingItems.get(position);

        // Set the data to the views
        holder.propertyTitle.setText(listingItem.getTitle());
        holder.propertyLocation.setText(listingItem.getLocation());
        holder.propertyPrice.setText(listingItem.getPrice());
        holder.propertyBaths.setText(String.valueOf(listingItem.getBath()));
        holder.propertyBeds.setText(String.valueOf(listingItem.getBed()));

        // Load image using Glide
        Glide.with(holder.propertyImage.getContext())
                .load(listingItem.getImage())
                .into(holder.propertyImage);
    }

    @Override
    public int getItemCount() {
        return listingItems.size();
    }
}
