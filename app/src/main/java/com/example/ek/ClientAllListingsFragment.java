package com.example.ek;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ClientAllListingsFragment extends Fragment implements ClientListingsAdapter.OnItemClickListener {

    private SharedViewModel sharedViewModel;
    private RecyclerView rvAllListings;
    private FloatingActionButton backToHome;
    private ClientListingsAdapter clientListingsAdapter;
    private List<ListingItem> listingItems;
    private DBHelper dbHelper;
    private TextView tv_intent, tv_showing;

    public ClientAllListingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_all_listings, container, false);

        tv_showing = view.findViewById(R.id.tv_showing);
        tv_intent = view.findViewById(R.id.tv_intent);

        // Initialize SharedViewModel and DBHelper
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dbHelper = new DBHelper(getContext());

        backToHome = view.findViewById(R.id.backToHome);
        rvAllListings = view.findViewById(R.id.rv_all_listings);
        listingItems = new ArrayList<>();
        clientListingsAdapter = new ClientListingsAdapter(this, listingItems, this);

        // Set up RecyclerView
        rvAllListings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAllListings.setAdapter(clientListingsAdapter);

        // Observe the shared view model for filters
        sharedViewModel.getSelectedCity().observe(getViewLifecycleOwner(), city -> {
            sharedViewModel.getSelectedIntent().observe(getViewLifecycleOwner(), intent -> {
                sharedViewModel.getSelectedType().observe(getViewLifecycleOwner(), type -> {
                    // Set the tv_intent text based on the intent value
                    if ("Sell".equalsIgnoreCase(intent)) {
                        tv_intent.setText(R.string.properties_for_sale);
                    } else if ("Rent".equalsIgnoreCase(intent)) {
                        tv_intent.setText(R.string.properties_for_rent);
                    }

                    // Use city, intent, and type to filter the database query
                    Cursor filteredListingsCursor = dbHelper.getFilteredListings(city, intent, type);
                    if (filteredListingsCursor != null && filteredListingsCursor.moveToFirst()) {
                        @SuppressLint("Range") String province = filteredListingsCursor.getString(filteredListingsCursor.getColumnIndex("province"));
                        String showingText = String.format("Showing %s in %s, %s", type, city, province);
                        tv_showing.setText(showingText);
                    }
                    loadFilteredListings(filteredListingsCursor);
                });
            });
        });

        // Back button click listener
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(ClientAllListingsFragment.this)
                        .navigate(R.id.action_clientAllListingsFragment_to_clientHomeFragment);
            }
        });

        return view;
    }

    @SuppressLint("Range")
    private void loadFilteredListings(Cursor cursor) {
        listingItems.clear(); // Clear the current list

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int listingID = cursor.getInt(cursor.getColumnIndex("listing_id"));
                String imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String listingCity = cursor.getString(cursor.getColumnIndex("city"));
                String listingProvince = cursor.getString(cursor.getColumnIndex("province"));
                String location = listingCity + ", " + listingProvince;
                String price = cursor.getString(cursor.getColumnIndex("price"));
                int bath = cursor.getInt(cursor.getColumnIndex("bathrooms"));
                int bed = cursor.getInt(cursor.getColumnIndex("bedrooms"));

                // Create a ListingItem object and add it to the list
                ListingItem listingItem = new ListingItem(listingID, imagePath, title, location, price, bath, bed);
                listingItems.add(listingItem);
            } while (cursor.moveToNext());

            cursor.close(); // Close the cursor after use
        }

        clientListingsAdapter.notifyDataSetChanged(); // Notify the adapter of data change
    }

    @Override
    public void onItemClick(ListingItem item) {
        // Handle item click and navigate
        sharedViewModel.setListingID(item.getListingID());
        NavHostFragment.findNavController(ClientAllListingsFragment.this)
                .navigate(R.id.action_clientAllListingsFragment_to_clientListingFragment);
    }
}
