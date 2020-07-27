package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StatusPanel extends JPanel implements ActionListener{


    CPU cpu;


    // One subpanel
    JPanel subPanel;


    public StatusPanel(LayoutManager layout){
        super(layout);
        setBackground(Color.decode("#E4D8B4"));
        createPanel();
        initializeLabels();
        initializeButtons();

        add(subPanel);
    }


    protected void createPanel(){
        subPanel = new JPanel();
        subPanel.setBackground(Color.decode("#E4D8B4"));
        BoxLayout box = new BoxLayout(subPanel, BoxLayout.Y_AXIS);
        subPanel.setLayout(box);
    }


    protected void initializeLabels(){
        JLabel title = new JLabel("Chip-8 Emulator by Steven Vaught");
        subPanel.add(title);
        JLabel rights = new JLabel("No Rights Reserved");
        subPanel.add(rights);
    }

    protected void initializeButtons(){
        JButton stopButton = new JButton("Stop Emulation");
        stopButton.addActionListener(this);
        stopButton.setActionCommand("Stop Emulation");
        subPanel.add(stopButton);
    }


    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "Stop Emulation"){
            cpu.stopRunning();
            cpu.initializeCPU();
        }
    }
}