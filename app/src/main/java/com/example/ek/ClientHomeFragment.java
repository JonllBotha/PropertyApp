package com.example.ek;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHomeFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private RecyclerView rvClientHome;
    private ClientListingsAdapter clientListingsAdapter;
    private List<ListingItem> listingItems;
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

        TextView tvNearby = view.findViewById(R.id.tv_Nearby);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dbHelper = new DBHelper(getContext());

        rvClientHome = view.findViewById(R.id.rv_client_home);
        listingItems = new ArrayList<>();

        // Fetch listings for the logged-in client
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadListings(email, tvNearby); // Load listings based on the client's email
            }
        });

        // Set up RecyclerView
        clientListingsAdapter = new ClientListingsAdapter(listingItems);
        rvClientHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClientHome.setAdapter(clientListingsAdapter);

        return view;
    }

    @SuppressLint("Range")
    private void loadListings(String clientEmail, TextView tvNearby) {
        Cursor clientCursor = dbHelper.getClientLocation(clientEmail); // Get location from DB

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
                        String imagePath = listingCursor.getString(listingCursor.getColumnIndex("image_path"));
                        String title = listingCursor.getString(listingCursor.getColumnIndex("title"));
                        String listingCity = listingCursor.getString(listingCursor.getColumnIndex("city"));
                        String listingProvince = listingCursor.getString(listingCursor.getColumnIndex("province"));
                        String location = listingCity + ", " + listingProvince;
                        String price = listingCursor.getString(listingCursor.getColumnIndex("price"));
                        int bath = listingCursor.getInt(listingCursor.getColumnIndex("bathrooms"));
                        int bed = listingCursor.getInt(listingCursor.getColumnIndex("bedrooms"));

                        ListingItem listingItem = new ListingItem(imagePath, title, location, price, bath, bed);
                        listingItems.add(listingItem);
                    } while (listingCursor.moveToNext());
                    listingCursor.close(); // Close the cursor when done
                } else {
                    Log.d("LoadListings", "No listings found for this location");
                    tvNearby.setText("Popular"); // Change TextView to "Popular"

                    // Load all listings as a fallback
                    Cursor allListingsCursor = dbHelper.getAllListings();
                    if (allListingsCursor != null && allListingsCursor.moveToFirst()) {
                        do {
                            String imagePath = allListingsCursor.getString(allListingsCursor.getColumnIndex("image_path"));
                            String title = allListingsCursor.getString(allListingsCursor.getColumnIndex("title"));
                            String listingCity = allListingsCursor.getString(allListingsCursor.getColumnIndex("city"));
                            String listingProvince = allListingsCursor.getString(allListingsCursor.getColumnIndex("province"));
                            String location = listingCity + ", " + listingProvince;
                            String price = allListingsCursor.getString(allListingsCursor.getColumnIndex("price"));
                            int bath = allListingsCursor.getInt(allListingsCursor.getColumnIndex("bathrooms"));
                            int bed = allListingsCursor.getInt(allListingsCursor.getColumnIndex("bedrooms"));

                            ListingItem listingItem = new ListingItem(imagePath, title, location, price, bath, bed);
                            listingItems.add(listingItem);
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
