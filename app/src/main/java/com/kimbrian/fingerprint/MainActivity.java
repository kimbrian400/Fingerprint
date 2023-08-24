package com.kimbrian.fingerprint;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_BIOMETRIC_PERMISSION = 100;
    private ImageView fingerprintIcon;
    private TextView unlockText;
    private ImageView animationView;
    private FingerprintManagerCompat fingerprintManager;
    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = findViewById(R.id.layout);
        fingerprintIcon = findViewById(R.id.fingerprintIcon);
        unlockText = findViewById(R.id.unlockText);
        animationView = findViewById(R.id.animationView);

        // Initialize FingerprintManagerCompat
        fingerprintManager = FingerprintManagerCompat.from(this);

        // Check for biometric hardware and permission
        if (!fingerprintManager.isHardwareDetected() || ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            // Biometric authentication is not available, handle this case accordingly
            // You can hide the fingerprint UI and provide an alternative method for authentication
            fingerprintIcon.setVisibility(View.GONE);
            unlockText.setText("Biometric not available");
        } else {
            // Request the biometric permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_BIOMETRIC}, REQUEST_BIOMETRIC_PERMISSION);
            }
        }

        // Set an onClickListener for the fingerprint icon to start the animation and authenticate
        fingerprintIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUnlockAnimation();
                startFingerprintAuthentication();
            }
        });
    }

    private void startUnlockAnimation() {
        animationView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fingerprint_animation);
        animationView.startAnimation(animation);
    }

    private void startFingerprintAuthentication() {
        // Create a cancellation signal to stop authentication if needed
        cancellationSignal = new CancellationSignal();

        // Create a callback for fingerprint authentication
        FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                // Handle authentication errors here
                // You can display an error message or take appropriate action
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                // Handle authentication help messages (e.g., "Move your finger")
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                // Fingerprint authentication succeeded, you can unlock the account or perform your action
                // You can also dismiss the animation here
                animationView.clearAnimation();
                animationView.setVisibility(View.INVISIBLE);

                // Handle the successful authentication
            }

            @Override
            public void onAuthenticationFailed() {
                // Handle authentication failure (e.g., wrong fingerprint)
            }
        };

        // Start fingerprint authentication
        fingerprintManager.authenticate(null, 0, cancellationSignal, authenticationCallback, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cancel the fingerprint authentication when the activity is stopped
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BIOMETRIC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with fingerprint authentication
            } else {
                // Permission denied, handle this case accordingly
                fingerprintIcon.setVisibility(View.GONE);
                unlockText.setText("Permission denied for biometric");
            }
        }
    }
}