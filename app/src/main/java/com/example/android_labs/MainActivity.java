package com.example.android_labs;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"; // Debugging Tag

    private EditText inputTask;
    private SwitchCompat urgentSwitch;
    private List<TodoItem> todoList;
    private TodoAdapter adapter;
    private TodoDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        ListView listView = findViewById(R.id.listView);
        inputTask = findViewById(R.id.inputTask);
        urgentSwitch = findViewById(R.id.urgentSwitch);
        Button addButton = findViewById(R.id.addButton);

        // Initialize database helper
        dbHelper = new TodoDatabaseHelper(this);
        todoList = new ArrayList<>();

        // Initialize the adapter BEFORE loading tasks
        adapter = new TodoAdapter(this, todoList);
        listView.setAdapter(adapter);

        // Load saved tasks from database
        loadTasksFromDatabase();

        // Add button click event
        addButton.setOnClickListener(v -> addNewTask());

        // Long click to delete an item
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteDialog(position);
            return true;
        });
    }

    private void loadTasksFromDatabase() {
        todoList.clear(); // Prevent duplicate entries

        Cursor cursor = dbHelper.getAllTasks();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String taskText = cursor.getString(1);
                    boolean isUrgent = cursor.getInt(2) == 1;
                    todoList.add(new TodoItem(id, taskText, isUrgent));
                }
            } finally {
                cursor.close(); // Ensure cursor is closed properly to prevent memory leaks
            }
        } else {
            Log.e(TAG, "Cursor is null. Could not load tasks.");
        }

        if (adapter != null) {
            adapter.updateList(todoList); // ✅ Refresh UI safely
        } else {
            Log.e(TAG, "Adapter is NULL! Can't update list.");
        }
    }

    private void addNewTask() {
        String taskText = inputTask.getText().toString().trim();
        boolean isUrgent = urgentSwitch.isChecked();

        if (taskText.isEmpty()) {
            inputTask.setError("Task cannot be empty!");
            return; // Exit if input is empty
        }

        // Insert task into database
        long newTaskId = dbHelper.insertTask(taskText, isUrgent);
        if (newTaskId != -1) {
            todoList.add(new TodoItem((int) newTaskId, taskText, isUrgent));
            adapter.notifyDataSetChanged(); // ✅ Refresh UI
            inputTask.setText(""); // Clear input field
        } else {
            Log.e(TAG, "Failed to insert task into database.");
        }
    }

    private void showDeleteDialog(int position) {
        TodoItem item = todoList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_task))
                .setMessage(getString(R.string.delete_message) + " " + item.getTask())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> deleteTask(position, item))
                .setNegativeButton(getString(R.string.no), null)
                .create()
                .show();
    }

    private void deleteTask(int position, TodoItem item) {
        int rowsDeleted = dbHelper.deleteTask(item.getId());
        if (rowsDeleted > 0) {
            todoList.remove(position);
            adapter.notifyDataSetChanged(); // ✅ Ensure UI updates after deletion
        } else {
            Log.e(TAG, "Failed to delete task ID: " + item.getId());
        }
    }
}