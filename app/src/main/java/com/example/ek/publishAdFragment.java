package com.example.ek;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.ClipData;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class publishAdFragment extends Fragment {

    private RadioButton rbSell, rbRent;
    private FloatingActionButton btnBackToProfile;
    private TabItem tiHome, tiFlat, tiPlot;
    private EditText etBedrooms, etFloors, etBathrooms, etAreaSize, etPrice, etTitle, etDescription, etAgentEmail, etAgentContactNumber;
    private boolean isDataChanged = false;
    private Spinner spProvince, spCity;
    private SharedViewModel sharedViewModel;
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private DBHelper dbHelper;
    private Button btnSubmit;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap selectedImageBitmap;
    private ArrayList<Bitmap> selectedImages = new ArrayList<>();
    private ImagesAdapter imagesAdapter;
    private RecyclerView imagesRecyclerView;

    public publishAdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish_ad, container, false);

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        // Initialize SharedViewModel;
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //Find variables
        etAgentEmail = view.findViewById(R.id.agentEmailEditTxt);
        etBathrooms = view.findViewById(R.id.bathroomsEditTxt);
        etBedrooms = view.findViewById(R.id.bedroomsEditTxt);
        etDescription = view.findViewById(R.id.descriptionEditTxt);
        etFloors = view.findViewById(R.id.floorsEditTxt);
        etAgentContactNumber = view.findViewById(R.id.agentContactNumberEditTxt);
        etAreaSize = view.findViewById(R.id.areaSizeEditTxt);
        etPrice = view.findViewById(R.id.priceEditTxt);
        etTitle = view.findViewById(R.id.titleEditTxt);

        rbRent = view.findViewById(R.id.intentRentRadioBtn);
        rbSell = view.findViewById(R.id.intentSellRadioBtn);

        tiHome = view.findViewById(R.id.homePropertyCategory);
        tiFlat = view.findViewById(R.id.flatPropertyCategory);
        tiPlot = view.findViewById(R.id.plotPropertyCategory);

        spProvince = view.findViewById(R.id.spProvince);
        spCity = view.findViewById(R.id.spCity);

        btnSubmit = view.findViewById(R.id.submitBtn);
        btnBackToProfile = view.findViewById(R.id.toolbarBackBtn);

        imagesRecyclerView = view.findViewById(R.id.imagesRecyclerView);
        imagesAdapter = new ImagesAdapter(getContext(), selectedImages);
        imagesRecyclerView.setAdapter(imagesAdapter);

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
        etTitle.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etPrice.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etAreaSize.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etFloors.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etDescription.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etAgentContactNumber.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etBedrooms.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etBathrooms.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        etAgentEmail.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));

         // Observe the email from the SharedViewModel
         sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), newEmail -> {
             if (newEmail != null && !newEmail.isEmpty()) {
              etAgentEmail.setText(newEmail);  // Automatically fill email
                etAgentEmail.setEnabled(false);  // Disable editing for the email field
              // Load agent data based on the email
              loadAgentData(newEmail);
          }
         });

        // Submit button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String email = etAgentEmail.getText().toString();
                String bedrooms = etBedrooms.getText().toString();
                String bathrooms = etBathrooms.getText().toString();
                String floors = etFloors.getText().toString();
                String area_size = etAreaSize.getText().toString();
                String contactNumber = etAgentContactNumber.getText().toString();
                String price = etPrice.getText().toString();
                String province = spProvince.getSelectedItem().toString();
                String city = spCity.getSelectedItem().toString();

                byte[] imageBytes = (selectedImageBitmap != null) ? getImageBytes(selectedImageBitmap) : null;

                // Checking if the agent intends to rent or sell
                String listing_intent = rbSell.getText().toString();
                if (rbRent.isChecked()) {
                    listing_intent = rbRent.getText().toString();
                } else if (rbSell.isChecked()) {
                    listing_intent = rbSell.getText().toString();
                }

                // Checking what the listing type is
                String listing_type = tiHome.toString();
                if (tiHome.isActivated())
                {
                    listing_type = tiHome.toString();
                }
                else if (tiFlat.isActivated())
                {
                    listing_type = tiFlat.toString();
                }
                else if (tiPlot.isActivated())
                {
                    listing_type = tiPlot.toString();
                }

                // Validate inputs
                if (title.isEmpty() || description.isEmpty() || email.isEmpty() || bedrooms.isEmpty() || bathrooms.isEmpty() || floors.isEmpty()
                        || area_size.isEmpty() || contactNumber.isEmpty() || price.isEmpty() || province.isEmpty() || city.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Confirmation Dialog
                String finalListing_intent = listing_intent;
                String finalListing_type = listing_type;
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Submission")
                        .setMessage("Are you sure you want to submit this listing?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Call the method to insert data
                            boolean result = dbHelper.insertListingData(
                                    title, description, price, province, city,
                                    finalListing_intent, finalListing_type,
                                    bedrooms, bathrooms, floors, area_size,
                                    email, contactNumber, imageBytes);

                            if (result) {
                                Toast.makeText(getContext(), "Listing submitted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Submission failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
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
                                NavHostFragment.findNavController(publishAdFragment.this)
                                        .navigate(R.id.action_publishAdFragment_to_agentHomeFragment);
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    NavHostFragment.findNavController(publishAdFragment.this)
                            .navigate(R.id.action_publishAdFragment_to_agentHomeFragment);
                }
            }
        });
        return view;
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

        cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,getResources().getStringArray(citiesArrayId));
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(cityAdapter);
    }

    @SuppressLint("Range")
    private void loadAgentData(String email) {
        Cursor cursor = dbHelper.getAgentData(email);
        if (cursor.moveToFirst()) {
            // Populate the EditTexts with existing agent data
            etAgentContactNumber.setText(cursor.getString(cursor.getColumnIndex("phoneNumber")));
            etAgentEmail.setText(email); // Email is already stored in the ViewModel

            // Load province and city from the database and set them in the spinners
            String province = cursor.getString(cursor.getColumnIndex("province"));
            String city = cursor.getString(cursor.getColumnIndex("city"));

            // Set the province and city in the spinners
            setSpinnerSelection(spProvince, province);
            setSpinnerSelection(spCity, city);
        } else {
            Toast.makeText(getContext(), "Agent data not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter(); // Ensure adapter is not null
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) { // Check for position being valid
                spinner.setSelection(position);
            } else {
                Log.e("publishAdFragment", "Value not found in adapter: " + value);
            }
        } else {
            Log.e("publishAdFragment", "Adapter is null for spinner: " + spinner.getId());
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

    private byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // Allow multiple images selection
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) { // Multiple images selected
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        selectedImages.add(bitmap); // Add to selected images list
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) { // Single image selected
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    selectedImages.add(bitmap); // Add to selected images list
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imagesAdapter.notifyDataSetChanged(); // Update RecyclerView
        }
    }
}