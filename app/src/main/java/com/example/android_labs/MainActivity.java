package com.example.android_labs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Components
        ImageButton imageButton = findViewById(R.id.imageButton);
        Button button = findViewById(R.id.button);
        EditText editText = findViewById(R.id.editText);
        TextView textView = findViewById(R.id.textView);
        CheckBox checkBox = findViewById(R.id.checkBox);

        // Detect current language and set the correct flag
        String currentLang = Locale.getDefault().getLanguage();
        if (currentLang.equals("fr")) {
            imageButton.setImageResource(R.drawable.flag_france); // French flag
        } else {
            imageButton.setImageResource(R.drawable.flag_usa); // US flag
        }
        // Button Click Listener: Updates TextView & Shows Toast
        button.setOnClickListener(v -> {
            String text = editText.getText().toString();
            textView.setText(text);
            Toast.makeText(MainActivity.this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
        });

        // Checkbox Change Listener: Shows Snack_bar with Undo option
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String message = getString(R.string.snack_bar) + " " + (isChecked ? "on" : "off");
            Snackbar.make(buttonView, message, Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> checkBox.setChecked(!isChecked))
                    .show();
        });
    }
}

