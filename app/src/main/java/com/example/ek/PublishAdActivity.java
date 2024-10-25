package com.example.ek;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ek.databinding.ActivityPublishAdBinding;

public class PublishAdActivity extends AppCompatActivity {

    private ActivityPublishAdBinding binding;

    private static final String TAG = "PUBLISH_AD_TAG";

    private static final String[] HomeSubcategories = new String[] {

    };

    private static final String[] FlatSubcategories = new String[] {

    };

    private static final String[] PlotSubcategories = new String[] {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AutoCompleteTextView autoCompleteLocation = findViewById(R.id.locationACT);
        String[] cities = getResources().getStringArray(R.array.cities);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cities);
        autoCompleteLocation.setAdapter(adapter);

        binding = ActivityPublishAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    };
}
