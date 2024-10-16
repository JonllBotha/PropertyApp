package com.example.ek;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ClientProfileFragment extends Fragment {

    private RecyclerView rv_profile;
    private ClientProfileAdapter adapter;
    private String fullName, email;
    private TextView profileName;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        profileName = view.findViewById(R.id.profile_name);

        // Observe the full name from SharedViewModel
        sharedViewModel.getUserFullName().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                profileName.setText(fullName);
            }
        });

        rv_profile = view.findViewById(R.id.rv_profile);

        // Initialize the list of profile items
        List<ProfileItem> items = new ArrayList<>();
        items.add(new ProfileItem(R.drawable.icon_profile_outline, "Your Profile", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_calculator, "Bond Calculator", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_settings, "Settings", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_privacy, "Privacy Policy", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_logout, "Log Out", R.drawable.icon_arrow));

        // Pass the fragment itself (this) instead of the context to the adapter
        adapter = new ClientProfileAdapter(this, items, email);
        rv_profile.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_profile.setAdapter(adapter);

        return view;
    }
}
