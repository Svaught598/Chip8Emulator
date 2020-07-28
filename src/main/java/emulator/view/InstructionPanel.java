package emulator.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Font;

import emulator.core.CPU;

public class InstructionPanel extends JPanel{


    JLabel instruction;


    public InstructionPanel(LayoutManager layout){
        super(layout);
        setBackground(Color.decode("#E4D8B4"));
        initializePanel();
    }


    protected void initializePanel(){
        // set font, color, & border
        instruction = new JLabel("0x0000");
        instruction.setFont(new Font("Bauhaus 93", Font.BOLD, 26));
        instruction.setForeground(Color.decode("#83B799"));
        instruction.setMaximumSize(instruction.getMinimumSize());
        instruction.setHorizontalAlignment(SwingConstants.CENTER);
        add(instruction, SwingConstants.CENTER);
    }


    public void updateInstruction(CPU cpu){
        instruction.setText(String.format("0x%X", cpu.opcode));
    }
}


