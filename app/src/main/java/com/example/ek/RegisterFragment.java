package com.example.ek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class RegisterFragment extends Fragment {

    private EditText etUser, etPassword, etCPassword;
    private Button btnRegister;
    private DBHelper dbHelper;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize UI components
        etUser = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        etCPassword = view.findViewById(R.id.etCPassword);

        btnRegister = view.findViewById(R.id.btnRegister);

        dbHelper = new DBHelper(getActivity());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString();
                String password = etPassword.getText().toString();
                String cPassword = etCPassword.getText().toString();

                if (user.isEmpty() || password.isEmpty() || cPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Please ensure all fields are filled.", Toast.LENGTH_LONG).show();
                } else {
                    if (password.equals(cPassword)) {
                        if (dbHelper.checkUsername(user)) {
                            Toast.makeText(getActivity(), "User already exists.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Proceed with registration
                        boolean registerSuccess = dbHelper.insertData(user, password);
                        if (registerSuccess) {
                            Toast.makeText(getActivity(), "User registered successfully.", Toast.LENGTH_LONG).show();
                            NavController navController = Navigation.findNavController(view);
                            navController.navigate(R.id.action_registerFragment_to_loginFragment);
                        } else {
                            Toast.makeText(getActivity(), "User registration failed.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }
}
