package com.example.ek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AgentHomeFragment extends Fragment {

    public AgentHomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agent_home, container, false);

        // Use 'view' to find the Button, not 'getView()'
        Button btnAddListing = view.findViewById(R.id.btnAddListing);
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragmentContainerView);

        btnAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_agentHomeFragment_to_publishAdActivity);
            }
        });

        return view; // Make sure to return the inflated view
    }
}