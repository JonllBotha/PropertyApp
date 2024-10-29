package com.example.ek;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ek.databinding.ActivityPublishAdBinding;
import com.google.android.material.tabs.TabItem;

public class publishAdFragment extends Fragment {

    private RadioButton rbSell, rbRent;
    private TabItem tiHome, tiFlat, tiPlot;
    private AutoCompleteTextView actvAreaSizeUnit;
    private EditText etBedrooms, etFloors, etBathrooms, etAreaSize, etPrice, etTitle, etDescription, etAgentEmail, etAgentContactNumber;
    private boolean isDataChanged = false;
    private Spinner spProvince, spCity;
    private SharedViewModel sharedViewModel;
    private DBHelper dbHelper;
    private Button btnSubmit;

    public publishAdFragment() {
        // Required empty public constructor
    }

    private ActivityPublishAdBinding binding;
    private static final String TAG = "PUBLISH_AD_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish_ad, container, false);

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());

        //---------------------
        //not sure why ViewModelProvider is red?:
        // Initialize SharedViewModel
      //  sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        //------------------------

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

        actvAreaSizeUnit = view.findViewById(R.id.areaSizeUnitACT);

        //--------------------
        //need to finish SimpleTextWatcher way below:
    //    etTitle.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
    //    etPrice.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
    //    etAreaSize.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
     //   etFloors.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
     //   etDescription.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
     //   etAgentContactNumber.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
     //   etBedrooms.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
     //   etBathrooms.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
     //   etAgentEmail.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));

        //spProvince.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));
        //spCity.addTextChangedListener(new publishAdFragment.SimpleTextWatcher(() -> isDataChanged = true));

        // Observe the email from the SharedViewModel
       // sharedViewModel.getProfileEmail().observe(getViewLifecycleOwner(), newEmail -> {
       //     if (newEmail != null && !newEmail.isEmpty()) {
          //      etAgentEmail.setText(newEmail);  // Automatically fill email
        //        etAgentEmail.setEnabled(false);  // Disable editing for the email field
          //      // Load agent data based on the email
          //      loadAgentData(newEmail);
          //  }
       // });
        //---------------------------

        // Update button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String email = etAgentEmail.getText().toString();
                String bedrooms = etBedrooms.getText().toString();
                String bathrooms = etBathrooms.getText().toString();
                String floors = etFloors.getText().toString();
                String areaSize = etAreaSize.getText().toString();
                String contactNumber = etAgentContactNumber.getText().toString();
                String price = etPrice.getText().toString();
                String locationProvince = spProvince.toString();
                String locationCity = spCity.toString();
                String areaSizeUnit = actvAreaSizeUnit.getText().toString();

                String intentRent = rbRent.getText().toString();
                String intentSell = rbSell.getText().toString();

                String propertyAreaSize = actvAreaSizeUnit.getText().toString();

                String homePropertyType = tiHome.toString();
                String flatPropertyType = tiFlat.toString();
                String plotPropertyType = tiPlot.toString();

                //-------------------------------------
                //Copied from Sav:
               // boolean result = dbHelper.insertAgentData(email, firstName, lastName, cell, about, province, city);

             //   if (result) {
             //       Toast.makeText(getContext(), "Property ad published successfully!", Toast.LENGTH_SHORT).show();
              //      isDataChanged = false;
              //  } else {
              //      Toast.makeText(getContext(), "Failed to publish property ad.", Toast.LENGTH_SHORT).show();
              //  }
            }
        });

        //--------------------
        //Still need to adapt below to this fragment's details:
        //@SuppressLint("Range")
       // private void loadAgentData(String email) {
           // Cursor cursor = dbHelper.getAgentData(email);
           // if (cursor.moveToFirst()) {
           //     // Populate the EditTexts with existing agent data
            //    etAgentContactNumber.setText(cursor.getString(cursor.getColumnIndex("phoneNumber")));
            //    etAgentEmail.setText(email); // Email is already stored in the ViewModel

            //    // Load province and city from the database and set them in the spinners
            //    String province = cursor.getString(cursor.getColumnIndex("province"));
             //   String city = cursor.getString(cursor.getColumnIndex("city"));

             //   // Set the province and city in the spinners
              //  setSpinnerSelection(spProvince, province);
             //   setSpinnerSelection(spCity, city);
         //   } else {
          //      Toast.makeText(getContext(), "Agent data not found", Toast.LENGTH_SHORT).show();
          //  }
       //     cursor.close();
      //  }
        //----------------------------


        //---------------------
        //Copied code from Sav: + still need to adjust:
        // Inner class SimpleTextWatcher
      //  public class SimpleTextWatcher implements TextWatcher {
          //  private Runnable onChange;

         //   public SimpleTextWatcher(Runnable onChange) {
          //      this.onChange = onChange;
         //   }

       //     @Override
         //   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

         //   @Override
         //   public void onTextChanged(CharSequence s, int start, int before, int count) {
         //       onChange.run();
        //    }

        //    @Override
       //     public void afterTextChanged(Editable s) {}
       // }
        //-----------------------------



//---------------------
        //Copied code from old activity:
      //  AutoCompleteTextView autoCompleteLocation = findViewById(R.id.locationACT);
     //   String[] cities = getResources().getStringArray(R.array.cities);

       // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cities);
       // autoCompleteLocation.setAdapter(adapter);

       // binding = ActivityPublishAdBinding.inflate(getLayoutInflater());
      //  setContentView(binding.getRoot());

//----------------------------------
        //Copied from Sav and edited:
        Button btnPublishFinalAd = view.findViewById(R.id.submitBtn);
        NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragmentContainerView);
//-----------------------------
        //Once screen is made where the whole published ad will be shown:
       // btnPublishFinalAd.setOnClickListener(new View.OnClickListener() {

      //      @Override
      //      public void onClick(View v) {
      //          navController.navigate(R.id.a);
        //    }
      //  });

        return view;
    }
}