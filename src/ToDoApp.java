package src;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

public class ToDoApp {
    private static final String FILE_NAME = "tasks.txt";
    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
        }catch (Exception e){
            System.out.println("FlatDarkLaf not loaded");
        }

        JFrame frame = new JFrame("Focus List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,600);
        frame.setLayout(new BorderLayout());

        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(Color.decode("#1C1C1C"));

        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField taskInput = new JTextField();
        JButton addButton = new JButton("Add Task");
        addButton.setFont(new Font("Arial",Font.BOLD,12));
        addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.BLACK);

        inputPanel.add(taskInput,BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        frame.add(inputPanel,BorderLayout.SOUTH);

        JPanel clearPanel = new JPanel(new BorderLayout());

        clearPanel.setBackground(Color.decode("#1C1C1C"));
        clearPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        JLabel title = new JLabel("My Tasks");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial",Font.BOLD,16));
        clearPanel.add(title,BorderLayout.WEST);

        JButton clearButton = new JButton("Clear All");
        clearButton.setFont(new Font("Arial",Font.BOLD,12));
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setBackground(Color.pink);
        clearButton.setForeground(Color.RED);
        clearPanel.add(clearButton,BorderLayout.EAST);
        frame.add(clearPanel,BorderLayout.NORTH);

        Runnable saveProcess = ()->saveTasksToFile(taskListPanel);

        ActionListener addTaskAction = e->{
            String text = taskInput.getText();
            if(!text.isEmpty()){
                TaskRow newRow = new TaskRow(text, taskListPanel, saveProcess);
                taskListPanel.add(newRow);
                taskListPanel.revalidate();
                taskInput.setText("");

                saveTasksToFile(taskListPanel);
            }
        };

        ActionListener clearAllTaskAction = e->{
            taskListPanel.removeAll();
            taskListPanel.revalidate();
            taskListPanel.repaint();
            saveTasksToFile(taskListPanel);
        };

        addButton.addActionListener(addTaskAction);

        taskInput.addActionListener(addTaskAction);

        clearButton.addActionListener(clearAllTaskAction);

        loadTasksFromFile(taskListPanel,saveProcess);

        frame.setVisible(true);
    }

    // Save data to file
    private static void saveTasksToFile(JPanel taskListPanel){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for(Component comp: taskListPanel.getComponents()){
                if(comp instanceof TaskRow){
                    TaskRow row = (TaskRow) comp;
                    writer.write(row.getTaskText()+"|"+row.checkBox.isSelected());
                    writer.newLine();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Load from file
    private static void loadTasksFromFile(JPanel taskListPanel, Runnable saveProcess){
        File file = new File(FILE_NAME);
        if(!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line=reader.readLine())!=null){
                String parts[] = line.split("\\|");
                String text = parts[0];
                boolean isDone = false;

                if(parts.length>1){
                    isDone = Boolean.parseBoolean(parts[1]);
                }

                TaskRow row = new TaskRow(text,taskListPanel,saveProcess);
                row.setSelected(isDone);
                taskListPanel.add(row);
            }
            taskListPanel.revalidate();
            taskListPanel.repaint();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
