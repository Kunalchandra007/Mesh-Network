package com.meshnetwork.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class UserActivity extends AppCompatActivity {
    
    private FusedLocationProviderClient fusedLocationClient;
    private Button sosButton;
    private Button shareLocationButton;
    private TextView statusTextView;
    private TextView locationTextView;
    private TextView messageTextView;
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private BLEMeshService bleMeshService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        initializeViews();
        setupClickListeners();
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermissions();
        
        // Start BLE mesh service
        startBLEMeshService();
    }
    
    private void initializeViews() {
        sosButton = findViewById(R.id.sosButton);
        shareLocationButton = findViewById(R.id.shareLocationButton);
        statusTextView = findViewById(R.id.statusTextView);
        locationTextView = findViewById(R.id.locationTextView);
        messageTextView = findViewById(R.id.messageTextView);
        
        statusTextView.setText("Status: Ready");
        messageTextView.setText("Messages will appear here...");
    }
    
    private void setupClickListeners() {
        sosButton.setOnClickListener(v -> sendSOS());
        shareLocationButton.setOnClickListener(v -> shareCurrentLocation());
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
                Toast.makeText(this, "Location permission is required for SOS functionality", Toast.LENGTH_LONG).show();
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
            locationTextView.setText(String.format("Location: %.6f, %.6f", currentLatitude, currentLongitude));
        } else {
            locationTextView.setText("Location: Getting location...");
        }
    }
    
    private void sendSOS() {
        if (currentLatitude == 0.0 && currentLongitude == 0.0) {
            Toast.makeText(this, "Getting location... Please try again", Toast.LENGTH_SHORT).show();
            getCurrentLocation();
            return;
        }
        
        // Send SOS through BLE mesh network
        if (bleMeshService != null) {
            bleMeshService.sendSOSMessage(currentLatitude, currentLongitude);
        }
        
        statusTextView.setText("Status: SOS Sent!");
        Toast.makeText(this, "SOS sent with location data via BLE mesh!", Toast.LENGTH_LONG).show();
        
        // Reset status after 3 seconds
        sosButton.postDelayed(() -> {
            statusTextView.setText("Status: Ready");
        }, 3000);
    }
    
    private void shareCurrentLocation() {
        if (currentLatitude == 0.0 && currentLongitude == 0.0) {
            Toast.makeText(this, "Getting location... Please try again", Toast.LENGTH_SHORT).show();
            getCurrentLocation();
            return;
        }
        
        // Share location through BLE mesh network
        if (bleMeshService != null) {
            bleMeshService.sendLocationUpdate(currentLatitude, currentLongitude);
        }
        Toast.makeText(this, "Location shared with admin via BLE mesh", Toast.LENGTH_SHORT).show();
    }
    
    public void displayMessage(String message) {
        messageTextView.setText("Admin Message: " + message);
    }
    
    public void displayAlert(String alert) {
        messageTextView.setText("ALERT: " + alert);
        Toast.makeText(this, "Emergency Alert: " + alert, Toast.LENGTH_LONG).show();
    }
    
    private void startBLEMeshService() {
        bleMeshService = BLEMeshService.getInstance(this);
        
        if (!bleMeshService.isBluetoothAvailable()) {
            Toast.makeText(this, "Bluetooth is not available or disabled", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Start BLE mesh service for user mode
        bleMeshService.startService(false, new BLEMeshService.MeshMessageCallback() {
            @Override
            public void onMessageReceived(String deviceAddress, String message) {
                displayMessage(message);
            }
            
            @Override
            public void onSOSReceived(String deviceAddress, double latitude, double longitude) {
                // User received SOS - shouldn't happen in user mode
            }
            
            @Override
            public void onAlertReceived(String alertMessage) {
                displayAlert(alertMessage);
            }
        }, new BLEMeshService.LocationCallback() {
            @Override
            public void onLocationReceived(String deviceAddress, double latitude, double longitude) {
                // User received location - shouldn't happen in user mode
            }
        });
        
        Toast.makeText(this, "BLE Mesh Network started", Toast.LENGTH_SHORT).show();
    }
    
    public void goBack(View view) {
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
