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
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipData;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class publishAdFragment extends Fragment {

    private RadioButton rbSell, rbRent;
    private FloatingActionButton btnBackToProfile;
    private TabLayout tabLayout;
    private TabLayout.Tab tiHome, tiFlat, tiPlot;
    private TextView selectImages;
    private EditText etBedrooms, etFloors, etBathrooms, etAreaSize, etPrice, etTitle, etDescription, etAgentEmail, etAgentContactNumber;
    private boolean isDataChanged = false;
    private Spinner spProvince, spCity;
    private SharedViewModel sharedViewModel;
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private DBHelper dbHelper;
    private Button btnSubmit;
    private static final int PICK_IMAGE_REQUEST = 1;
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

        selectImages = view.findViewById(R.id.selectImages);

        rbRent = view.findViewById(R.id.intentRentRadioBtn);
        rbSell = view.findViewById(R.id.intentSellRadioBtn);

        tabLayout = view.findViewById(R.id.propertyCategoryTabLayout);
        tiHome = tabLayout.getTabAt(0);
        tiFlat = tabLayout.getTabAt(1);
        tiPlot = tabLayout.getTabAt(2);

        spProvince = view.findViewById(R.id.spProvince);
        spCity = view.findViewById(R.id.spCity);

        btnSubmit = view.findViewById(R.id.submitBtn);
        btnBackToProfile = view.findViewById(R.id.toolbarBackBtn);

        imagesRecyclerView = view.findViewById(R.id.imagesRecyclerView);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        imagesAdapter = new ImagesAdapter(getContext(), selectedImages);
        imagesRecyclerView.setAdapter(imagesAdapter);

        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

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

        sharedViewModel.getListingID().observe(getViewLifecycleOwner(), listingID -> {
            if (listingID != null) {
                fetchListingDetails(listingID);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String email = etAgentEmail.getText().toString().trim();
                String bedrooms = etBedrooms.getText().toString().trim();
                String bathrooms = etBathrooms.getText().toString().trim();
                String floors = etFloors.getText().toString().trim();
                String area_size = etAreaSize.getText().toString().trim();
                String contactNumber = etAgentContactNumber.getText().toString().trim();
                String price = etPrice.getText().toString().trim();
                String province = spProvince.getSelectedItem().toString();
                String city = spCity.getSelectedItem().toString();

                // Validate inputs
                if (title.isEmpty() || description.isEmpty() || email.isEmpty() || bedrooms.isEmpty() || bathrooms.isEmpty() || floors.isEmpty()
                        || area_size.isEmpty() || contactNumber.isEmpty() || price.isEmpty() || province.isEmpty() || city.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Checking if the agent intends to rent or sell
                String listing_intent = "Sell";
                if (rbRent.isChecked()) {
                    listing_intent = "Rent";
                } else if (rbSell.isChecked()) {
                    listing_intent = "Sell";
                }

                // Checking what the listing type is
                String listing_type = "Homes";
                if (tiHome != null && tiHome.getText() != null)
                {
                    listing_type = "Homes";
                }
                else if (tiFlat != null && tiFlat.getText() != null)
                {
                    listing_type = "Flats";
                }
                else if (tiPlot != null && tiPlot.getText() != null)
                {
                    listing_type = "Plots";
                }

                String finalListing_intent = listing_intent;
                String finalListing_type = listing_type;
                // Confirmation Dialog and Insert Data
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Submission")
                        .setMessage("Are you sure you want to submit this listing?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            boolean result = dbHelper.insertListingData(
                                    title, description, price, province, city,
                                    finalListing_intent, finalListing_type,
                                    bedrooms, bathrooms, floors, area_size,
                                    email, contactNumber, selectedImages);

                            if (result) {
                                Toast.makeText(getContext(), "Listing submitted successfully", Toast.LENGTH_SHORT).show();
                                etTitle.setText("");
                                etDescription.setText("");
                                etBedrooms.setText("");
                                etBathrooms.setText("");
                                etFloors.setText("");
                                etAreaSize.setText("");
                                etPrice.setText("");
                                spProvince.setPrompt("Province");
                                spCity.setPrompt("City");
                                NavHostFragment.findNavController(publishAdFragment.this)
                                        .navigate(R.id.action_publishAdFragment_to_agentHomeFragment);
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
    private void fetchListingDetails(int listingID) {
        Cursor cursor = dbHelper.getListingDetails(listingID);
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String price = cursor.getString(cursor.getColumnIndex("price"));
            String floors = cursor.getString(cursor.getColumnIndex("floors"));
            String area = cursor.getString(cursor.getColumnIndex("area_size"));
            int baths = cursor.getInt(cursor.getColumnIndex("bathrooms"));
            int beds = cursor.getInt(cursor.getColumnIndex("bedrooms"));


            // Set the fetched values to the respective EditText fields
            etBathrooms.setText(String.valueOf(baths));
            etBedrooms.setText(String.valueOf(beds));
            etDescription.setText(description);
            etFloors.setText(floors);
            etAreaSize.setText(area);
            etPrice.setText(price);
            etTitle.setText(title);
        }
        if (cursor != null) {
            cursor.close(); // Always close the cursor to avoid memory leaks
        }
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) { // Multiple images selected
                ClipData clipData = data.getClipData();
                int count = clipData.getItemCount();
                int currentSize = selectedImages.size();

                for (int i = 0; i < count && currentSize < 8; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        selectedImages.add(bitmap);
                        currentSize++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) { // Single image selected
                if (selectedImages.size() < 8) {
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        selectedImages.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "You can only select up to 8 images", Toast.LENGTH_SHORT).show();
                }
            }
            imagesAdapter.notifyDataSetChanged(); // Update RecyclerView
        }
    }

}