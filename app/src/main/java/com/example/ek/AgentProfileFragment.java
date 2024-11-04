package com.example.ek;

import com.example.ek.R;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AgentProfileFragment extends Fragment {

    private RecyclerView rv_agent_profile;
    private AgentProfileAdapter adapter;
    private String fullName, email;
    private TextView profileName;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agent_profile, container, false);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        profileName = view.findViewById(R.id.profile_name);

        // Observe the full name from SharedViewModel
        sharedViewModel.getUserFullName().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                profileName.setText(new StringBuilder().append(fullName).append(",").toString());
            }
        });

        rv_agent_profile = view.findViewById(R.id.rv_agent_profile);

        // Initialize the list of profile items
        List<ProfileItem> items = new ArrayList<>();
        items.add(new ProfileItem(R.drawable.icon_profile_outline, "Your Profile", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_settings, "Settings", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_logout, "Log Out", R.drawable.icon_arrow));

        // Pass the fragment itself (this) instead of the context to the adapter
        adapter = new AgentProfileAdapter(this, items, email);
        rv_agent_profile.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_agent_profile.setAdapter(adapter);

        return view;
    }
}