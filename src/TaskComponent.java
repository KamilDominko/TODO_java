import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TaskComponent extends JPanel implements ActionListener {
    private JCheckBox checkBox;
    private JTextPane taskField;
    private JButton deleteButton;

    public JTextPane getTaskField() {
        return taskField;
    }

    // ten panel jest używany, abyśmy mogli dokonywać aktualizacji w panelu komponentu zadania podczas usuwania zadań
    private JPanel parentPanel;

    public TaskComponent(JPanel parentPanel) {
        this.parentPanel = parentPanel;

        // pole zadania
        taskField = new JTextPane();
        taskField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskField.setPreferredSize(CommonConstans.TASKFIELD_SIZE);
        taskField.setContentType("text/html");
        taskField.addFocusListener(new FocusListener() {
            // wskazuje, które pole zadania jest aktualnie edytowane
            @Override
            public void focusGained(FocusEvent e) {
                taskField.setBackground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                taskField.setBackground(null);
            }
        });

        // pole wyboru
        checkBox = new JCheckBox();
        checkBox.setPreferredSize(CommonConstans.CHECKBOX_SIZE);
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.addActionListener(this);

        // przycisk usuwania
        deleteButton = new JButton("X");
        deleteButton.setPreferredSize(CommonConstans.DELETE_BUTTON_SIZE);
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this);

        // dodaj do tego komponentu zadania
        add(checkBox);
        add(taskField);
        add(deleteButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkBox.isSelected()){
            // zamienia wszystkie tagi html na pusty ciąg, aby uzyskać główny tekst
            String taskText = taskField.getText().replaceAll("<[^>]*>","");

            // dodaje tekst z przekreśleniem
            taskField.setText("<html><s>" + taskText + "</s><html>");
        } else if (!checkBox.isSelected()){
            String taskText = taskField.getText().replaceAll("<[^>]*>","");

            taskField.setText(taskText);
        }

        if (e.getActionCommand().equalsIgnoreCase("X")){
            // usuń ten komponent z panelu rodzica
            parentPanel.remove(this);
            parentPanel.repaint();
            parentPanel.revalidate();
        }
    }
}
