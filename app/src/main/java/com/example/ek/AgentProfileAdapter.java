package com.example.ek;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AgentProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    private final Fragment fragment;  // Store fragment instance
    private final List<ProfileItem> items;
    private final String email;

    public AgentProfileAdapter(Fragment fragment, List<ProfileItem> items, String email) {
        this.fragment = fragment;
        this.items = items;
        this.email = email;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(fragment.getContext()).inflate(R.layout.profile_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getItem());
        holder.imageView.setImageResource(items.get(position).getImage());
        holder.arrow.setImageResource(items.get(position).getArrow());

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
