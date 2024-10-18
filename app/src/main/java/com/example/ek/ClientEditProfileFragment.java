package com.example.ek;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextWatcher;

import android.app.AlertDialog;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClientEditProfileFragment extends Fragment {

    private EditText etFirstName, etLastName, etCell, etEmail;
    private DBHelper dbHelper;
    private Button btnUpdate;
    private FloatingActionButton btnBackToProfile;
    private boolean isDataChanged = false;
    private SharedViewModel sharedViewModel;

    public ClientEditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_edit_profile, container, false);

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Find EditTexts
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etCell = view.findViewById(R.id.etCell);
        etEmail = view.findViewById(R.id.etEmail);
        btnUpdate = view.findViewById(R.id.btnUpdateProfile);
        btnBackToProfile = view.findViewById(R.id.backToProfile);

        // Mark data as changed if the user modifies any field
        etFirstName.addTextChangedListener(new SimpleTextWatcher(() -> isDataChanged = true));
        etLastName.addTextChangedListener(new SimpleTextWatcher(() -> isDataChanged = true));
        etCell.addTextChangedListener(new SimpleTextWatcher(() -> isDataChanged = true));
        etEmail.addTextChangedListener(new SimpleTextWatcher(() -> isDataChanged = true));

        // Observe the email from the SharedViewModel
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), newEmail -> {
            if (newEmail != null && !newEmail.isEmpty()) {
                etEmail.setText(newEmail);  // Automatically fill email
                etEmail.setEnabled(false);  // Disable editing for the email field
                // Load client data based on the email
                loadClientData(newEmail);
            }
        });

        // Update button click listener
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String cell = etCell.getText().toString();
                String email = etEmail.getText().toString();

                boolean result = dbHelper.insertClientData(email, firstName, lastName, cell);

                if (result) {
                    Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    isDataChanged = false;
                } else {
                    Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back button click listener
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (isDataChanged) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Are you sure you want to go back?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Navigate back
                                NavHostFragment.findNavController(ClientEditProfileFragment.this)
                                        .navigate(R.id.action_clientEditProfileFragment_to_clientProfileFragment);
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    NavHostFragment.findNavController(ClientEditProfileFragment.this)
                            .navigate(R.id.action_clientEditProfileFragment_to_clientProfileFragment);
                }
            }
        });
        return view;
    }

    // Load client data from the database
    @SuppressLint("Range")
    private void loadClientData(String email) {
        Cursor cursor = dbHelper.getClientData(email);
        if (cursor.moveToFirst()) {
            // Populate the EditTexts with existing client data
            etFirstName.setText(cursor.getString(cursor.getColumnIndex("firstName")));
            etLastName.setText(cursor.getString(cursor.getColumnIndex("lastName")));
            etCell.setText(cursor.getString(cursor.getColumnIndex("phoneNumber")));
            etEmail.setText(email); // Email is already stored in the ViewModel
        } else {
            Toast.makeText(getContext(), "Client data not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
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

