package com.todoapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Main class for the todo list application, implementing the GUI interface
 */
public class TodoApp extends JFrame {
    private TaskManager taskManager;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private JTextField taskInput;
    private JLabel statusLabel;
    private JFileChooser fileChooser;

    /**
     * Create and initialize the application
     */
    public TodoApp() {
        // Set window
        setTitle("Todo List");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize task manager
        taskManager = new TaskManager();
        
        // Create UI
        createUI();
        
        // Refresh task list
        refreshTaskList();
        
        // Initialize file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON 文件", "json"));
    }

    /**
     * Create user interface
     */
    private void createUI() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // North - Title and input area
        JPanel northPanel = new JPanel(new BorderLayout(0, 10));
        
        JLabel titleLabel = new JLabel("Todo List", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        northPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Task input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        taskInput = new JTextField();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addTask());
        
        inputPanel.add(taskInput, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        northPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Middle - Task list
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskListRenderer());
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Mouse click event, handle task selection status and deletion
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Task task = listModel.getElementAt(index);
                    Rectangle cellBounds = taskList.getCellBounds(index, index);
                    
                    // Check if the click is on the checkbox area (approximately the first 40 pixels)
                    if (e.getX() < cellBounds.x + 40) {
                        taskManager.toggleTaskStatus(task.getId());
                        refreshTaskList();
                    } 
                    // Double-click to delete the task
                    else if (e.getClickCount() == 2) {
                        deleteTask(index);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // South - Status bar and buttons
        JPanel southPanel = new JPanel(new BorderLayout(0, 10));
        
        // Status bar
        statusLabel = new JLabel();
        updateStatusLabel();
        southPanel.add(statusLabel, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                deleteTask(selectedIndex);
            }
        });
        
        JButton importButton = new JButton("Import JSON");
        importButton.addActionListener(e -> importTasks());
        
        JButton exportButton = new JButton("Export JSON");
        exportButton.addActionListener(e -> exportTasks());
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to clear all tasks?", "Confirm clear", 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                
                for (int i = listModel.size() - 1; i >= 0; i--) {
                    taskManager.deleteTask(listModel.getElementAt(i).getId());
                }
                refreshTaskList();
            }
        });
        
        buttonPanel.add(deleteButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(clearButton);
        
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Add to main panel
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Add to window
        add(mainPanel);
    }

    /**
     * Add a new task
     */
    private void addTask() {
        String text = taskInput.getText().trim();
        if (!text.isEmpty()) {
            taskManager.addTask(text);
            taskInput.setText("");
            refreshTaskList();
            taskInput.requestFocus();
        }
    }

    /**
     * Delete a task
     * @param index the index of the task in the list
     */
    private void deleteTask(int index) {
        Task task = listModel.getElementAt(index);
        if (JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete the task \"" + task.getText() + "\"?", 
            "Confirm delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            
            taskManager.deleteTask(task.getId());
            refreshTaskList();
        }
    }

    /**
     * Import tasks
     */
    private void importTasks() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (taskManager.importFromFile(file.getPath())) {
                refreshTaskList();
                JOptionPane.showMessageDialog(this, "Import tasks successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Import tasks failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Export tasks
     */
    private void exportTasks() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getPath();
            if (!path.toLowerCase().endsWith(".json")) {
                path += ".json";
            }
            if (taskManager.exportToFile(path)) {
                JOptionPane.showMessageDialog(this, "Export tasks successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Export tasks failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Refresh task list
     */
    private void refreshTaskList() {
        listModel.clear();
        for (Task task : taskManager.getAllTasks()) {
            listModel.addElement(task);
        }
        updateStatusLabel();
    }

    /**
     * Update status label
     */
    private void updateStatusLabel() {
        int total = taskManager.getAllTasks().size();
        int completed = taskManager.getCompletedTaskCount();
        int pending = taskManager.getPendingTaskCount();
        statusLabel.setText(String.format("Total: %d tasks | Completed: %d | Pending: %d", total, completed, pending));
    }

    /**
     * Application entry
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Set the appearance to the native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            TodoApp app = new TodoApp();
            app.setVisible(true);
        });
    }
} 