package com.example.ek;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;  // Add this import for Fragment
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClientProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    private final Fragment fragment;  // Store fragment instance
    private final List<ProfileItem> items;
    private final String email;

    public ClientProfileAdapter(Fragment fragment, List<ProfileItem> items, String email) {
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
                    // Create a Bundle to pass the email
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);  // Pass the email to the next fragment
                    navController.navigate(R.id.action_clientProfileFragment_to_clientEditProfileFragment,bundle);
                    break;
                case 1: // "Bond Calculator"
                    //navController.navigate(R.id.action_clientProfileFragment_to_bondCalculatorFragment);
                    break;
                case 2: // "Settings"
                    //navController.navigate(R.id.action_clientProfileFragment_to_settingsFragment);
                    break;
                case 3: // "Privacy Policy"
                    //navController.navigate(R.id.action_clientProfileFragment_to_privacyPolicyFragment);
                    break;
                case 4: // "Log Out"
                    navController.navigate(R.id.action_clientProfileFragment_to_startupFragment);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
