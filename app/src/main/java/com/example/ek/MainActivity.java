package com.example.ek;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain reference to the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragmentContainerView);

        // Get the NavController
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Set up the ActionBar with the Navigation UI
        NavigationUI.setupActionBarWithNavController(this, navController);

        // Set up BottomNavigationView with NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Add destination change listener to manage bottom nav visibility
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Show BottomNavigationView only on ClientHomeFragment or similar fragments
            if (destination.getId() == R.id.clientHomeFragment || destination.getId() == R.id.clientProfileFragment || destination.getId() == R.id.clientChatFragment) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
