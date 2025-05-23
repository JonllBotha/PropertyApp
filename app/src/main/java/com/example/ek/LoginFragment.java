package com.example.ek;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginFragment extends Fragment {

    private DBHelper dbHelper;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FloatingActionButton btnbackToStartup;
    private SharedViewModel sharedViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize DBHelper and UI components
        dbHelper = new DBHelper(getActivity());

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnbackToStartup = view.findViewById(R.id.backToStartup);

        // Back button click listener
        btnbackToStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_loginFragment_to_startupFragment);
            }
        });

        // Set login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Initialize SharedViewModel
                sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

                // Pass the email to the SharedViewModel
                sharedViewModel.setProfileEmail(email);

                // Check if the username and password are correct
                boolean isLoggedIn = dbHelper.checkUser(email, password);
                if (isLoggedIn) {
                    // Get the user's name and role
                    String fullName = getUserName(email);
                    String role = getUserRole(email);

                    // Set the full name and role in SharedViewModel
                    sharedViewModel.setUserFullName(fullName);
                    sharedViewModel.setUserRole(role);

                    // Navigate based on role
                    NavController navController = Navigation.findNavController(view);
                    if ("Agent".equals(role)) {
                        navController.navigate(R.id.action_loginFragment_to_agentHomeFragment);
                    } else {
                        navController.navigate(R.id.action_loginFragment_to_clientHomeFragment);
                    }
                } else {
                    Toast.makeText(getActivity(), "Login Failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    // Retrieve the user's role from the database
    public String getUserRole(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE email = ?", new String[]{email});

        String role = "Client"; // Default role
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
        }
        cursor.close();
        return role;
    }

    // Retrieve the user's name from the database
    public String getUserName(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT firstName, lastName FROM users WHERE email = ?", new String[]{email});

        String fullName = "";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"));
                fullName = firstName + " " + lastName;
            }
            cursor.close();
        } else {
            Log.e("LoginFragment", "Cursor is null for email: " + email);
        }
        return fullName;
    }
}
