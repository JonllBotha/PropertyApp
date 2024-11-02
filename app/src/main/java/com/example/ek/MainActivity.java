package com.example.ek;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.ek.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private SharedViewModel sharedViewModel; // Declare the SharedViewModel
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the SharedViewModel
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Obtain reference to the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragmentContainerView);

        // Get the NavController
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Observe the user role from the SharedViewModel
        sharedViewModel.getUserRole().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String userRole) {
                if ("Agent".equals(userRole)) {
                    bottomNavigationView.getMenu().clear(); // Clear any previous menu items
                    bottomNavigationView.inflateMenu(R.menu.menu_agent);  // Agent menu
                } else {
                    bottomNavigationView.getMenu().clear(); // Clear any previous menu items
                    bottomNavigationView.inflateMenu(R.menu.menu_client); // Client menu
                }
            }
        });

        // Link BottomNavigationView with NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Add destination change listener to manage visibility of BottomNavigationView
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Show BottomNavigationView only on specific fragments
            if (destination.getId() == R.id.clientHomeFragment ||
                    destination.getId() == R.id.clientMapFragment ||
                    destination.getId() == R.id.clientProfileFragment ||
                    destination.getId() == R.id.clientChatFragment ||
                    destination.getId() == R.id.agentHomeFragment ||
                    destination.getId() == R.id.publishAdFragment ||
                    destination.getId() == R.id.agentProfileFragment) {
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
