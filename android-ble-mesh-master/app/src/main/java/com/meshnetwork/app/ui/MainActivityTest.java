package com.meshnetwork.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.meshnetwork.app.R;

public class MainActivityTest extends AppCompatActivity {
    
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        
        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(v -> {
            Toast.makeText(this, "App is working! Basic functionality OK.", Toast.LENGTH_LONG).show();
        });
    }
}
