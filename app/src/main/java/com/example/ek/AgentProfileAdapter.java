package com.example.ek;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AgentProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    private final Fragment fragment;  // Store fragment instance
    private final List<ProfileItem> items;
    private final String email;
    private final DBHelper dbHelper;

    public AgentProfileAdapter(Fragment fragment, List<ProfileItem> items, String email) {
        this.fragment = fragment;
        this.items = items;
        this.email = email;
        this.dbHelper = new DBHelper(fragment.getContext());
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
                case 1: // "Privacy Policy"
                    navController.navigate(R.id.action_agentProfileFragment_to_privacyPolicyFragment);
                    break;
                case 2: // "Log Out"
                    navController.navigate(R.id.action_agentProfileFragment_to_startupFragment);
                    break;
                case 3: // "Delete Account"
                    confirmAndDeleteAccount();
                    break;
            }
        });
    }

    private void confirmAndDeleteAccount() {
        // Retrieve email from SharedViewModel
        SharedViewModel sharedViewModel = new ViewModelProvider(fragment.requireActivity()).get(SharedViewModel.class);
        String email = sharedViewModel.getProfileEmail().getValue();

        if (email != null) {
            // Confirm deletion
            new AlertDialog.Builder(fragment.getContext())
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete account from the database
                        boolean isDeleted = dbHelper.deleteAccount(email);
                        if (isDeleted) {
                            Toast.makeText(fragment.getContext(), "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                            // Navigate to startup screen
                            NavController navController = Navigation.findNavController(fragment.getActivity(), R.id.navHostFragmentContainerView);
                            navController.navigate(R.id.action_agentProfileFragment_to_startupFragment);
                        } else {
                            Toast.makeText(fragment.getContext(), "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            Toast.makeText(fragment.getContext(), "Error: Unable to retrieve account information.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
