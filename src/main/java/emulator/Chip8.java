package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import emulator.view.*;

public class Chip8{
    

    CPU cpu;
    Keyboard keyboard;
    Window window;
    GraphicsPanel gPanel;
    MemoryPanel mPanel;
    // StatusPanel sPanel;
    // InstructionPanel iPanel;


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
                keyboard = new Keyboard();

                window.getContentPane().setLayout(new GridBagLayout());

                // adding Graphics Panel to GUI
                gPanel.setBorder(BorderFactory.createLoweredBevelBorder());
                window.getContentPane().add(
                    gPanel, new GridBagConstraints(
                        0, 0, 1, 1, 0.75, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0)
                );

                // adding Memory Panel to GUI
                mPanel.setBorder(BorderFactory.createLoweredBevelBorder());
                JLabel mLabel =  new JLabel("Memory Panel");
                mLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mPanel.add(mLabel, BorderLayout.CENTER);
                window.getContentPane().add(
                    mPanel, new GridBagConstraints(
                        1, 0, 1, 1, 0.25, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0)
                );

                window.setPreferredSize(new Dimension(1024, 768));
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

