package com.example.ek;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AgentHomeFragment extends Fragment implements AgentListingsAdapter.OnItemClickListener{

    private SharedViewModel sharedViewModel;
    private TextView profileName;
    private RecyclerView rvAgentHome;
    private AgentListingsAdapter agentListingsAdapter;
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
                profileName.setText(new StringBuilder().append(fullName).append(",").toString());
            }
        });

        // Fetch listings for the logged-in agent
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                loadListings(email); // Load listings based on the agent's email
            }
        });

        // Set up RecyclerView
        rvAgentHome.setLayoutManager(new LinearLayoutManager(getContext()));
        agentListingsAdapter = new AgentListingsAdapter(this, listingItems, this);
        rvAgentHome.setAdapter(agentListingsAdapter);

        return view;
    }

    // Implement the OnItemClickListener method for RecyclerView
    @Override
    public void onItemClick(ListingItem item) {
        sharedViewModel.setListingID(item.getListingID()); // Store the selected listing ID
        // Navigate to AgentListingFragment
        NavHostFragment.findNavController(AgentHomeFragment.this)
                .navigate(R.id.action_agentHomeFragment_to_agentListingFragment);
    }

    @SuppressLint("Range")
    private void loadListings(String agentEmail) {
        Cursor cursor = dbHelper.getListingData(agentEmail); // Get listings from DB

        if (cursor != null) {
            Log.d("ColumnNames", Arrays.toString(cursor.getColumnNames()));

            if (cursor.moveToFirst()) {
                do {
                    int listingID = cursor.getInt(cursor.getColumnIndex("listing_id"));
                    String imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String city = cursor.getString(cursor.getColumnIndex("city"));
                    String province = cursor.getString(cursor.getColumnIndex("province"));
                    String location = city + ", " + province;
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    int bath = cursor.getInt(cursor.getColumnIndex("bathrooms"));
                    int bed = cursor.getInt(cursor.getColumnIndex("bedrooms"));

                    // Create a ListingItem object and add it to the list
                    ListingItem listingItem = new ListingItem(listingID, imagePath, title, location, price, bath, bed);
                    listingItems.add(listingItem);
                } while (cursor.moveToNext());
                cursor.close(); // Close the cursor when done
            } else {
                Log.d("LoadListings", "Cursor is empty");
            }
        } else {
            Log.d("LoadListings", "Cursor is null");
        }

        agentListingsAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
    }

}