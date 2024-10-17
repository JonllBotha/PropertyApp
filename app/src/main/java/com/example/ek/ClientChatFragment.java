package com.example.ek;

import static com.example.ek.R.id.btnBackClientHome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ClientChatFragment extends Fragment {

    public ClientChatFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_chat, container, false);
        //return inflater.inflate(R.layout.fragment_client_chat, container, false);
        Button btnDial = view.findViewById(R.id.btnDial);
        Button btnTextJonell = view.findViewById(R.id.btnTextJonell);
        Button btnTextSune = view.findViewById(R.id.btnTextSune);
        Button btnTextSaveena = view.findViewById(R.id.btnTextSaveena);

        Button btnBackClientHome = view.findViewById(R.id.btnBackClientHome);
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragmentContainerView);

        btnTextJonell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+27843668560"));
                startActivity(intent);
            }
        });

        btnTextSaveena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+27820723408"));
                startActivity(intent);
            }
        });

        btnTextSune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+27737386173"));
                startActivity(intent);
            }
        });

        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+27410000000"));
                startActivity(intent);
            }
        });

        btnBackClientHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_clientChatFragment_to_clientHomeFragment);
            }
        });

        return view;
    }
}