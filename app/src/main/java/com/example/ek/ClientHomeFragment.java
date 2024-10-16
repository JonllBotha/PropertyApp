package com.example.ek;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ClientHomeFragment extends Fragment {

    public ClientHomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_home, container, false);

        // Retrieve the email from the arguments
        if (getArguments() != null) {
            String fullName = getArguments().getString("fullName");
            String email = getArguments().getString("email");
        }

        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragmentContainerView);

        return view;
    }
}
