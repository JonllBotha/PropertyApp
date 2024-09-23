package com.example.ek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class StartupFragment extends Fragment {

    public StartupFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_startup, container, false);

        // Get references
        TextView tvGoToLogin = view.findViewById(R.id.tvGoToLogin);
        Button btnGoToRegister = view.findViewById(R.id.btnGoToRegister);

        // Set up NavController
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragmentContainerView);

        // Set button listeners
        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_startupFragment_to_loginFragment);
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_startupFragment_to_registerFragment);
            }
        });

        return view;
    }
}
