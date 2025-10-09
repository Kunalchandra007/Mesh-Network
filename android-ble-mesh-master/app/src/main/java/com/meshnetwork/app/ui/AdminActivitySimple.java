package com.meshnetwork.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.meshnetwork.app.R;
import com.meshnetwork.app.services.BLEMeshService;

public class AdminActivitySimple extends AppCompatActivity {
    
    private FusedLocationProviderClient fusedLocationClient;
    private EditText messageEditText;
    private Button sendAlertButton;
    private Button broadcastMessageButton;
    private TextView locationTextView;
    private BLEMeshService bleMeshService;
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_simple);
        
        initializeViews();
        setupClickListeners();
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermissions();
        
        // Start BLE mesh service
        startBLEMeshService();
    }
    
    private void initializeViews() {
        messageEditText = findViewById(R.id.messageEditText);
        sendAlertButton = findViewById(R.id.sendAlertButton);
        broadcastMessageButton = findViewById(R.id.broadcastMessageButton);
        locationTextView = findViewById(R.id.locationTextView);
        
        locationTextView.setText("Location: Getting location...");
    }
    
    private void setupClickListeners() {
        sendAlertButton.setOnClickListener(v -> sendEmergencyAlert());
        broadcastMessageButton.setOnClickListener(v -> broadcastMessage());
    }
    
    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission is required for this app", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLatitude = location.getLatitude();
                                currentLongitude = location.getLongitude();
                                updateLocationDisplay();
                            }
                        }
                    });
        }
    }
    
    private void updateLocationDisplay() {
        if (currentLatitude != 0.0 && currentLongitude != 0.0) {
            locationTextView.setText(String.format("Admin Location: %.6f, %.6f", currentLatitude, currentLongitude));
        } else {
            locationTextView.setText("Location: Getting location...");
        }
    }
    
    private void sendEmergencyAlert() {
        // Broadcast emergency alert through BLE mesh network
        if (bleMeshService != null) {
            bleMeshService.broadcastEmergencyAlert("EMERGENCY ALERT: Immediate attention required!");
        }
        Toast.makeText(this, "Emergency alert sent to all users via BLE mesh!", Toast.LENGTH_SHORT).show();
    }
    
    private void broadcastMessage() {
        String message = messageEditText.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Broadcast message through BLE mesh network
        if (bleMeshService != null) {
            bleMeshService.broadcastMessage(message);
        }
        Toast.makeText(this, "Message broadcasted via BLE mesh: " + message, Toast.LENGTH_SHORT).show();
        messageEditText.setText("");
    }
    
    private void startBLEMeshService() {
        bleMeshService = BLEMeshService.getInstance(this);
        
        if (!bleMeshService.isBluetoothAvailable()) {
            Toast.makeText(this, "Bluetooth is not available or disabled", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Start BLE mesh service for admin mode
        bleMeshService.startService(true, new BLEMeshService.MeshMessageCallback() {
            @Override
            public void onMessageReceived(String deviceAddress, String message) {
                // Admin received message - display in UI
                runOnUiThread(() -> {
                    Toast.makeText(AdminActivitySimple.this, "Message from " + deviceAddress + ": " + message, Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onSOSReceived(String deviceAddress, double latitude, double longitude) {
                // Admin received SOS - handle emergency
                runOnUiThread(() -> {
                    Toast.makeText(AdminActivitySimple.this, "SOS RECEIVED from " + deviceAddress + " at " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
                    locationTextView.setText("Emergency Location: " + latitude + ", " + longitude);
                });
            }
            
            @Override
            public void onAlertReceived(String alertMessage) {
                // Admin received alert - shouldn't happen in admin mode
            }
        }, new BLEMeshService.LocationCallback() {
            @Override
            public void onLocationReceived(String deviceAddress, double latitude, double longitude) {
                // Admin received location update
                runOnUiThread(() -> {
                    Toast.makeText(AdminActivitySimple.this, "Location update from " + deviceAddress, Toast.LENGTH_SHORT).show();
                });
            }
        });
        
        Toast.makeText(this, "BLE Mesh Network started (Admin Mode)", Toast.LENGTH_SHORT).show();
    }
    
    public void logout(View view) {
        // Stop BLE mesh service
        if (bleMeshService != null) {
            bleMeshService.stopService();
        }
        
        // Return to main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
