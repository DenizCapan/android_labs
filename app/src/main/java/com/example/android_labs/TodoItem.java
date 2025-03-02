package com.example.android_labs;

public class TodoItem {
    private final long id;  // Unique ID from SQLite database
    private final String task;
    private final boolean urgent;

    // Constructor with ID
    public TodoItem(long id, String task, boolean urgent) {
        this.id = id;
        this.task = task;
        this.urgent = urgent;
    }

    // Getters only (Immutable object)
    public long getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public boolean isUrgent() {
        return urgent;
    }
}