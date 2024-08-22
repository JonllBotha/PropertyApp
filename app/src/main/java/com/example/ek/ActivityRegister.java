package com.example.ek;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityRegister extends AppCompatActivity {

    EditText etUser, etPassword, etCPassword;
    Button btnRegister, btnGoToLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUser = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnLogin);

        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, password, cPassword;
                user = etUser.getText().toString();
                password = etPassword.getText().toString();
                cPassword = etCPassword.getText().toString();

                if (user.equals("") || password.equals("") || cPassword.equals("")){
                    Toast.makeText(ActivityRegister.this, "Please ensure all fields are filled.", Toast.LENGTH_LONG).show();
                } else {
                    if (password.equals(cPassword)){
                        if (dbHelper.checkUsername(user)){
                            Toast.makeText(ActivityRegister.this, "User already exists.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // proceed with registration
                        boolean registerSuccess = dbHelper.insertData(user,password);
                        if (registerSuccess)
                            Toast.makeText(ActivityRegister.this, "User registered successfully.", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(ActivityRegister.this, "User registered failed.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ActivityRegister.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}