package com.meshnetwork.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.meshnetwork.app.R;

public class MainActivitySimple extends AppCompatActivity {
    
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button adminLoginButton;
    private Button userModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupClickListeners();
    }
    
    private void initializeViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        adminLoginButton = findViewById(R.id.adminLoginButton);
        userModeButton = findViewById(R.id.userModeButton);
    }
    
    private void setupClickListeners() {
        adminLoginButton.setOnClickListener(v -> handleAdminLogin());
        userModeButton.setOnClickListener(v -> startUserMode());
    }
    
    private void handleAdminLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            // Admin login successful - navigate to admin activity
            Intent intent = new Intent(this, AdminActivitySimple.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void startUserMode() {
        // User mode - navigate to user activity
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
        finish();
    }
}
