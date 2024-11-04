package com.example.ek;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentHomeFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private TextView profileName;
    private RecyclerView rvAgentHome;
    private ClientListingsAdapter clientListingsAdapter;
    private List<ListingItem> listingItems;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agent_home, container, false);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dbHelper = new DBHelper(getContext());

        profileName = view.findViewById(R.id.tv_name);
        rvAgentHome = view.findViewById(R.id.rv_agent_home);
        listingItems = new ArrayList<>();

        // Observe the full name from SharedViewModel
        sharedViewModel.getUserFullName().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                profileName.setText(fullName);
            }
        });

        // Fetch listings for the logged-in agent
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadListings(email); // Load listings based on the agent's email
            }
        });

        // Set up RecyclerView
        clientListingsAdapter = new ClientListingsAdapter(this, listingItems);
        rvAgentHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAgentHome.setAdapter(clientListingsAdapter);

        return view;
    }

    @SuppressLint("Range")
    private void loadListings(String agentEmail) {
        Cursor cursor = dbHelper.getListingData(agentEmail); // Get listings from DB

        if (cursor != null) {
            // Log the column names for debugging
            Log.d("ColumnNames", Arrays.toString(cursor.getColumnNames()));

            if (cursor.moveToFirst()) {
                do {
                    // Make sure the column names below match your actual database schema
                    String imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String city = cursor.getString(cursor.getColumnIndex("city"));
                    String province = cursor.getString(cursor.getColumnIndex("province"));
                    String location = city + ", " + province;
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    int bath = cursor.getInt(cursor.getColumnIndex("bathrooms"));
                    int bed = cursor.getInt(cursor.getColumnIndex("bedrooms"));

                    // Create a ListingItem object and add it to the list
                    ListingItem listingItem = new ListingItem(imagePath, title, location, price, bath, bed);
                    listingItems.add(listingItem);
                } while (cursor.moveToNext());

                cursor.close(); // Close the cursor when done
            } else {
                Log.d("LoadListings", "Cursor is empty");
            }
        } else {
            Log.d("LoadListings", "Cursor is null");
        }

        clientListingsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
    }

}