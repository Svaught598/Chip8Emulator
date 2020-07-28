package emulator.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.GridLayout;

import emulator.core.CPU;


public class MemoryPanel extends JPanel{


    class MLabel extends JLabel{
        Dimension dim = new Dimension(80, 20);

        public MLabel(){
            super();
            setMinimumSize(dim);
            setPreferredSize(dim);
            setMaximumSize(dim);
        }

        public MLabel(String text){
            super(text);
            setMinimumSize(dim);
            setPreferredSize(dim);
            setMaximumSize(dim);
        }
    }

    // two subPanels
    JPanel registerPanel;
    JPanel stackPanel;
    JPanel otherPanel;


    // Dimensions for each subpanel
    Dimension subPanelDims = new Dimension(50, 230);


    // all of the labels for the CPU memory
    MLabel[] registerLabels = new MLabel[0x10];
    MLabel[] stackLabels = new MLabel[0x10];
    MLabel soundTimer = new MLabel();
    MLabel delayTimer = new MLabel();
    MLabel programCounter = new MLabel();
    MLabel stackPointer = new MLabel();
    MLabel valueI = new MLabel();

    
    public MemoryPanel(LayoutManager layout){
        super(layout);
        setLayout(new GridLayout(0, 3));
        setBackground(Color.decode("#E4D8B4"));
        initializeLabels();
        createRegisterPanel();
        createStackPanel();
        createOtherPanel();

        add(registerPanel);
        add(stackPanel);
        add(otherPanel);
    }


    protected void initializeLabels(){
        // Setting initial values
        for (int i=0; i<0x10; i++){
            registerLabels[i] = new MLabel(String.format("V%X: N/A", i));
            stackLabels[i] = new MLabel(String.format("S%X: N/A", i));
        }
        soundTimer.setText("Sound: N/A");
        delayTimer.setText("Delay: N/A");
        programCounter.setText("PC: N/A");
        stackPointer.setText("SP: N/A");
        valueI.setText("I: N/A");

        // Aligning the labels in the center of column
        for (int i=0; i<0x10; i++){
            registerLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            stackLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
        }
        soundTimer.setHorizontalAlignment(SwingConstants.CENTER);
        delayTimer.setHorizontalAlignment(SwingConstants.CENTER);
        programCounter.setHorizontalAlignment(SwingConstants.CENTER);
        stackPointer.setHorizontalAlignment(SwingConstants.CENTER);
        valueI.setHorizontalAlignment(SwingConstants.CENTER);

    }


    protected void createRegisterPanel(){
        // Initialize new panel
        registerPanel = new JPanel();
        registerPanel.setBackground(Color.decode("#E4D8B4"));
        BoxLayout box = new BoxLayout(registerPanel, BoxLayout.Y_AXIS);
        registerPanel.setLayout(box);
        registerPanel.setPreferredSize(subPanelDims);
        registerPanel.setMinimumSize(subPanelDims);
        registerPanel.setMaximumSize(subPanelDims);

        // adding labels to panel
        for (int i=0; i < 0x10; i++){
            registerPanel.add(registerLabels[i]);
        }
    }


    protected void createStackPanel(){
        // Initialize new panel
        stackPanel = new JPanel();
        stackPanel.setBackground(Color.decode("#E4D8B4"));
        BoxLayout box = new BoxLayout(stackPanel, BoxLayout.Y_AXIS);
        stackPanel.setLayout(box);
        stackPanel.setPreferredSize(subPanelDims);
        stackPanel.setMinimumSize(subPanelDims);
        stackPanel.setMaximumSize(subPanelDims);

        //adding labels to panel
        for (int i=0; i < 0x10; i++){
            stackPanel.add(stackLabels[i]);
        }
    }


    protected void createOtherPanel(){
        // Initialize new panel
        otherPanel = new JPanel();
        otherPanel.setBackground(Color.decode("#E4D8B4"));
        BoxLayout box = new BoxLayout(otherPanel, BoxLayout.Y_AXIS);
        otherPanel.setLayout(box);
        otherPanel.setPreferredSize(subPanelDims);
        otherPanel.setMinimumSize(subPanelDims);
        otherPanel.setMaximumSize(subPanelDims);

        // adding labels to panel
        otherPanel.add(soundTimer);
        otherPanel.add(delayTimer);
        otherPanel.add(stackPointer);
        otherPanel.add(programCounter);
        otherPanel.add(valueI);
    }


    public void updateMemory(CPU cpu){
        for (int i=0; i < 0x10; i++){
            registerLabels[i].setText(String.format("V%X: %X", i, cpu.V[i]));
            stackLabels[i].setText(String.format("S%X: %X", i, cpu.stack[i]));
        }
        soundTimer.setText(String.format("Sound: %X", cpu.soundTimer));
        delayTimer.setText(String.format("Delay: %X", cpu.delayTimer));
        programCounter.setText(String.format("PC: %X", cpu.programCounter));
        stackPointer.setText(String.format("SP: %X", cpu.stackPointer));
        valueI.setText(String.format("I: %X", cpu.I));
    }
}