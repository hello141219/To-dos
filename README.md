# Todo List Application (Java Version)

A simple Todo List application developed with Java Swing, using JSON for data storage.

## Features

- Add new tasks
- Mark tasks as completed/uncompleted (click checkbox in front of task)
- Delete tasks (double-click task or select and click delete button)
- Display task statistics
- Import/Export JSON data files
- Clear all tasks

## Getting Started

### Prerequisites

Ensure your system has:
- Java JDK 11 or higher
- Maven build tool

### Build and Run

1. Clone or download this repository
2. Run the following command in the project root directory to compile and package the application:

```bash
mvn clean package
```

3. Run the generated JAR file:

```bash
java -jar target/todo-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Usage Instructions

- **Add Task**: Enter task content in the text box, then click "Add" button or press Enter
- **Complete Task**: Click the checkbox in front of the task
- **Delete Task**: Double-click the task or select the task and click "Delete" button
- **Import/Export**: Use corresponding buttons to import or export task data in JSON format
- **Clear Tasks**: Click "Clear" button to delete all tasks

## Project Structure

```
├── src/main/java/com/todoapp/
│   ├── Task.java           # Task model class
│   ├── TaskManager.java    # Task management and JSON processing class
│   ├── TaskListRenderer.java # Custom list renderer
│   └── TodoApp.java        # Main application class and GUI implementation
├── pom.xml                 # Maven configuration file
└── README.md              # Project documentation
```

## Technology Stack

- Java 11+
- Swing (GUI)
- Gson (JSON processing)
- Maven (Build tool)

## Data Storage

The application saves data by default in a `tasks.json` file in the current directory. You can also use custom file paths through the import/export functionality.
