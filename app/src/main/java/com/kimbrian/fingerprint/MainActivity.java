package com.kimbrian.fingerprint;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.os.Bundle;

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
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Fingerprint authentication failed.
                // Handle this case as needed.
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