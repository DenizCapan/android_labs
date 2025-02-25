package com.example.android_labs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText inputTask;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch urgentSwitch;
    private final List<TodoItem> todoList = new ArrayList<>();
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        ListView listView = findViewById(R.id.listView);
        inputTask = findViewById(R.id.inputTask);
        urgentSwitch = findViewById(R.id.urgentSwitch);
        Button addButton = findViewById(R.id.addButton);

        // Set up adapter for ListView
        adapter = new TodoAdapter(this, todoList);
        listView.setAdapter(adapter);

        // Handle "ADD" button click
        addButton.setOnClickListener(v -> {
            String taskText = inputTask.getText().toString().trim();
            if (!taskText.isEmpty()) {
                boolean isUrgent = urgentSwitch.isChecked();
                todoList.add(new TodoItem(taskText, isUrgent));
                adapter.notifyDataSetChanged();  // Refresh ListView
                inputTask.setText("");  // Clear input field
                urgentSwitch.setChecked(false);  // Reset switch

                addButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            }
        });

        // Handle long click to delete a task
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteDialog(position);
            return true;
        });
    }

    // Show a confirmation dialog before deleting a task
    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_task)) // Dialog title
                .setMessage(getString(R.string.delete_message) + " " + todoList.get(position).getTask()) // Dialog message
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    todoList.remove(position);  // Remove item from list
                    adapter.notifyDataSetChanged(); // Refresh ListView
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss()) // Just close dialog
                .create()
                .show();
    }

}

