package pl.kul.todo.components;

import pl.kul.todo.constants.CommonConstans;
import pl.kul.todo.utils.FontsManager;
import pl.kul.todo.utils.JsonFileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Objects;

public class TaskComponent extends JPanel implements ActionListener {
    private final JCheckBox checkBox;
    private final JLabel taskLabel;

    public JLabel getTaskLabel() {
        return taskLabel;
    }

    // ten panel jest używany, abyśmy mogli dokonywać aktualizacji w panelu komponentu zadania podczas usuwania zadań
    private final JPanel parentPanel;
    private final HashMap<String, String> task;
    private final JsonFileManager jsonFileManager;

    public TaskComponent(JPanel parentPanel, HashMap<String, String> task) {
        this.parentPanel = parentPanel;
        this.task = task;
        jsonFileManager = new JsonFileManager(TaskComponent.class.getClassLoader().getResource("data/todos.json").getPath());

        // pole zadania
        taskLabel = new JLabel();
        taskLabel.setFont(new FontsManager().createFont(14f));
        taskLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskLabel.setPreferredSize(CommonConstans.TASKFIELD_SIZE);
        taskLabel.addFocusListener(new FocusListener() {
            // wskazuje, które pole zadania jest aktualnie edytowane
            @Override
            public void focusGained(FocusEvent e) {
                taskLabel.setBackground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                taskLabel.setBackground(null);
            }
        });

        // pole wyboru
        checkBox = new JCheckBox();
        checkBox.setPreferredSize(CommonConstans.CHECKBOX_SIZE);
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.addActionListener(this);

        // przycisk usuwania
        JButton deleteButton = new JButton("X");
        deleteButton.setBackground(Color.BLUE);
        deleteButton.setPreferredSize(CommonConstans.DELETE_BUTTON_SIZE);
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this);

        if (!task.isEmpty()) {
            taskLabel.setText(this.task.get("content").replace("\"", ""));
            boolean isActive = Objects.equals(this.task.get("isActive"), "true");
            checkBox.setSelected(!isActive);

            if (!isActive) {
                taskLabel.setText("<html><s>" + taskLabel.getText() + "</s><html>");
            }
        }

        // dodaj do tego komponentu zadania
        add(checkBox);
        add(taskLabel);
        add(deleteButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkBox.isSelected()) {
            jsonFileManager.changeTaskStatus(task.get("id"), false);

            // dodaje tekst z przekreśleniem
            taskLabel.setText("<html><s>" + taskLabel.getText() + "</s><html>");
        } else if (!checkBox.isSelected()) {
            jsonFileManager.changeTaskStatus(task.get("id"), true);

            String taskText = taskLabel.getText().replaceAll("<[^>]*>", "");
            taskLabel.setText(taskText);
        }

        if (e.getActionCommand().equalsIgnoreCase("X")) {
            jsonFileManager.deleteTask(task.get("id"));

            // usuń ten komponent z panelu rodzica
            parentPanel.remove(this);
            parentPanel.repaint();
            parentPanel.revalidate();
        }
    }
}
