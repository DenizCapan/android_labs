package com.example.android_labs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_NAME = "username";
    private static final int REQUEST_CODE = 1; // Code for startActivityForResult

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        Button btnNext = findViewById(R.id.button_next);

        // Load saved name from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedName = preferences.getString(KEY_NAME, "");
        editTextName.setText(savedName);

        // Handle button click
        btnNext.setOnClickListener(v -> {
            String username = editTextName.getText().toString();
            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra("USERNAME", username);
            startActivityForResult(intent, REQUEST_CODE); // Start NameActivity for result
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save name to SharedPreferences when activity pauses
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NAME, editTextName.getText().toString());
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case 0:
                    // User wants to change their name (do nothing)
                    break;
                case 1:
                    // User is happy, close the app
                    finish();
                    break;
            }
        }
    }
}