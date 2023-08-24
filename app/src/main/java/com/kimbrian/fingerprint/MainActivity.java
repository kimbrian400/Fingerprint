package com.kimbrian.fingerprint;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize executor
        executor = Executors.newSingleThreadExecutor();

        // Create a BiometricPrompt instance
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Fingerprint authentication succeeded; allow access to the app.
                // You can put your app's logic here.
                // Start the HomeActivity upon successful authentication
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                  startActivity(intent);
                    finish(); // Optionally finish the MainActivity if you don't want to go back to it.
                }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Fingerprint authentication failed.
                    // Display a message to the user indicating the failure
                    Toast.makeText(MainActivity.this, "Fingerprint authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                // Launch the PIN input activity
                Intent intent = new Intent(MainActivity.this, PinInputActivity.class);
                startActivity(intent);
                finish(); // Optionally finish the MainActivity if you don't want to go back to it.
            }
        });

        // Configure the authentication prompt
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Authentication")
                .setSubtitle("Unlock the app with your fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        // Trigger the fingerprint authentication dialog
        findViewById(R.id.authenticateButton).setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }
}