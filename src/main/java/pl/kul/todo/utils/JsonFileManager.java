package pl.kul.todo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class JsonFileManager {
    private HashMap<String, HashMap<String, String>> flashcards;
    private HashMap<String, String> content;
    private final String filePath;

    public JsonFileManager(String filePath) {
        this.filePath = filePath;
    }

    public int getTasksLength() {
        int tasksLength = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            tasksLength = rootNode.size();
        } catch (IOException e) {
            System.err.println("Error writing to the JSON file: " + e.getMessage());
        }

        return tasksLength;
    }

    public HashMap<String, HashMap<String, String>> readFromFile() {
        flashcards = new HashMap<>();

        try {
            // Read the existing JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Convert JsonNode fields to HashMap
            if (((JsonNode) rootNode).isObject()) {
                rootNode.fields().forEachRemaining(node -> {
                    String key = node.getKey();
                    if (((JsonNode) node.getValue()).isObject()) {
                        content = new HashMap<>();
                        node.getValue().fields().forEachRemaining(field -> {
                            content.put(field.getKey(), field.getValue().toString().replace("\"", ""));
                        });
                        flashcards.put(key, content);
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Error writing to the JSON file: " + e.getMessage());
        }

        return flashcards;
    }

    public void addTaskToFile(String key, HashMap<String, String> content) {
        try {
            // Read the existing JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Add task to file
            if (((JsonNode) rootNode).isObject()) {
                ObjectNode objectNode = (ObjectNode) rootNode;
                ObjectNode contentNode = new ObjectNode(JsonNodeFactory.instance);
                content.forEach(contentNode::put);
                objectNode.set(key, contentNode);
            }

            // Write the modified JSON back to the file
            objectMapper.writeValue(new File(filePath), rootNode);
        } catch (IOException e) {
            System.err.println("Error writing to the JSON file: " + e.getMessage());
        }
    }

    public void changeTaskStatus(String id, boolean status) {
        try {
            // Read the existing JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Change task status by id
            if (((JsonNode) rootNode).isObject()) {
                rootNode.fields().forEachRemaining(field -> {
                    if (((JsonNode) rootNode).isObject()) {
                        String taskId = objectMapper.convertValue(field.getValue().get("id"), String.class);
                        if (Objects.equals(taskId, id)) {
                            field.getValue().fields().forEachRemaining(item -> {
                                if (Objects.equals(item.getKey(), "isActive")) {
                                    item.setValue(objectMapper.convertValue(status, JsonNode.class));
                                }
                            });
                        }
                    }
                });
            }

            // Write the modified JSON back to the file
            objectMapper.writeValue(new File(filePath), rootNode);
        } catch (IOException e) {
            System.err.println("Error writing to the JSON file: " + e.getMessage());
        }
    }

    public void deleteTask(String id) {
        try {
            // Read the existing JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Remove task by id from file
            if (((JsonNode) rootNode).isObject()) {
                ObjectNode objectNode = (ObjectNode) rootNode;
                objectNode.remove("task" + id);
            }

            // Write the modified JSON back to the file
            objectMapper.writeValue(new File(filePath), rootNode);
        } catch (IOException e) {
            System.err.println("Error writing to the JSON file: " + e.getMessage());
        }
    }

    public void deleteAll() {
        try {
            // Read the existing JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Remove task by id from file
            if (((JsonNode) rootNode).isObject()) {
                ObjectNode objectNode = (ObjectNode) rootNode;
                objectNode.removeAll();
            }

            // Write the modified JSON back to the file
            objectMapper.writeValue(new File(filePath), rootNode);
        } catch (IOException e) {
            System.err.println("Error writing to the JSON file: " + e.getMessage());
        }
    }
}
