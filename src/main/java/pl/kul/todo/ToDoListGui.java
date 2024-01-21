package pl.kul.todo;

import pl.kul.todo.components.TaskComponent;
import pl.kul.todo.constants.CommonConstans;
import pl.kul.todo.utils.FontsManager;
import pl.kul.todo.utils.JsonFileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ToDoListGui extends JFrame implements ActionListener {
    private JPanel taskComponentPanel;
    private JTextField taskField;
    private final JsonFileManager jsonFileManager;

    public ToDoListGui() {
        super("ToDoApp");
        jsonFileManager = new JsonFileManager(ToDoListGui.class.getClassLoader().getResource("data/todos.json").getPath());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(CommonConstans.GUI_SIZE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // tekst banera
        JLabel bannerLabel = new JLabel("TODO List");
        bannerLabel.setFont(new FontsManager().createFont(36f));
        bannerLabel.setBounds((CommonConstans.GUI_SIZE.width - bannerLabel.getPreferredSize().width) / 2,
                15, CommonConstans.BANNER_SIZE.width, CommonConstans.BANNER_SIZE.height);

        // taskpanel
        JPanel taskPanel = new JPanel();

        // taskcomponentpanel
        taskComponentPanel = new JPanel();
        taskComponentPanel.setLayout(new BoxLayout(taskComponentPanel, BoxLayout.Y_AXIS));
        taskPanel.add(taskComponentPanel);

        getTaskComponents();

        // dodawanie przewijania do panelu zadań
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBounds(5, 70, CommonConstans.TASKPANEL_SIZE.width, CommonConstans.TASKPANEL_SIZE.height - 100);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setMaximumSize(CommonConstans.TASKPANEL_SIZE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // zmiana prędkości paska przewijania
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20);

        // Tworzenie pola do wpisania treści zadania
        taskField = new JTextField();
        taskField.setFont(new FontsManager().createFont(18f));
        taskField.setBounds(CommonConstans.GUI_SIZE.width / 10, CommonConstans.GUI_SIZE.height - 168,
                CommonConstans.ADDTASK_BUTTON_SIZE.width, CommonConstans.ADDTASK_BUTTON_SIZE.height);

        // Tworzenie przycisku dodawania zadań
        JButton addTaskButton = new JButton("Add TODO");
        addTaskButton.setFont(new FontsManager().createFont(18f));
        addTaskButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addTaskButton.setBounds(CommonConstans.GUI_SIZE.width / 10, CommonConstans.GUI_SIZE.height - 108,
                CommonConstans.ADDTASK_BUTTON_SIZE.width, CommonConstans.ADDTASK_BUTTON_SIZE.height);
        addTaskButton.addActionListener(this);

        // dodaj do ramki
        this.getContentPane().add(bannerLabel);
        this.getContentPane().add(scrollPane);
        this.getContentPane().add(taskField);
        this.getContentPane().add(addTaskButton);
    }

    private void getTaskComponents() {
        HashMap<String, HashMap<String, String>> todos = jsonFileManager.readFromFile();
        todos.forEach((key, value) -> {
            createTodoComponent(value);
        });
    }

    private void createTodoComponent(HashMap<String, String> task) {
        TaskComponent taskComponent = new TaskComponent(taskComponentPanel, task);
        taskComponentPanel.add(taskComponent);

        if (taskComponentPanel.getComponentCount() > 1) {
            TaskComponent previousTask = (TaskComponent) taskComponentPanel.getComponent(taskComponentPanel.getComponentCount() - 2);
            JLabel taskLabel = previousTask.getTaskLabel();
            taskLabel.setBackground(null);
        }

        // skupienie na polu zadania po utworzeniu
        taskComponent.getTaskLabel().requestFocus();
        repaint();
        revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("Add TODO")) {
            HashMap<String, String> task = new HashMap<>();
            int newTaskId = jsonFileManager.getTasksLength() + 1;

            task.put("id", String.valueOf(newTaskId));
            task.put("content", taskField.getText());
            task.put("isActive", String.valueOf(true));
            createTodoComponent(task);

            jsonFileManager.addTaskToFile("task" + String.valueOf(newTaskId), task);
            taskField.setText("");
        }
    }
}
