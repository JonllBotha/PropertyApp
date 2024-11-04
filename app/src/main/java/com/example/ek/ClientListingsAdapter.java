package com.example.ek;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ClientListingsAdapter extends RecyclerView.Adapter<ClientListingViewHolder>{

    private final Fragment fragment;
    private final List<ListingItem> listingItems;
    private final OnItemClickListener listener;

    // Define an interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(ListingItem item);
    }

    public ClientListingsAdapter(Fragment fragment, List<ListingItem> listingItems, ClientListingsAdapter.OnItemClickListener listener) {
        this.fragment = fragment;
        this.listingItems = listingItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClientListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientListingViewHolder(LayoutInflater.from(fragment.getContext()).inflate(R.layout.client_dashboard_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListingViewHolder holder, int position) {
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

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> listener.onItemClick(listingItem));
    }

    @Override
    public int getItemCount() {
        return listingItems.size();
    }
}
