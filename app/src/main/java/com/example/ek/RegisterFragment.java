package com.example.ek;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RegisterFragment extends Fragment {

    private EditText etFullName, etEmail, etPassword, etCPassword;
    private Button btnRegister;
    private FloatingActionButton btnbackToStartup;
    private boolean isDataChanged = false;
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

        // Mark data as changed if the user modifies any field
        etFullName.addTextChangedListener(new RegisterFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etCPassword.addTextChangedListener(new RegisterFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etPassword.addTextChangedListener(new RegisterFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etEmail.addTextChangedListener(new RegisterFragment.SimpleTextWatcher(() -> isDataChanged = true));

        btnRegister = view.findViewById(R.id.btnRegister);
        btnbackToStartup = view.findViewById(R.id.backToStartup);

        dbHelper = new DBHelper(getActivity());

        // Back button click listener
        btnbackToStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (isDataChanged) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Are you sure you want to go back?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Navigate back
                                NavHostFragment.findNavController(RegisterFragment.this)
                                        .navigate(R.id.action_registerFragment_to_startupFragment);
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    NavHostFragment.findNavController(RegisterFragment.this)
                            .navigate(R.id.action_registerFragment_to_startupFragment);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String cPassword = etCPassword.getText().toString();
                String fullName = etFullName.getText().toString();
                String firstName = "";
                String lastName = "";
                String role = email.endsWith("@horizonhomefinders.com") ? "Agent" : "Client";

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
                        boolean registerSuccess = dbHelper.insertData(email, password, firstName, lastName, role);
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

    // Inner class SimpleTextWatcher
    public class SimpleTextWatcher implements TextWatcher {
        private Runnable onChange;

        public SimpleTextWatcher(Runnable onChange) {
            this.onChange = onChange;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onChange.run();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
