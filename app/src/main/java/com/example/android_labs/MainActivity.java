package com.example.android_labs;


import android.annotation.SuppressLint;
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
    // Debugging Tag
    private EditText inputTask;
    private SwitchCompat urgentSwitch;
    private List<TodoItem> todoList;
    private TodoAdapter adapter;
    private TodoDatabaseHelper dbHelper;

    @SuppressLint("WrongViewCast")

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

        // Load saved tasks from database
        loadTasksFromDatabase();

        // Set up adapter
        adapter = new TodoAdapter(this, todoList);
        listView.setAdapter(adapter);

        // Add button click event
        addButton.setOnClickListener(v -> {
            String taskText = inputTask.getText().toString().trim();
            boolean isUrgent = urgentSwitch.isChecked();

            if (!taskText.isEmpty()) {
                // Insert task into database
                long newTaskId = dbHelper.insertTask(taskText, isUrgent);
                if (newTaskId != -1) {
                    // Add to the list and refresh UI
                    todoList.add(new TodoItem((int) newTaskId, taskText, isUrgent));
                    adapter.notifyDataSetChanged();
                    inputTask.setText(""); // Clear input field

                } else {
                    Log.e("DB_ERROR", "Failed to insert task into database");
                }
            }
        });

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
            dbHelper.printCursor(cursor); // ✅ Corrected call to printCursor()

            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String taskText = cursor.getString(1);
                boolean isUrgent = cursor.getInt(2) == 1;
                todoList.add(new TodoItem((int) id, taskText, isUrgent));
            }
            cursor.close(); // Prevent memory leak
        }
        adapter.notifyDataSetChanged(); // ✅ Ensure UI refresh
    }

    private void showDeleteDialog(int position) {
        TodoItem item = todoList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_task))
                .setMessage(getString(R.string.delete_message) + " " + item.getTask())
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    int rowsDeleted = dbHelper.deleteTask(item.getId()); // ✅ Check if deletion was successful
                    if (rowsDeleted > 0) {
                        todoList.remove(position); // Remove from list
                        adapter.notifyDataSetChanged(); // Refresh UI
                    } else {
                        Log.e("DB_ERROR", "Failed to delete task ID: " + item.getId());
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
                .show();
    }
}

