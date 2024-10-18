package com.example.ek;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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



        binding = ActivityPublishAdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    };
}
