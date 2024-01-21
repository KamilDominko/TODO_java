import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JsonFileManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFileManagerTest {

    private static final String TEST_FILE_PATH = "/Users/yevheniisoliuk/Desktop/java-project/TODO_java/src/test/resources/testTasks.json";
    private JsonFileManager jsonFileManager;

    @BeforeEach
    void setUp() {
        jsonFileManager = new JsonFileManager(TEST_FILE_PATH);
        jsonFileManager.deleteAll();
        createSampleJsonFile();
    }

    @Test
    void testGetTasksLength() {
        int tasksLength = jsonFileManager.getTasksLength();
        assertEquals(2, tasksLength, "Expected tasks length to be 2");
    }

    @Test
    void testReadFromFile() {
        HashMap<String, HashMap<String, String>> flashcards = jsonFileManager.readFromFile();

        // Check if the expected content is present
        assertTrue(flashcards.containsKey("task1"));
        assertTrue(flashcards.containsKey("task2"));

        // Check the content of one task
        HashMap<String, String> task1Content = flashcards.get("task1");
        assertEquals("1", task1Content.get("id"));
        assertEquals("Task 1 Description", task1Content.get("content"));
        assertEquals("true", task1Content.get("isActive"));
    }

    @Test
    void testAddTaskToFile() {
        // Add a new task
        HashMap<String, String> newTaskContent = new HashMap<>();
        newTaskContent.put("id", "1");
        newTaskContent.put("content", "New Task Description");
        newTaskContent.put("isActive", "false");
        jsonFileManager.addTaskToFile("newTask", newTaskContent);

        // Verify that the new task has been added
        HashMap<String, HashMap<String, String>> flashcards = jsonFileManager.readFromFile();
        assertTrue(flashcards.containsKey("newTask"));
        assertEquals("New Task Description", flashcards.get("newTask").get("content"));
    }

    @Test
    void testChangeTaskStatus() {
        // Change the status of an existing task
        jsonFileManager.changeTaskStatus("1", false);

        // Verify that the task status has been changed
        HashMap<String, HashMap<String, String>> flashcards = jsonFileManager.readFromFile();
        assertFalse(Boolean.parseBoolean(flashcards.get("task1").get("isActive")));
    }

    @Test
    void testDeleteTask() {
        // Delete an existing task
        jsonFileManager.deleteTask("1");

        // Verify that the task has been deleted
        HashMap<String, HashMap<String, String>> flashcards = jsonFileManager.readFromFile();
        assertFalse(flashcards.containsKey("task1"));
    }

    // Helper method to create a sample JSON file for testing
    private void createSampleJsonFile() {
        try {
            File testFile = new File(TEST_FILE_PATH);
            if (testFile.exists() || testFile.createNewFile()) {
                jsonFileManager.addTaskToFile("task1", createTaskContent("1", "Task 1 Description", "true"));
                jsonFileManager.addTaskToFile("task2", createTaskContent("2", "Task 2 Description", "false"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to create task content for testing
    private HashMap<String, String> createTaskContent(String id, String content, String isActive) {
        HashMap<String, String> task = new HashMap<>();

        task.put("id", id);
        task.put("content", content);
        task.put("isActive", isActive);

        return task;
    }
}

