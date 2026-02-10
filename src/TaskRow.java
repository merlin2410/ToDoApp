package src;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;

public class TaskRow extends JPanel {
    JCheckBox checkBox;

    public TaskRow(String taskText, JPanel parentPanel, Runnable onUpdate){
        setLayout(new BorderLayout());
        setBackground(Color.decode("#333333"));

        setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createLineBorder(Color.decode("#555555"),1)
        ));

        checkBox = new JCheckBox(taskText);
        checkBox.setForeground(Color.white);
        checkBox.setBackground(Color.decode("#333333"));
        checkBox.setFont(new Font("Arial",Font.PLAIN,16));
        checkBox.setFocusPainted(false);
        add(checkBox,BorderLayout.CENTER);

        JButton deleteBtn = new JButton("X");
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setFont(new Font("Arial", Font.BOLD,12));

        deleteBtn.addActionListener(e->{
            parentPanel.remove(this);
            parentPanel.revalidate();
            parentPanel.repaint();
            onUpdate.run();
        });

        checkBox.addItemListener(e->{
            updateStrikeThrough();
            onUpdate.run();
        });

        add(deleteBtn,BorderLayout.EAST);


    }

    public String getTaskText(){
        JCheckBox cb = (JCheckBox) getComponent(0);
        return cb.getText();
    }

    private boolean isSelected(){
        return checkBox.isSelected();
    }

    public void setSelected(boolean completed){
        checkBox.setSelected(completed);
        updateStrikeThrough();
    }

    private Font getStrikeThroughFont(Font original){
        Map attributes = original.getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH,TextAttribute.STRIKETHROUGH_ON);
        return new Font(attributes);
    }

    private void updateStrikeThrough(){
        if(checkBox.isSelected()){
            checkBox.setFont(getStrikeThroughFont(checkBox.getFont()));
            checkBox.setForeground(Color.GRAY);
        }else {
            checkBox.setFont(new Font("Arial",Font.PLAIN,16));
            checkBox.setForeground(Color.WHITE);
        }
    }
}