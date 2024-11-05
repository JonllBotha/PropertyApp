package com.example.ek;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

public class ClientListingFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private FloatingActionButton btnFavouriteListing, backToProfile1;
    private Button btnContactAgent;
    private FloatingActionButton btnMsgAgent1;
    private ImageView propertyImage1;
    private TextView propertyTitle1, propertyLocation1, propertyPrice1, propertyFloors1,
            propertyBaths1, propertyBeds1, propertyDescription1, agentName1, agentDescription1;
    private DBHelper dbHelper;

    public ClientListingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_listing, container, false);

        agentName1 = view.findViewById(R.id.agentName1);
        agentDescription1 = view.findViewById(R.id.agentDescription1);

        backToProfile1 = view.findViewById(R.id.backToProfile1);
        btnFavouriteListing = view.findViewById(R.id.btnFavourite1);
        btnContactAgent = view.findViewById(R.id.btnContactAgent1);
        btnMsgAgent1 = view.findViewById(R.id.btnMsgAgent1);

        // Initialize views
        propertyImage1 = view.findViewById(R.id.propertyImage1);
        propertyTitle1 = view.findViewById(R.id.propertyTitle1);
        propertyLocation1 = view.findViewById(R.id.propertyLocation1);
        propertyPrice1 = view.findViewById(R.id.propertyPrice1);
        propertyFloors1 = view.findViewById(R.id.propertyFloors1);
        propertyBaths1 = view.findViewById(R.id.propertyBaths1);
        propertyBeds1 = view.findViewById(R.id.propertyBeds1);
        propertyDescription1 = view.findViewById(R.id.propertyDescription1);

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
        backToProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ClientListingFragment.this)
                        .navigate(R.id.action_clientListingFragment_to_clientHomeFragment);
            }
        });

        // Contact Agent button click listener
        btnContactAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.getAgentCell().observe(getViewLifecycleOwner(), cell -> {
                    if (cell != null && !cell.isEmpty()) {
                        // Create an Intent to open WhatsApp with the agent's phone number
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + cell));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "WhatsApp not installed or invalid number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Navigate to ClientContactFragment if the agent's phone number is not available
                        Toast.makeText(getContext(), "Agent's phone number not available. Redirecting to enquiry form.", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(ClientListingFragment.this)
                                .navigate(R.id.action_clientListingFragment_to_clientContactFragment);
                    }
                });
            }
        });

        // Contact Agent button click listener
        btnMsgAgent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.getAgentCell().observe(getViewLifecycleOwner(), cell -> {
                    if (cell != null && !cell.isEmpty()) {
                        // Create an Intent to open WhatsApp with the agent's phone number
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + cell));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "WhatsApp not installed or invalid number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Navigate to ClientContactFragment if the agent's phone number is not available
                        Toast.makeText(getContext(), "Agent's phone number not available. Redirecting to enquiry form.", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(ClientListingFragment.this)
                                .navigate(R.id.action_clientListingFragment_to_clientContactFragment);
                    }
                });
            }
        });

        sharedViewModel.getListingID().observe(getViewLifecycleOwner(), listingID -> {
            if (listingID != null) {
                sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), clientEmail -> {
                    if (clientEmail != null) {
                        // Check if the listing is already a favorite
                        boolean isFavourite = dbHelper.isListingFavourite(listingID, clientEmail);
                        if (isFavourite) {
                            // Remove from favorites
                            dbHelper.removeFavourite(listingID, clientEmail);
                            btnFavouriteListing.setForeground(getResources().getDrawable(R.drawable.unfavourite_grey));
                            Toast.makeText(getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                        } else {
                            // Add to favorites
                            dbHelper.addFavourite(listingID, clientEmail);
                            btnFavouriteListing.setForeground(getResources().getDrawable(R.drawable.favourite_red));
                            Toast.makeText(getContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


                        // Favourite button click listener
        btnFavouriteListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.getListingID().observe(getViewLifecycleOwner(), listingID -> {
                    if (listingID != null) {
                        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), clientEmail -> {
                            if (clientEmail != null) {
                                // Check if the listing is already a favorite
                                boolean isFavourite = dbHelper.isListingFavourite(listingID, clientEmail);

                                if (isFavourite) {
                                    // Remove from favorites
                                    dbHelper.removeFavourite(listingID, clientEmail);
                                    btnFavouriteListing.setForeground(getResources().getDrawable(R.drawable.unfavourite_grey));
                                    Toast.makeText(getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Add to favorites
                                    dbHelper.addFavourite(listingID, clientEmail);
                                    btnFavouriteListing.setForeground(getResources().getDrawable(R.drawable.favourite_red));
                                    Toast.makeText(getContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    @SuppressLint("Range")
    private void loadAgentDetails(String email) {
        Cursor cursor = dbHelper.getAgentDetails(email); // Method to fetch agent details by email

        if (cursor != null && cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndex("firstName"));
            String lastName = cursor.getString(cursor.getColumnIndex("lastName"));
            String fullName = firstName + " " + lastName;
            String cell = cursor.getString(cursor.getColumnIndex("phoneNumber"));
            String desc = cursor.getString(cursor.getColumnIndex("about"));

            agentName1.setText(fullName);
            agentDescription1.setText(desc);

            // Save the cell number to SharedViewModel
            sharedViewModel.setAgentCell(cell);
        } else {
            Toast.makeText(getContext(), "Agent data not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }


    @SuppressLint("Range")
    private void loadListingDetails(int listingID) {
        Cursor cursor = dbHelper.getListingDetails(listingID); // Method to fetch listing details by ID

        if (cursor != null && cursor.moveToFirst()) {
            // Assuming your database has corresponding fields
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

            // Save the cell number to SharedViewModel
            sharedViewModel.setAgentEmail(agentEmail);

            // Set the values to the UI components
            propertyTitle1.setText(title);
            propertyLocation1.setText(location);
            propertyPrice1.setText(price);
            propertyFloors1.setText(floors);
            propertyBaths1.setText(String.valueOf(baths));
            propertyBeds1.setText(String.valueOf(beds));
            propertyDescription1.setText(description);

            // Load image
            Glide.with(propertyImage1.getContext())
                    .load(imagePath)
                    .into(propertyImage1);

            cursor.close();
        }
    }
}