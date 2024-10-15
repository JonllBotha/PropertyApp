package com.example.ek;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    private DBHelper dbHelper;
    private EditText etEmail, etPassword;
    private Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize DBHelper and UI components
        dbHelper = new DBHelper(getActivity());

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        // Set login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                // Check if the username and password are correct
                boolean isLoggedIn = dbHelper.checkUser(email, password);
                if (isLoggedIn) {
                    // Get the user's role
                    String role = getUserRole(email);

                    // Save the role in SharedPreferences for later access
                    saveUserRole(role);

                    // Get user's name
                    String fullName = getUserName(email);

                    // Create a Bundle to pass the profile name
                    Bundle bundle = new Bundle();
                    bundle.putString("profile_name", fullName);

                    // Navigate based on role
                    NavController navController = Navigation.findNavController(view);
                    if ("Agent".equals(role)) {
                        navController.navigate(R.id.action_loginFragment_to_agentHomeFragment, bundle);
                    } else {
                        navController.navigate(R.id.action_loginFragment_to_clientHomeFragment, bundle);
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
        if (cursor.moveToFirst()) {
            try {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"));
                fullName = firstName + " " + lastName;
            } catch (IllegalArgumentException e) {
                Log.e("Error", "Column not found: " + e.getMessage());
                // Handle the case where one or both columns are missing
            }
        }
        cursor.close();
        return fullName;
    }

    // Save the user's role in SharedPreferences
    private void saveUserRole(String role) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_role", role);
        editor.apply();
    }
}
