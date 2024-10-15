package com.example.ek;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClientProfileFragment extends Fragment {

    private RecyclerView rv_profile;
    private ClientProfileAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String profileName = bundle.getString("profile_name");
            Log.d("ClientProfileFragment", "Profile name received: " + profileName);

            // Set the profile name in the TextView
            TextView profileNameTextView = view.findViewById(R.id.profile_name);
            profileNameTextView.setText(profileName != null ? profileName : "Unknown User");
        }

        rv_profile = view.findViewById(R.id.rv_profile);

        List<ProfileItem> items = new ArrayList<>();
        items.add(new ProfileItem(R.drawable.icon_profile_outline, "Your Profile", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_settings, "Settings", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_privacy, "Privacy Policy", R.drawable.icon_arrow));
        items.add(new ProfileItem(R.drawable.icon_logout, "Log Out", R.drawable.icon_arrow));

        adapter = new ClientProfileAdapter(getContext(), items);
        rv_profile.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_profile.setAdapter(adapter);

        return view;
    }

}
