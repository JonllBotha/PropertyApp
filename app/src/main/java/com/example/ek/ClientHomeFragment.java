package com.example.ek;

import android.os.Bundle;
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
        //return inflater.inflate(R.layout.fragment_client_home, container, false);

        //Button btnContact = getView().findViewById(R.id.btnContact);
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragmentContainerView);

        //btnContact.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View v) {
               // navController.navigate(R.id.action_clientHomeFragment_to_clientChatFragment);
          //  }
      //  });
        return view;
    }
}
