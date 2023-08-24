package com.kimbrian.fingerprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private ImageView fingerprintIcon;
    private TextView unlockText;
    private ImageView animationView;
    private FingerprintManagerCompat fingerprintManager;
    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
        fingerprintIcon = findViewById(R.id.fingerprintIcon);
        unlockText = findViewById(R.id.unlockText);
        animationView = findViewById(R.id.animationView);

        // Initialize FingerprintManagerCompat
        fingerprintManager = FingerprintManagerCompat.from(this);

        // Check if the device has a fingerprint sensor and if the app has permission
        if (!fingerprintManager.isHardwareDetected() || ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            // Fingerprint authentication is not available, handle this case accordingly
            // You can hide the fingerprint UI and provide an alternative method for authentication
            fingerprintIcon.setVisibility(View.GONE);
            unlockText.setText("Fingerprint not available");
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

}