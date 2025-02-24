package com.example.android_labs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button btnDontCallMe = findViewById(R.id.button_no_call);
        Button btnThankYou = findViewById(R.id.button_thank_you);

        // Get the username from intent
        String username = getIntent().getStringExtra("USERNAME");
        welcomeTextView.setText(String.format("%s %s!", getString(R.string.welcome), username));

        // If "Don't Call Me That" is clicked, return result 0
        btnDontCallMe.setOnClickListener(v -> {
            setResult(0);
            finish();
        });

        // If "Thank You" is clicked, return result 1 and finish
        btnThankYou.setOnClickListener(v -> {
            setResult(1);
            finish();
        });
    }
}