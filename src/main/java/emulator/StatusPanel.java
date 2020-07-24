package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StatusPanel extends JPanel implements ActionListener{


    CPU cpu;


    public StatusPanel(LayoutManager layout){
        super(layout);
        setBackground(Color.decode("#E4D8B4"));
        initializeButtons();
    }


    protected void initializeButtons(){
        JButton stopButton = new JButton("Stop Emulation");
        stopButton.addActionListener(this);
        stopButton.setActionCommand("Stop Emulation");
        add(stopButton);
    }


    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "Stop Emulation"){
            cpu.stopRunning();
            cpu.initializeCPU();
        }
    }
}