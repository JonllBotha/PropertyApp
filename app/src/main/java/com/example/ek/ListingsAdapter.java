package com.example.ek;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingViewHolder>{
    private final Fragment fragment;  // Store fragment instance
    private final List<ListingItem> items;
    private final String email;

    public ListingsAdapter(Fragment fragment, List<ListingItem> items, String email) {
        this.fragment = fragment;
        this.items = items;
        this.email = email;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListingViewHolder(LayoutInflater.from(fragment.getContext()).inflate(R.layout.client_dashboard_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        holder.propertyImage.setImageResource(items.get(position).getImage());
        holder.propertyTitle.setText(items.get(position).getTitle());
        holder.propertyLocation.setText(items.get(position).getLocation());
        holder.propertyPrice.setText(items.get(position).getPrice());
        holder.propertyBaths.setText(items.get(position).getBath());
        holder.propertyBeds.setText(items.get(position).getBed());

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(fragment.getActivity(), R.id.navHostFragmentContainerView);

            switch (position) {
                case 0: // "Your Profile"
                    navController.navigate(R.id.action_agentProfileFragment_to_agentEditProfileFragment);
                    break;
                case 1: // "Settings"
                    //navController.navigate(R.id.action_clientProfileFragment_to_clientSettingsFragment);
                    break;
                case 2: // "Log Out"
                    navController.navigate(R.id.action_agentProfileFragment_to_startupFragment);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
