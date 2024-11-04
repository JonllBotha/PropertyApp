package com.example.ek;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextWatcher;

import android.app.AlertDialog;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClientEditProfileFragment extends Fragment {

    private EditText etFirstName, etLastName, etCell, etEmail;
    private Spinner spProvince, spCity;
    private DBHelper dbHelper;
    private Button btnUpdate;
    private FloatingActionButton btnBackToProfile;
    private boolean isDataChanged = false;
    private SharedViewModel sharedViewModel;
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;

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

        spProvince = view.findViewById(R.id.spProvince);
        spCity = view.findViewById(R.id.spCity);

        // Populate province spinner
        provinceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.provinces));
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvince.setAdapter(provinceAdapter);

        // Set listener for province spinner
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected province
                String selectedProvince = parentView.getItemAtPosition(position).toString();
                populateCitySpinner(selectedProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

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
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String cell = etCell.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String province = spProvince.getSelectedItem().toString().trim();
                String city = spCity.getSelectedItem().toString().trim();

                // Validate inputs
                if (firstName.isEmpty() || lastName.isEmpty() || cell.isEmpty() || email.isEmpty() || province.isEmpty() || city.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isDataChanged) {
                    // Check if a record already exists for the email
                    if (dbHelper.isClientRecordExists(email)) {
                        // Update existing user data
                        boolean result = dbHelper.updateClientData(email, firstName, lastName, cell, province, city);
                        if (result) {
                            Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Insert new user data
                        boolean result = dbHelper.insertClientData(email, firstName, lastName, cell, province, city);
                        if (result) {
                            Toast.makeText(getContext(), "Profile inserted successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to insert profile.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    isDataChanged = false;
                } else {
                    Toast.makeText(getContext(), "No changes made.", Toast.LENGTH_SHORT).show();
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

            // Load province and city from the database
            String province = cursor.getString(cursor.getColumnIndex("province"));
            String city = cursor.getString(cursor.getColumnIndex("city"));

            // Set the province first to populate the city spinner correctly
            setSpinnerSelection(spProvince, province);
            populateCitySpinner(province); // Ensure cities are populated based on the province
            setSpinnerSelection(spCity, city); // Now set the city selection

        } else {
            Toast.makeText(getContext(), "Client data not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    // Method to populate city spinner based on selected province
    private void populateCitySpinner(String province) {
        int citiesArrayId;
        switch (province) {
            case "Eastern Cape":
                citiesArrayId = R.array.eastern_cape_cities;
                break;
            case "Free State":
                citiesArrayId = R.array.free_state_cities;
                break;
            case "Gauteng":
                citiesArrayId = R.array.gauteng_cities;
                break;
            case "KwaZulu-Natal":
                citiesArrayId = R.array.kwazulu_natal_cities;
                break;
            case "Limpopo":
                citiesArrayId = R.array.limpopo_cities;
                break;
            case "Mpumalanga":
                citiesArrayId = R.array.mpumalanga_cities;
                break;
            case "Northern Cape":
                citiesArrayId = R.array.northern_cape_cities;
                break;
            case "North West":
                citiesArrayId = R.array.north_west_cities;
                break;
            case "Western Cape":
                citiesArrayId = R.array.western_cape_cities;
                break;
            default:
                citiesArrayId = R.array.eastern_cape_cities;  // Default if nothing is selected
        }
        String[] cityArray = getResources().getStringArray(citiesArrayId);
        cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cityArray);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(cityAdapter);
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter(); // Ensure adapter is not null
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) { // Check for position being valid
                spinner.setSelection(position);
            } else {
                Log.e("AgentEditProfileFragment", "Value not found in adapter: " + value);
            }
        } else {
            Log.e("AgentEditProfileFragment", "Adapter is null for spinner: " + spinner.getId());
        }
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

