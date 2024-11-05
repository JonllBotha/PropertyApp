package com.example.ek;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AgentListingFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private FloatingActionButton btnDeleteListing, backToHome2, btnEditProfile;
    private Button btnEditListing;
    private ImageView propertyImage2;
    private TextView propertyTitle2, propertyLocation2, propertyPrice2, propertyFloors2,
            propertyBaths2, propertyBeds2, propertyDescription2, agentName2, agentDescription2;
    private DBHelper dbHelper;

    public AgentListingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agent_listing, container, false);

        agentName2 = view.findViewById(R.id.agentName2);
        agentDescription2 = view.findViewById(R.id.agentDescription2);

        backToHome2 = view.findViewById(R.id.backToHome2);
        btnDeleteListing = view.findViewById(R.id.btnDeleteListing);
        btnEditListing = view.findViewById(R.id.btnEditListing);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Initialize property views
        propertyImage2 = view.findViewById(R.id.propertyImage2);
        propertyTitle2 = view.findViewById(R.id.propertyTitle2);
        propertyLocation2 = view.findViewById(R.id.propertyLocation2);
        propertyPrice2 = view.findViewById(R.id.propertyPrice2);
        propertyFloors2 = view.findViewById(R.id.propertyFloors2);
        propertyBaths2 = view.findViewById(R.id.propertyBaths2);
        propertyBeds2 = view.findViewById(R.id.propertyBeds2);
        propertyDescription2 = view.findViewById(R.id.propertyDescription2);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dbHelper = new DBHelper(getContext());

        // Observe listing ID and fetch details
        sharedViewModel.getListingID().observe(getViewLifecycleOwner(), listingID -> {
            if (listingID != null) {
                loadListingDetails(listingID);
            }
        });

        // Fetch listings for the logged-in agent
        sharedViewModel.getAgentEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadAgentDetails(email); // Load agent details based on the agent's email
            }
        });

        // Back button click listener
        backToHome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(AgentListingFragment.this)
                        .navigate(R.id.action_agentListingFragment_to_agentHomeFragment);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(AgentListingFragment.this)
                        .navigate(R.id.action_agentListingFragment_to_agentEditProfileFragment);
            }
        });

        // Edit Listing button click listener
        btnEditListing.setOnClickListener(v -> showEditConfirmationDialog());

        // Delete button click listener
        btnDeleteListing.setOnClickListener(v -> showDeleteConfirmationDialog());

        return view;
    }

    private void showEditConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Edit Listing")
                .setMessage("Are you sure you want to edit this listing?")
                .setPositiveButton("Yes", (dialog, which) ->
                        NavHostFragment.findNavController(AgentListingFragment.this)
                                .navigate(R.id.action_agentListingFragment_to_publishAdFragment)
                )
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show(); // Display the dialog
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Listing")
                .setMessage("Are you sure you want to delete this listing?")
                .setPositiveButton("Yes", (dialog, which) -> deleteListing())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show(); // Display the dialog
    }

    private void deleteListing() {
        sharedViewModel.getListingID().observe(getViewLifecycleOwner(), listingID -> {
            if (listingID != null) {
                // Call the delete method
                dbHelper.deleteListing(listingID);
                Toast.makeText(getContext(), "Listing deleted", Toast.LENGTH_SHORT).show();
                // Navigate back
                NavHostFragment.findNavController(AgentListingFragment.this)
                        .navigate(R.id.action_agentListingFragment_to_agentHomeFragment);
            }
        });
    }

    @SuppressLint("Range")
    private void loadAgentDetails(String email) {
        Cursor cursor = dbHelper.getAgentDetailsByEmail(email);

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndex("firstName"));
            String lastName = cursor.getString(cursor.getColumnIndex("lastName"));
            String fullName = firstName + " " + lastName;
            String desc = cursor.getString(cursor.getColumnIndex("about"));

            agentName2.setText(fullName);
            agentDescription2.setText(desc);
        } else {
            Toast.makeText(getContext(), "Agent data not found", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) cursor.close();
    }

    @SuppressLint("Range")
    private void loadListingDetails(int listingID) {
        Cursor cursor = dbHelper.getListingDetails(listingID);

        if (cursor != null && cursor.moveToFirst()) {
            String imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String province = cursor.getString(cursor.getColumnIndex("province"));
            String location = city + ", " + province;
            String price = cursor.getString(cursor.getColumnIndex("price"));
            String floors = cursor.getString(cursor.getColumnIndex("floors"));
            int baths = cursor.getInt(cursor.getColumnIndex("bathrooms"));
            int beds = cursor.getInt(cursor.getColumnIndex("bedrooms"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String agentEmail = cursor.getString(cursor.getColumnIndex("agent_email"));

            // Save listing details to SharedViewModel
            sharedViewModel.setAgentEmail(agentEmail);

            // Set the values to the UI components
            propertyTitle2.setText(title);
            propertyLocation2.setText(location);
            propertyPrice2.setText(price);
            propertyFloors2.setText(floors);
            propertyBaths2.setText(String.valueOf(baths));
            propertyBeds2.setText(String.valueOf(beds));
            propertyDescription2.setText(description);

            // Load image
            Glide.with(propertyImage2.getContext())
                    .load(imagePath)
                    .into(propertyImage2);

            cursor.close();
        }
    }
}
