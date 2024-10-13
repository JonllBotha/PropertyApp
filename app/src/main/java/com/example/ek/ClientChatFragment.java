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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientChatFragment newInstance(String param1, String param2) {
        ClientChatFragment fragment = new ClientChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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