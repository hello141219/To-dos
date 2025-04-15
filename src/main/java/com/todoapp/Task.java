package com.todoapp;

import java.util.Date;

/**
 * Class representing a todo list task
 */
public class Task {
    private long id;
    private String text;
    private boolean completed;
    private Date createdAt;

    /**
     * Create a new task
     * @param text task description
     */
    public Task(String text) {
        this.id = System.currentTimeMillis();
        this.text = text;
        this.completed = false;
        this.createdAt = new Date();
    }

    /**
     * Full constructor, used for deserialization from JSON
     */
    public Task(long id, String text, boolean completed, Date createdAt) {
        this.id = id;
        this.text = text;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Toggle the completion status of the task
     */
    public void toggleStatus() {
        this.completed = !this.completed;
    }

    @Override
    public String toString() {
        return text + (completed ? " (Completed)" : " (Pending)");
    }
} 