package com.example.ek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class PrivacyPolicyFragment extends Fragment {

    private FloatingActionButton btnBackToProfile;
    private SharedViewModel sharedViewModel;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);


        btnBackToProfile = view.findViewById(R.id.toolbarBackBtn);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe user role and set the back button listener based on role
        sharedViewModel.getUserRole().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String userRole) {
                if ("Client".equalsIgnoreCase(userRole)) {
                    btnBackToProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavHostFragment.findNavController(PrivacyPolicyFragment.this)
                                    .navigate(R.id.action_privacyPolicyFragment_to_clientProfileFragment);
                        }
                    });
                } else if ("Agent".equalsIgnoreCase(userRole)) {
                    btnBackToProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavHostFragment.findNavController(PrivacyPolicyFragment.this)
                                    .navigate(R.id.action_privacyPolicyFragment_to_agentProfileFragment);
                        }
                    });
                }
            }
        });

        return view;
    }
}