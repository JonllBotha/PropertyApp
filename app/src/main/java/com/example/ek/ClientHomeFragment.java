package com.example.ek;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHomeFragment extends Fragment implements ClientListingsAdapter.OnItemClickListener{

    private SharedViewModel sharedViewModel;
    private RecyclerView rvClientHome;
    private RadioButton rbHomes, rbFlats, rbPlots;
    private Button btnFindAllListings;
    private AutoCompleteTextView autoCompleteLocation;
    private TabLayout tabLayout;
    private TabLayout.Tab tiRent, tiBuy;
    private TextView tvNearby;
    private ClientListingsAdapter clientListingsAdapter;
    private List<ListingItem> clientListingItems;
    private DBHelper dbHelper;

    public ClientHomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_home, container, false);

        tvNearby = view.findViewById(R.id.tv_Nearby);
        btnFindAllListings = view.findViewById(R.id.btnFindAllListings);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Fetch listings for the logged-in client
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadListings(email, tvNearby); // Load listings based on the client's email
            }
        });

        dbHelper = new DBHelper(getContext());

        rvClientHome = view.findViewById(R.id.rv_client_home);
        clientListingItems = new ArrayList<>();

        rbHomes = view.findViewById(R.id.btnHomes);
        rbFlats = view.findViewById(R.id.btnFlats);
        rbPlots  = view.findViewById(R.id.btnPlots);

        tabLayout = view.findViewById(R.id.propertyIntentTabLayout);
        tiBuy = tabLayout.getTabAt(0);
        tiRent = tabLayout.getTabAt(1);

        autoCompleteLocation = view.findViewById(R.id.City);
        String[] cities = getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, cities);
        autoCompleteLocation.setAdapter(adapter);

        // Set up RecyclerView
        clientListingsAdapter = new ClientListingsAdapter(this, clientListingItems, this);
        rvClientHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClientHome.setAdapter(clientListingsAdapter);

        // Find button click listener
        btnFindAllListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking if the agent intends to rent or sell
                String propertyType = "Homes";
                if (rbHomes.isChecked()) {
                    propertyType = "Homes";
                } else if (rbFlats.isChecked()) {
                    propertyType = "Flats";
                } else if (rbPlots.isChecked()) {
                    propertyType = "Plots";
                }

                // Checking what the listing type is
                String propertyIntent ;
                if (tiBuy != null && tiBuy.getText() != null)
                {
                    propertyIntent = "Sell";
                }
                else if (tiRent != null && tiRent.getText() != null)
                {
                    propertyIntent = "Rent";
                }
                else {propertyIntent = "Sell";
                }

                String selectedCity = autoCompleteLocation.getText().toString();

                sharedViewModel.setSelectedCity(selectedCity);
                sharedViewModel.setSelectedIntent(propertyIntent);
                sharedViewModel.setSelectedType(propertyType);

                NavHostFragment.findNavController(ClientHomeFragment.this)
                        .navigate(R.id.action_clientHomeFragment_to_clientAllListingsFragment);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the RecyclerView data when the fragment comes back to the foreground
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadListings(email, tvNearby); // Reload listings based on the client's email
            }
        });
    }

    // Implement the OnItemClickListener method for RecyclerView
    @Override
    public void onItemClick(ListingItem item) {
        sharedViewModel.setListingID(item.getListingID()); // Store the selected listing ID
        // Navigate to ClientListingFragment
        NavHostFragment.findNavController(ClientHomeFragment.this)
                .navigate(R.id.action_clientHomeFragment_to_clientListingFragment);
    }

    @SuppressLint("Range")
    private void loadListings(String email, TextView tvNearby) {
        Cursor clientCursor = dbHelper.getClientLocation(email); // Get location from DB

        if (clientCursor != null && clientCursor.moveToFirst()) {
            // Retrieve the city and province for the client
            String city = clientCursor.getString(clientCursor.getColumnIndex("city"));
            String province = clientCursor.getString(clientCursor.getColumnIndex("province"));
            clientCursor.close(); // Close cursor after use

            Cursor listingCursor = dbHelper.getListingsByLocation(city, province); // Get listings from DB

            if (listingCursor != null) {
                Log.d("ColumnNames", Arrays.toString(listingCursor.getColumnNames()));

                if (listingCursor.moveToFirst()) {
                    do {
                        int listingID = listingCursor.getInt(listingCursor.getColumnIndex("listing_id"));
                        String imagePath = listingCursor.getString(listingCursor.getColumnIndex("image_path"));
                        String title = listingCursor.getString(listingCursor.getColumnIndex("title"));
                        String listingCity = listingCursor.getString(listingCursor.getColumnIndex("city"));
                        String listingProvince = listingCursor.getString(listingCursor.getColumnIndex("province"));
                        String location = listingCity + ", " + listingProvince;
                        String price = listingCursor.getString(listingCursor.getColumnIndex("price"));
                        int bath = listingCursor.getInt(listingCursor.getColumnIndex("bathrooms"));
                        int bed = listingCursor.getInt(listingCursor.getColumnIndex("bedrooms"));

                        // Create a ListingItem object and add it to the list
                        ListingItem listingItem = new ListingItem(listingID, imagePath, title, location, price, bath, bed);
                        clientListingItems.add(listingItem);
                    } while (listingCursor.moveToNext());
                    listingCursor.close(); // Close the cursor when done
                } else {
                    Log.d("LoadListings", "No listings found for this location");
                    tvNearby.setText("Popular"); // Change TextView to "Popular"

                    // Load all listings as a fallback
                    Cursor allListingsCursor = dbHelper.getAllListings();
                    if (allListingsCursor != null && allListingsCursor.moveToFirst()) {
                        do {
                            int listingID = allListingsCursor.getInt(allListingsCursor.getColumnIndex("listing_id"));
                            String imagePath = allListingsCursor.getString(allListingsCursor.getColumnIndex("image_path"));
                            String title = allListingsCursor.getString(allListingsCursor.getColumnIndex("title"));
                            String listingCity = allListingsCursor.getString(allListingsCursor.getColumnIndex("city"));
                            String listingProvince = allListingsCursor.getString(allListingsCursor.getColumnIndex("province"));
                            String location = listingCity + ", " + listingProvince;
                            String price = allListingsCursor.getString(allListingsCursor.getColumnIndex("price"));
                            int bath = allListingsCursor.getInt(allListingsCursor.getColumnIndex("bathrooms"));
                            int bed = allListingsCursor.getInt(allListingsCursor.getColumnIndex("bedrooms"));

                            ListingItem listingItem = new ListingItem(listingID, imagePath, title, location, price, bath, bed);
                            clientListingItems.add(listingItem);
                        } while (allListingsCursor.moveToNext());

                        allListingsCursor.close();
                    }
                }
            } else {
                Log.d("LoadListings", "Failed to retrieve client location");
            }
            clientListingsAdapter.notifyDataSetChanged();
        }
    }


}
