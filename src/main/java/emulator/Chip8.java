package emulator;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Chip8{
    

    CPU cpu;
    Keyboard keyboard;
    Window window;
    GraphicsPanel gPanel;
    MemoryPanel mPanel;
    StatusPanel sPanel;
    InstructionPanel iPanel;


    public boolean romLoaded = false;
    public static JMenuItem loadRomItem = new JMenuItem("Load Rom");


    public Chip8(){
    }


    public Chip8 getChip8(){
        return this;
    }


    public void initChip8(){
        //instantiate objects for emulation
        cpu = new CPU();

        // Swing components need to be invoked later to be in the
        // event dispatching thread (otherwise, the CPU emulation cycle
        // ends up preventing menu navigation, paintComponent, etc... 
        // Things that depend on event handling)
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                window = new Window("CHIP8 Emulator - Svaught598");
                gPanel = new GraphicsPanel(new BorderLayout());
                mPanel = new MemoryPanel(new BorderLayout());
                sPanel = new StatusPanel(new BorderLayout());
                iPanel = new InstructionPanel(new BorderLayout());
                keyboard = new Keyboard();

                window.getContentPane().setLayout(new GridBagLayout());

                // adding Graphics Panel to GUI
                gPanel.setBorder(BorderFactory.createLoweredBevelBorder());
                window.getContentPane().add(
                    gPanel, new GridBagConstraints(
                        0, 0, 1, 1, 0.75, 0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0)
                );

                // adding Memory Panel to GUI
                mPanel.setBorder(BorderFactory.createLoweredBevelBorder());
                JLabel mLabel =  new JLabel("Memory Panel");
                mLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mPanel.add(mLabel, BorderLayout.CENTER);
                window.getContentPane().add(
                    mPanel, new GridBagConstraints(
                        1, 0, 1, 1, 0.25, 0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0)
                );

                // adding Status Panel to GUI
                sPanel.setBorder(BorderFactory.createLoweredBevelBorder());
                JLabel sLabel = new JLabel("Status Panel");
                sLabel.setHorizontalAlignment(SwingConstants.CENTER);
                sPanel.add(sLabel, BorderLayout.CENTER);
                window.getContentPane().add(
                    sPanel, new GridBagConstraints(
                        0, 1, 1, 1, 0.75, 0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0)
                );

                // adding Instruction Panel to GUI
                iPanel.setBorder(BorderFactory.createLoweredBevelBorder());
                JLabel iLabel = new JLabel("Instruction Panel");
                iLabel.setHorizontalAlignment(SwingConstants.CENTER);
                iPanel.add(iLabel, BorderLayout.CENTER);
                window.getContentPane().add(
                    iPanel, new GridBagConstraints(
                        1, 1, 1, 1, 0.25, 0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0)
                );

                window.setPreferredSize(new Dimension(920, 700));
                window.pack();

                window.cpu = cpu;
                window.chip8 = getChip8();
                cpu.keyboard = keyboard;
                cpu.gPanel = gPanel;

                window.initWindow();
            }
        });
    }


    public void mainLoop(){

        SwingWorker swingWorker = new SwingWorker(){
            @Override
            protected String doInBackground() throws Exception{
                while (true){
                    if (cpu.romLoaded == true){
                        cpu.step();
                    }
                    try{
                        Thread.sleep(10);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        swingWorker.execute();
    }
}

