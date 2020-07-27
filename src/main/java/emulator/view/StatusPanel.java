package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StatusPanel extends JPanel implements ActionListener{


    CPU cpu;


    // subPanels
    JPanel leftPanel;
    JPanel rightPanel;


    public StatusPanel(LayoutManager layout){
        super(layout);
        setLayout(new GridLayout(0, 2));
        setBackground(Color.decode("#E4D8B4"));
        createPanels();
        initializeLabels();
        initializeButtons();

        add(leftPanel);
        add(rightPanel);
    }


    protected void createPanels(){
        // left Panel
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.decode("#E4D8B4"));
        BoxLayout boxLeft = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(boxLeft);

        // right Panel
        rightPanel = new JPanel();
        BoxLayout boxRight = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setBackground(Color.decode("#E4D8B4"));
        rightPanel.setLayout(boxRight);
    }


    protected void initializeLabels(){
        // Static Labels (they don't change at runtime)
        JLabel title = new JLabel("Chip-8 Emulator");
        title.setFont(new Font("Bauhaus 93", Font.BOLD, 20));
        leftPanel.add(title);

        JLabel author = new JLabel("Steven Vaught");
        author.setFont(new Font("Bauhaus 93", Font.BOLD, 20));
        leftPanel.add(author);

        JLabel rights = new JLabel("No Rights Reserved");
        rights.setFont(new Font("Bauhaus 93", Font.BOLD, 20));
        leftPanel.add(rights);
    }

    protected void initializeButtons(){
        // Button to Increment CPU Speed
        JButton incButton = new JButton("Increment CPU Speed");
        incButton.addActionListener(this);
        incButton.setActionCommand("Increment");
        rightPanel.add(incButton);

        // Button to Decrement CPU Speed
        JButton decButton = new JButton("Decrement CPU Speed");
        decButton.addActionListener(this);
        decButton.setActionCommand("Decrement");
        rightPanel.add(decButton);

        // Button to Pause Emulator
        JButton pauseButton = new JButton("Stop Emulation");
        pauseButton.addActionListener(this);
        pauseButton.setActionCommand("Pause");
        rightPanel.add(pauseButton);

        // Button to Resume Emulator
        JButton resumeButton = new JButton("Resume Emulation");
        resumeButton.addActionListener(this);
        resumeButton.setActionCommand("Resume");
        rightPanel.add(resumeButton);

        // Button to Reset Emulator
        JButton resetButton = new JButton("Reset Emulator");
        resetButton.addActionListener(this);
        resetButton.setActionCommand("Reset");
        rightPanel.add(resetButton);
    }


    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "Increment"){
            // increment CPU speed
            System.out.println("increment");
        }
        if (e.getActionCommand() == "Decrement"){
            // Decrement CPU speed
            System.out.println("decrement");
        }
        if (e.getActionCommand() == "Pause"){
            // Pause emulator
            cpu.stopRunning();
            System.out.println("pause");
        }
        if (e.getActionCommand() == "Resume"){
            // Resume Emulation
            //cpu.resumeRunning();
            System.out.println("resume");
        }
        if (e.getActionCommand() == "Reset"){
            // Reset Emulation
            //cpu.reset();
            System.out.println("reset");
        }
    }
}