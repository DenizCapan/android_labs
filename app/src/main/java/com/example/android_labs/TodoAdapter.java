package com.example.android_labs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.util.List;

public class TodoAdapter extends BaseAdapter {
    private final Context context;
    private List<TodoItem> todoList;

    // Constructor
    public TodoAdapter(Context context, List<TodoItem> todoList) {
        this.context = context;
        this.todoList = (todoList != null) ? todoList : new java.util.ArrayList<>();
    }

    @Override
    public int getCount() {
        return (todoList != null) ? todoList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (todoList != null) ? todoList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (todoList != null) ? todoList.get(position).getId() : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.taskText = convertView.findViewById(R.id.taskText);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current TodoItem
        TodoItem item = todoList.get(position);

        if (item != null) {
            holder.taskText.setText(item.getTask());

            // Set background color based on urgency
            if (item.isUrgent()) {
                convertView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
                holder.taskText.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            } else {
                convertView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                holder.taskText.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        } else {
            holder.taskText.setText(context.getString(R.string.error_task_null));
        }

        return convertView;
    }

    // Update list method
    public void updateList(List<TodoItem> newList) {
        this.todoList = newList != null ? newList : new java.util.ArrayList<>();
        notifyDataSetChanged();
    }

    // ViewHolder pattern for performance
    private static class ViewHolder {
        TextView taskText;
    }
}