package com.example.ek;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ClientListingsAdapter extends RecyclerView.Adapter<AgentListingViewHolder>{

    private final Fragment fragment;
    private final List<ListingItem> listingItems;

    public ClientListingsAdapter(Fragment fragment, List<ListingItem> listingItems) {
        this.fragment = fragment;
        this.listingItems = listingItems;
    }

    @NonNull
    @Override
    public AgentListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgentListingViewHolder(LayoutInflater.from(fragment.getContext()).inflate(R.layout.client_dashboard_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AgentListingViewHolder holder, int position) {
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
