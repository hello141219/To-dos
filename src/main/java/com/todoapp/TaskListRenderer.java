package com.todoapp;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom renderer for the task list
 */
public class TaskListRenderer extends JPanel implements ListCellRenderer<Task> {
    private JLabel taskText;
    private JLabel dateLabel;
    private JCheckBox checkbox;
    private SimpleDateFormat dateFormat;

    /**
     * Create a task list renderer
     */
    public TaskListRenderer() {
        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        checkbox = new JCheckBox();
        checkbox.setOpaque(false);
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        
        taskText = new JLabel();
        dateLabel = new JLabel();
        dateLabel.setFont(new Font(dateLabel.getFont().getName(), Font.ITALIC, 10));
        dateLabel.setForeground(Color.GRAY);
        
        textPanel.add(taskText, BorderLayout.CENTER);
        textPanel.add(dateLabel, BorderLayout.SOUTH);
        
        add(checkbox, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
        
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index,
                                                 boolean isSelected, boolean cellHasFocus) {
        // Set background color
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(index % 2 == 0 ? new Color(240, 240, 240) : list.getBackground());
            setForeground(list.getForeground());
        }
        
        // Set text and checkbox status
        taskText.setText(task.getText());
        dateLabel.setText(formatDate(task.getCreatedAt()));
        checkbox.setSelected(task.isCompleted());
        
        // Set completed task style
        if (task.isCompleted()) {
            taskText.setFont(new Font(taskText.getFont().getName(), Font.ITALIC, taskText.getFont().getSize()));
            taskText.setForeground(Color.GRAY);
        } else {
            taskText.setFont(new Font(taskText.getFont().getName(), Font.PLAIN, taskText.getFont().getSize()));
            taskText.setForeground(list.getForeground());
        }
        
        setEnabled(list.isEnabled());
        setOpaque(true);
        
        return this;
    }
    
    /**
     * Format date
     * @param date date
     * @return formatted date string
     */
    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "";
    }
} 