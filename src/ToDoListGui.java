import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ToDoListGui extends JFrame implements ActionListener {
    private JPanel taskPanel, taskComponentPanel;

    public ToDoListGui() {
        super("ToDoApp");
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
        bannerLabel.setFont(createFont("LEMONMILK-Light.otf", 36f));
        bannerLabel.setBounds((CommonConstans.GUI_SIZE.width - bannerLabel.getPreferredSize().width) / 2,
                15, CommonConstans.BANNER_SIZE.width, CommonConstans.BANNER_SIZE.height);

        // taskpanel
        taskPanel = new JPanel();

        // taskcomponentpanel
        taskComponentPanel = new JPanel();
        taskComponentPanel.setLayout(new BoxLayout(taskComponentPanel, BoxLayout.Y_AXIS));
        taskPanel.add(taskComponentPanel);

        // dodawanie przewijania do panelu zadań
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setBounds(8, 70, CommonConstans.TASKPANEL_SIZE.width, CommonConstans.TASKPANEL_SIZE.height);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setMaximumSize(CommonConstans.TASKPANEL_SIZE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // zmiana prędkości paska przewijania
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20);

        // Tworzenie przycisku dodawania zadań
        JButton addTaskButton = new JButton("Add TODO");
        addTaskButton.setFont(createFont("LEMONMILK-Light.otf", 18f));
        addTaskButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addTaskButton.setBounds(-5, CommonConstans.GUI_SIZE.height-88,
                CommonConstans.ADDTASK_BUTTON_SIZE.width, CommonConstans.ADDTASK_BUTTON_SIZE.height);
        addTaskButton.addActionListener(this);

        // dodaj do ramki
        this.getContentPane().add(bannerLabel);
        this.getContentPane().add(scrollPane);
        this.getContentPane().add(addTaskButton);
    }

    private Font createFont(String resource, float size){
        // pobierz czcionkę z ścieżki pliku
        String filePath = getClass().getClassLoader().getResource(resource).getPath();

        // sprawdź, czy ścieżka zawiera folder ze spacjami
        if (filePath.contains("%20")){
            filePath = getClass().getClassLoader().getResource(resource).getPath().replaceAll("%20", " ");
        }

        // stwórz czcionkę
        try {
            File customFontFile = new File(filePath);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, customFontFile).deriveFont(size);
            return customFont;
        }catch (Exception e){
            System.out.println("Błąd: " + e);
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("Add TODO")){
            // stwórz komponent zadania
            TaskComponent taskComponent = new TaskComponent(taskComponentPanel);
            taskComponentPanel.add(taskComponent);

            // spraw, aby poprzednie zadanie wydawało się nieaktywne
            if (taskComponentPanel.getComponentCount() > 1){
                TaskComponent previousTask = (TaskComponent) taskComponentPanel.getComponent(taskComponentPanel.getComponentCount() - 2);
                previousTask.getTaskField().setBackground(null);
            }

            // skupienie na polu zadania po utworzeniu
            taskComponent.getTaskField().requestFocus();
            repaint();
            revalidate();
        }

    }
}
