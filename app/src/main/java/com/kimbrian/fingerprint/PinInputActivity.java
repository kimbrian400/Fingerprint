package com.kimbrian.fingerprint;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class PinInputActivity extends AppCompatActivity {

    private EditText pinEditText;
    private Button submitPinButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);

        pinEditText = findViewById(R.id.pinEditText);
        submitPinButton = findViewById(R.id.submitPinButton);

        submitPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the PIN entered by the user
                String enteredPin = pinEditText.getText().toString();

                // Check the entered PIN for correctness (you should have your own logic here)
                if (enteredPin.equals("1234")) { // Replace "1234" with your actual PIN check logic
                    // PIN is correct, proceed to the authenticated activity (e.g., HomeActivity)
                    Intent intent = new Intent(PinInputActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Incorrect PIN, show an error message
                    pinEditText.setError("Incorrect PIN. Please try again.");
                }
            }
        });
    }
}
