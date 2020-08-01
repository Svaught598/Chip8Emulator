package emulator.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Graphics;

import emulator.core.CPU;



public class StatusPanel extends JPanel implements ActionListener{


    public CPU cpu;


    // subPanels
    JPanel leftPanel;
    JPanel rightPanel;

    // Dynamic componenets
    JLabel romName;


    public StatusPanel(LayoutManager layout){
        super(layout);
        setLayout(new GridLayout(0, 2));
        setBackground(Color.decode("#11151C"));
        createPanels();
        initializeLabels();
        initializeButtons();

        add(leftPanel);
        add(rightPanel);
    }


    protected void createPanels(){
        // left Panel
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.decode("#11151c"));
        //BoxLayout boxLeft = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(new BorderLayout());//boxLeft);

        // right Panel
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5, 1, 10, 10));
        // BoxLayout boxRight = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setBackground(Color.decode("#11151c"));
        // rightPanel.setLayout(boxRight);
    }


    protected void initializeLabels(){

        JPanel subpanel = new JPanel();
        BoxLayout box = new BoxLayout(subpanel, BoxLayout.Y_AXIS);
        subpanel.setBackground(Color.decode("#11151C"));
        subpanel.setLayout(box);

        // Static Labels (they don't change at runtime)
        JLabel title = new JLabel("Chip-8 Emulator");
        title.setFont(new Font("Bauhaus 93", Font.BOLD, 30));
        title.setForeground(Color.decode("#7d4e57"));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subpanel.add(title);

        JLabel author = new JLabel("Steven Vaught");
        author.setFont(new Font("Bauhaus 93", Font.BOLD, 15));
        author.setForeground(Color.decode("#7d4e57"));
        author.setAlignmentX(Component.CENTER_ALIGNMENT);
        subpanel.add(author);

        JLabel rights = new JLabel("No Rights Reserved");
        rights.setFont(new Font("Bauhaus 93", Font.BOLD, 15));
        rights.setForeground(Color.decode("#7d4e57"));
        rights.setAlignmentX(Component.CENTER_ALIGNMENT);
        subpanel.add(rights);

        leftPanel.add(subpanel, BorderLayout.NORTH);

        // Dynamic Labels (do change at runtime)
        JPanel otherPanel = new JPanel();
        BoxLayout otherBox = new BoxLayout(otherPanel, BoxLayout.Y_AXIS);
        otherPanel.setBackground(Color.decode("#11151C"));
        otherPanel.setLayout(otherBox);

        romName = new JLabel("No ROM Loaded");
        romName.
        setFont(new Font("Bauhaus 93", Font.BOLD, 25));
        romName.setForeground(Color.decode("#7d4e57"));
        romName.setAlignmentX(Component.CENTER_ALIGNMENT);
        romName.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        otherPanel.add(romName);
        leftPanel.add(otherPanel, BorderLayout.SOUTH);
    }


    protected void initializeButtons(){
        // Button to Increment CPU Speed
        JGradientButton incButton = new JGradientButton("Increment CPU Speed");
        incButton.addActionListener(this);
        incButton.setActionCommand("Increment");
        incButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        incButton.setFont(new Font("Bauhaus 93", Font.BOLD, 15));
        incButton.setForeground(Color.decode("#11151c"));
        rightPanel.add(incButton);

        // Button to Decrement CPU Speed
        JGradientButton decButton = new JGradientButton("Decrement CPU Speed");
        decButton.addActionListener(this);
        decButton.setActionCommand("Decrement");
        decButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        decButton.setFont(new Font("Bauhaus 93", Font.BOLD, 15));
        decButton.setForeground(Color.decode("#11151c"));
        rightPanel.add(decButton);

        // Button to Pause Emulator
        JGradientButton pauseButton = new JGradientButton("Stop Emulation");
        pauseButton.addActionListener(this);
        pauseButton.setActionCommand("Pause");
        pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseButton.setFont(new Font("Bauhaus 93", Font.BOLD, 15));
        pauseButton.setForeground(Color.decode("#11151c"));
        rightPanel.add(pauseButton);

        // Button to Resume Emulator
        JGradientButton resumeButton = new JGradientButton("Resume Emulation");
        resumeButton.addActionListener(this);
        resumeButton.setActionCommand("Resume");
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resumeButton.setFont(new Font("Bauhaus 93", Font.BOLD, 15));
        resumeButton.setForeground(Color.decode("#11151c"));
        rightPanel.add(resumeButton);

        // Button to Reset Emulator
        JGradientButton resetButton = new JGradientButton("Reset Emulator");
        resetButton.addActionListener(this);
        resetButton.setActionCommand("Reset");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setFont(new Font("Bauhaus 93", Font.BOLD, 15));
        resetButton.setForeground(Color.decode("#11151c"));
        rightPanel.add(resetButton);
    }


    public void updateRomLoaded(String name){
        romName.setText(name);
    }


    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "Increment"){
            // increment CPU speed
            cpu.incrementClockSpeed();
        }
        if (e.getActionCommand() == "Decrement"){
            // Decrement CPU speed
            cpu.decrementClockSpeed();
        }
        if (e.getActionCommand() == "Pause"){
            // Pause emulator
            cpu.stopRunning();
        }
        if (e.getActionCommand() == "Resume"){
            // Resume Emulation
            cpu.resumeRunning();
        }
        if (e.getActionCommand() == "Reset"){
            // Reset Emulation
            cpu.reset();
        }
    }



    private class JGradientButton extends JButton {


        private JGradientButton(String name) {
            super(name);
            setContentAreaFilled(false);
            setFocusPainted(false); // used for demonstration
        }
    
    
        @Override
        protected void paintComponent(Graphics g) {
            final Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(
                    new Point(0, 0), 
                    Color.decode("#7d4e57"), 
                    new Point(0, getHeight()), 
                    Color.decode("#7d4e57")));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
    
            super.paintComponent(g);
        }
    }
}



