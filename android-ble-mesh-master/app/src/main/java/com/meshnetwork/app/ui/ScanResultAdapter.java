package com.meshnetwork.app.ui;

import android.bluetooth.le.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meshnetwork.app.R;

import java.util.List;

/**
 * Adapter for displaying BLE scan results
 */
public class ScanResultAdapter extends RecyclerView.Adapter<ScanResultAdapter.ScanResultViewHolder> {
    
    private List<ScanResult> scanResults;
    private OnDeviceClickListener onDeviceClickListener;
    
    public interface OnDeviceClickListener {
        void onDeviceClick(ScanResult scanResult);
    }
    
    public ScanResultAdapter(List<ScanResult> scanResults, OnDeviceClickListener listener) {
        this.scanResults = scanResults;
        this.onDeviceClickListener = listener;
    }
    
    @NonNull
    @Override
    public ScanResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_result, parent, false);
        return new ScanResultViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ScanResultViewHolder holder, int position) {
        ScanResult scanResult = scanResults.get(position);
        holder.bind(scanResult, onDeviceClickListener);
    }
    
    @Override
    public int getItemCount() {
        return scanResults.size();
    }
    
    static class ScanResultViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceIdPlaceholder;
        private TextView deviceId;
        private TextView inputTextPlaceholder;
        private TextView inputText;
        private TextView outputTextPlaceholder;
        private TextView outputText;
        private TextView messageToSend;
        
        public ScanResultViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceIdPlaceholder = itemView.findViewById(R.id.device_id_placeholder);
            deviceId = itemView.findViewById(R.id.device_id);
            inputTextPlaceholder = itemView.findViewById(R.id.inputText_placeholder);
            inputText = itemView.findViewById(R.id.inputText);
            outputTextPlaceholder = itemView.findViewById(R.id.outputText_placeholder);
            outputText = itemView.findViewById(R.id.outputText);
            messageToSend = itemView.findViewById(R.id.message_to_send);
        }
        
        public void bind(ScanResult scanResult, OnDeviceClickListener listener) {
            String deviceAddress = scanResult.getDevice().getAddress();
            String deviceName = scanResult.getDevice().getName();
            
            if (deviceName == null || deviceName.isEmpty()) {
                deviceName = "Unknown Device";
            }
            
            deviceId.setText(deviceName + " (" + deviceAddress + ")");
            inputText.setText("RSSI: " + scanResult.getRssi() + " dBm");
            outputText.setText("Ready for communication");
            messageToSend.setText("");
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeviceClick(scanResult);
                }
            });
        }
    }
}
