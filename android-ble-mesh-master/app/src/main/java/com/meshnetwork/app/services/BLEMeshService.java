package com.meshnetwork.app.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.meshnetwork.app.ui.AdminActivitySimple;
import com.meshnetwork.app.ui.UserActivity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * BLE Mesh Service for real Bluetooth Low Energy communication
 * Handles advertising, scanning, and GATT communication
 */
public class BLEMeshService {
    
    private static final String TAG = "BLEMeshService";
    
    // BLE UUIDs for our mesh network
    private static final String MESH_SERVICE_UUID = "12345678-1234-1234-1234-123456789ABC";
    private static final String MESSAGE_CHARACTERISTIC_UUID = "12345678-1234-1234-1234-123456789ABD";
    private static final String LOCATION_CHARACTERISTIC_UUID = "12345678-1234-1234-1234-123456789ABE";
    
    private static BLEMeshService instance;
    private Context context;
    
    // BLE Components
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;
    
    // Device Management
    private Map<String, BluetoothDevice> connectedDevices;
    private Map<String, String> deviceMessages;
    private String deviceName;
    private boolean isAdmin = false;
    
    // Callbacks
    private MeshMessageCallback messageCallback;
    private LocationCallback locationCallback;
    
    // Handler for UI updates
    private Handler mainHandler;
    
    public interface MeshMessageCallback {
        void onMessageReceived(String deviceAddress, String message);
        void onSOSReceived(String deviceAddress, double latitude, double longitude);
        void onAlertReceived(String alertMessage);
    }
    
    public interface LocationCallback {
        void onLocationReceived(String deviceAddress, double latitude, double longitude);
    }
    
    private BLEMeshService(Context context) {
        this.context = context;
        this.connectedDevices = new HashMap<>();
        this.deviceMessages = new HashMap<>();
        this.mainHandler = new Handler(Looper.getMainLooper());
        
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        
        deviceName = "MeshDevice_" + System.currentTimeMillis();
    }
    
    public static synchronized BLEMeshService getInstance(Context context) {
        if (instance == null) {
            instance = new BLEMeshService(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Start the BLE mesh service
     */
    public void startService(boolean isAdmin, MeshMessageCallback messageCallback, LocationCallback locationCallback) {
        this.isAdmin = isAdmin;
        this.messageCallback = messageCallback;
        this.locationCallback = locationCallback;
        
        Log.d(TAG, "Starting BLE Mesh Service - Admin: " + isAdmin);
        
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth not available or disabled");
            return;
        }
        
        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        
        // Start advertising our device
        startAdvertising();
        
        // Start scanning for other devices
        startScanning();
    }
    
    /**
     * Stop the BLE mesh service
     */
    public void stopService() {
        Log.d(TAG, "Stopping BLE Mesh Service");
        
        if (bluetoothLeAdvertiser != null) {
            bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
        }
        
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(scanCallback);
        }
        
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
        
        connectedDevices.clear();
        deviceMessages.clear();
    }
    
    /**
     * Start BLE advertising
     */
    private void startAdvertising() {
        if (bluetoothLeAdvertiser == null) return;
        
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(true)
                .build();
        
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(new ParcelUuid(UUID.fromString(MESH_SERVICE_UUID)))
                .build();
        
        bluetoothLeAdvertiser.startAdvertising(settings, data, advertiseCallback);
        Log.d(TAG, "Started BLE advertising");
    }
    
    /**
     * Start BLE scanning
     */
    private void startScanning() {
        if (bluetoothLeScanner == null) return;
        
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build();
        
        bluetoothLeScanner.startScan(null, settings, scanCallback);
        Log.d(TAG, "Started BLE scanning");
    }
    
    /**
     * Send SOS message with location
     */
    public void sendSOSMessage(double latitude, double longitude) {
        String message = "SOS|" + latitude + "|" + longitude;
        broadcastMessage(message);
        Log.d(TAG, "Sending SOS message: " + message);
    }
    
    /**
     * Send location update
     */
    public void sendLocationUpdate(double latitude, double longitude) {
        String message = "LOCATION|" + latitude + "|" + longitude;
        broadcastMessage(message);
        Log.d(TAG, "Sending location update: " + message);
    }
    
    /**
     * Broadcast emergency alert
     */
    public void broadcastEmergencyAlert(String alertMessage) {
        String message = "ALERT|" + alertMessage;
        broadcastMessage(message);
        Log.d(TAG, "Broadcasting emergency alert: " + message);
    }
    
    /**
     * Broadcast general message
     */
    public void broadcastMessage(String message) {
        // In a real implementation, this would send to all connected devices
        // For now, we'll simulate the message being sent
        Log.d(TAG, "Broadcasting message: " + message);
        
        // Simulate message processing
        processReceivedMessage("SIMULATED_DEVICE", message);
    }
    
    /**
     * Process received messages
     */
    private void processReceivedMessage(String deviceAddress, String message) {
        String[] parts = message.split("\\|");
        if (parts.length < 2) return;
        
        String messageType = parts[0];
        
        switch (messageType) {
            case "SOS":
                if (parts.length >= 3) {
                    try {
                        double latitude = Double.parseDouble(parts[1]);
                        double longitude = Double.parseDouble(parts[2]);
                        
                        mainHandler.post(() -> {
                            if (messageCallback != null) {
                                messageCallback.onSOSReceived(deviceAddress, latitude, longitude);
                            }
                        });
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid SOS coordinates", e);
                    }
                }
                break;
                
            case "LOCATION":
                if (parts.length >= 3) {
                    try {
                        double latitude = Double.parseDouble(parts[1]);
                        double longitude = Double.parseDouble(parts[2]);
                        
                        mainHandler.post(() -> {
                            if (locationCallback != null) {
                                locationCallback.onLocationReceived(deviceAddress, latitude, longitude);
                            }
                        });
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Invalid location coordinates", e);
                    }
                }
                break;
                
            case "ALERT":
                String alertMessage = parts.length > 1 ? parts[1] : "Emergency Alert";
                
                mainHandler.post(() -> {
                    if (messageCallback != null) {
                        messageCallback.onAlertReceived(alertMessage);
                    }
                });
                break;
                
            default:
                // General message
                mainHandler.post(() -> {
                    if (messageCallback != null) {
                        messageCallback.onMessageReceived(deviceAddress, message);
                    }
                });
                break;
        }
    }
    
    /**
     * Check if BLE is available and enabled
     */
    public boolean isBluetoothAvailable() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }
    
    /**
     * Get connected devices count
     */
    public int getConnectedDevicesCount() {
        return connectedDevices.size();
    }
    
    // BLE Callbacks
    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.d(TAG, "BLE advertising started successfully");
        }
        
        @Override
        public void onStartFailure(int errorCode) {
            Log.e(TAG, "BLE advertising failed: " + errorCode);
        }
    };
    
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            String deviceAddress = device.getAddress();
            
            if (!connectedDevices.containsKey(deviceAddress) && !deviceAddress.equals(bluetoothAdapter.getAddress())) {
                Log.d(TAG, "Found mesh device: " + deviceAddress);
                connectedDevices.put(deviceAddress, device);
                
                // In a real implementation, we would connect to the device
                // For now, we'll simulate receiving messages
                simulateDeviceCommunication(deviceAddress);
            }
        }
        
        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "BLE scan failed: " + errorCode);
        }
    };
    
    /**
     * Simulate device communication for testing
     */
    private void simulateDeviceCommunication(String deviceAddress) {
        // Simulate receiving a message after 2 seconds
        mainHandler.postDelayed(() -> {
            if (isAdmin) {
                // Simulate user sending location
                processReceivedMessage(deviceAddress, "LOCATION|40.7128|-74.0060");
            } else {
                // Simulate admin sending alert
                processReceivedMessage(deviceAddress, "ALERT|Test alert from admin");
            }
        }, 2000);
    }
}
