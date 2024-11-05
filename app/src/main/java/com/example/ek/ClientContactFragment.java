package com.example.ek;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ClientContactFragment extends Fragment {

    private Spinner spSubject;
    private EditText etMessage, etFullName, etCell, etEmail;
    private Button btnSubmit;
    private TextView btnCancel;
    private DBHelper dbHelper;
    private boolean isDataChanged = false;
    private SharedViewModel sharedViewModel;

    public ClientContactFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_contact, container, false);

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Find views
        spSubject = view.findViewById(R.id.spSubject);
        etMessage = view.findViewById(R.id.etMessage);
        etFullName = view.findViewById(R.id.etFullName);
        etCell = view.findViewById(R.id.etCell);
        etEmail = view.findViewById(R.id.etEmail);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.tv_cancel);

        Spinner spSubject = view.findViewById(R.id.spSubject);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), // or 'this' if in an activity
                R.array.enquiry_subjects,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubject.setAdapter(adapter);


        // Observe the email from the SharedViewModel
        sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), newEmail -> {
            if (newEmail != null && !newEmail.isEmpty()) {
                etEmail.setText(newEmail);  // Automatically fill email
                etEmail.setEnabled(false);  // Disable editing for the email field
            }
        });

        // Set button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = spSubject.getSelectedItem().toString();
                String message = etMessage.getText().toString().trim();
                String fullName = etFullName.getText().toString().trim();
                String cell = etCell.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                // Validate inputs
                if (subject.isEmpty() || message.isEmpty() || fullName.isEmpty() || cell.isEmpty() || email.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert enquiry into the database
                boolean result = dbHelper.insertEnquiry(subject, message, fullName, cell, email);
                if (result) {
                    Toast.makeText(getContext(), "Enquiry submitted successfully!", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(ClientContactFragment.this)
                            .navigate(R.id.action_clientContactFragment_to_clientProfileFragment);
                    // Optionally, clear the form
                    etMessage.setText("");
                    etFullName.setText("");
                    etCell.setText("");
                    etEmail.setText("");
                } else {
                    Toast.makeText(getContext(), "Failed to submit enquiry.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back button click listener
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (isDataChanged) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Unsaved Changes")
                            .setMessage("You have unsaved changes. Are you sure you want to cancel?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Navigate back
                                NavHostFragment.findNavController(ClientContactFragment.this)
                                        .navigate(R.id.action_clientContactFragment_to_clientProfileFragment);
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    NavHostFragment.findNavController(ClientContactFragment.this)
                            .navigate(R.id.action_clientContactFragment_to_clientProfileFragment);
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