package com.example.ek;

import android.content.SharedPreferences;
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

        // Set up the BottomNavigationView with NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Fetch the stored user role from SharedPreferences
        String userRole = getUserRoleFromSharedPreferences();

        // Check user role and set the appropriate menu
        // Check user role and set the appropriate menu
        if ("Agent".equals(userRole)) {
            bottomNavigationView.getMenu().clear(); // Clear any previous menu items
            bottomNavigationView.inflateMenu(R.menu.menu_agent);  // Inflate agent-specific menu
        } else {
            bottomNavigationView.getMenu().clear(); // Clear any previous menu items
            bottomNavigationView.inflateMenu(R.menu.menu_client); // Inflate client-specific menu
        }


        // Link BottomNavigationView with NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Add destination change listener to manage visibility of BottomNavigationView
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Show BottomNavigationView only on specific fragments
            if (destination.getId() == R.id.clientHomeFragment ||
                    destination.getId() == R.id.clientProfileFragment ||
                    destination.getId() == R.id.clientChatFragment ||
                    destination.getId() == R.id.agentHomeFragment ||
                    destination.getId() == R.id.agentPublishAd ||
                    destination.getId() == R.id.agentProfileFragment) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }

    // Fetch user role from SharedPreferences
    private String getUserRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return sharedPreferences.getString("user_role", "Client"); // Default to "Client" if role is not found
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
