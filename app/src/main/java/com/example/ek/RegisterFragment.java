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

    private EditText etFullName, etEmail, etPassword, etCPassword;
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
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etCPassword = view.findViewById(R.id.etCPassword);
        etFullName = view.findViewById(R.id.etFullName);

        btnRegister = view.findViewById(R.id.btnRegister);

        dbHelper = new DBHelper(getActivity());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String cPassword = etCPassword.getText().toString();
                String fullName = etFullName.getText().toString();
                String firstName = "";
                String lastName = "";

                if(fullName.split("\\w+").length>1){

                    lastName = fullName.substring(fullName.lastIndexOf(" ")+1);
                    firstName = fullName.substring(0, fullName.lastIndexOf(' '));
                }
                else{
                    firstName = fullName;
                }

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || cPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Please ensure all fields are filled.", Toast.LENGTH_LONG).show();
                } else {
                    if (password.equals(cPassword)) {
                        if (dbHelper.checkUsername(email)) {
                            Toast.makeText(getActivity(), "User already exists.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // Proceed with registration
                        boolean registerSuccess = dbHelper.insertData(email, password, firstName, lastName);
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
