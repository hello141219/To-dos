package com.todoapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Task management class, responsible for task list processing and JSON storage
 */
public class TaskManager {
    private List<Task> tasks;
    private static final String DEFAULT_FILE_PATH = "tasks.json";
    private final Gson gson;

    /**
     * Create a task manager and load tasks from the default file (if it exists)
     */
    public TaskManager() {
        tasks = new ArrayList<>();
        gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        loadFromFile(DEFAULT_FILE_PATH);
    }

    /**
     * Add a new task
     * @param text task description
     * @return the newly created task
     */
    public Task addTask(String text) {
        Task task = new Task(text);
        tasks.add(task);
        saveToFile(DEFAULT_FILE_PATH);
        return task;
    }

    /**
     * Delete a task
     * @param id the ID of the task to delete
     * @return whether the deletion is successful
     */
    public boolean deleteTask(long id) {
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (removed) {
            saveToFile(DEFAULT_FILE_PATH);
        }
        return removed;
    }

    /**
     * Toggle the completion status of a task
     * @param id the ID of the task to toggle
     * @return whether the status toggle is successful
     */
    public boolean toggleTaskStatus(long id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.toggleStatus();
                saveToFile(DEFAULT_FILE_PATH);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all tasks
     * @return the task list
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Get the number of pending tasks
     * @return the number of pending tasks
     */
    public int getPendingTaskCount() {
        return (int) tasks.stream().filter(task -> !task.isCompleted()).count();
    }

    /**
     * Get the number of completed tasks
     * @return the number of completed tasks
     */
    public int getCompletedTaskCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }

    /**
     * Save the task list to a JSON file
     * @param filePath file path
     * @return whether the saving is successful
     */
    public boolean saveToFile(String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(tasks, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load the task list from a JSON file
     * @param filePath file path
     * @return whether the loading is successful
     */
    public boolean loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }

        try (Reader reader = new FileReader(filePath)) {
            Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> loadedTasks = gson.fromJson(reader, taskListType);
            
            if (loadedTasks != null) {
                tasks = loadedTasks;
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error loading tasks from file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Import tasks from a custom path JSON file
     * @param filePath file path
     * @return whether the import is successful
     */
    public boolean importFromFile(String filePath) {
        boolean success = loadFromFile(filePath);
        if (success) {
            saveToFile(DEFAULT_FILE_PATH); // Save to default file
        }
        return success;
    }
    
    /**
     * Export tasks to a custom path JSON file
     * @param filePath file path
     * @return whether the export is successful
     */
    public boolean exportToFile(String filePath) {
        return saveToFile(filePath);
    }
} 