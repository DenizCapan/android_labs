package com.example.android_labs;

public class TodoItem {
    private final String task;
    private final boolean isUrgent;

    public TodoItem(String task, boolean isUrgent) {
        this.task = task;
        this.isUrgent = isUrgent;
    }

    public String getTask() {
        return task;
    }

    public boolean isUrgent() {
        return isUrgent;
    }
}
