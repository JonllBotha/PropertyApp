package com.example.ek;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ClientFavouritesFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private RecyclerView rvFavourites;
    private ClientListingsAdapter clientListingsAdapter;
    private List<ListingItem> clientListingItems;
    private DBHelper dbHelper;

    public ClientFavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_favourites, container, false);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        dbHelper = new DBHelper(getContext());
        rvFavourites = view.findViewById(R.id.rv_favourites);
        clientListingItems = new ArrayList<>();

        // Set up RecyclerView
        clientListingsAdapter = new ClientListingsAdapter(ClientFavouritesFragment.this, clientListingItems, new ClientListingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListingItem item) {
                // Handle item click here
            }
        });
        rvFavourites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavourites.setAdapter(clientListingsAdapter);

        // Observe the client's profile email and load favourite listings when available
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadFavourites(email); // Load listings based on the client's email
            }
        });

        return view;
    }

    @SuppressLint("Range")
    private void loadFavourites(String email) {
        // Get the favorite listings for the given client email
        Cursor favouriteListingsCursor = dbHelper.getFavouriteListings(email);
        clientListingItems.clear(); // Clear the existing list to avoid duplicates

        if (favouriteListingsCursor != null && favouriteListingsCursor.moveToFirst()) {
            do {
                int listingID = favouriteListingsCursor.getInt(favouriteListingsCursor.getColumnIndex("listing_id"));
                String imagePath = favouriteListingsCursor.getString(favouriteListingsCursor.getColumnIndex("image_path"));
                String title = favouriteListingsCursor.getString(favouriteListingsCursor.getColumnIndex("title"));
                String listingCity = favouriteListingsCursor.getString(favouriteListingsCursor.getColumnIndex("city"));
                String listingProvince = favouriteListingsCursor.getString(favouriteListingsCursor.getColumnIndex("province"));
                String location = listingCity + ", " + listingProvince;
                String price = favouriteListingsCursor.getString(favouriteListingsCursor.getColumnIndex("price"));
                int bath = favouriteListingsCursor.getInt(favouriteListingsCursor.getColumnIndex("bathrooms"));
                int bed = favouriteListingsCursor.getInt(favouriteListingsCursor.getColumnIndex("bedrooms"));

                // Create a ListingItem object and add it to the list
                ListingItem listingItem = new ListingItem(listingID, imagePath, title, location, price, bath, bed);
                clientListingItems.add(listingItem);
            } while (favouriteListingsCursor.moveToNext());

            favouriteListingsCursor.close(); // Close the cursor after use
        } else {
            Log.d("LoadFavourites", "No favourite listings found for this client.");
        }

        clientListingsAdapter.notifyDataSetChanged(); // Refresh the adapter with new data
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close(); // Close the database helper to avoid memory leaks
        }
    }
}
