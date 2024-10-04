package com.example.ek;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
                boolean isLoggedId = dbHelper.checkUser(email, password);
                if (isLoggedId) {
                    // Get user's name
                    String fullName = getUserName(email);

                    // Create a Bundle to pass the profile name
                    Bundle bundle = new Bundle();
                    bundle.putString("profile_name", fullName);

                    // Navigate to ClientHomeFragment, passing the bundle
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_loginFragment_to_clientHomeFragment, bundle);
                } else {
                    Toast.makeText(getActivity(), "Login Failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT firstName, lastName FROM users WHERE email = ?", new String[]{email});

        String fullName = "";
        if (cursor.moveToFirst()) {
            try {
                int firstNameIndex = cursor.getColumnIndexOrThrow("firstName");
                int lastNameIndex = cursor.getColumnIndexOrThrow("lastName");

                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);

                fullName = firstName + " " + lastName;
            } catch (IllegalArgumentException e) {
                Log.e("Error", "Column not found: " + e.getMessage());
                // Handle the case where one or both columns are missing
            }
        }
        cursor.close();
        return fullName;
    }
}