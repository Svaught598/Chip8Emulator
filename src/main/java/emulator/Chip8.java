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
    public static float nanoPeriodCPU = 1000000000/480;     // nanoseconds per instruction

    public long initialTime;
    public long finalTime;
    public long timeElapsed;

    public int stepsBeforeRefresh = 480/60;
    public int steps;


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
                while (cpu.running){

                    // begin CPU cycles when rom is Loaded
                    if (cpu.romLoaded == true){
                        initialTime = System.nanoTime();
                        cpu.step();

                        // Do these actions 60 times a second, 
                        // stepsBeforeRefresh is calculated from ratio of 
                        // CPU clock speed and 60 Hz (ratio of 'cycles')
                        if (steps % stepsBeforeRefresh == 0){
                            steps = 0;
                            cpu.decrementDelayTimer();
                            cpu.decrementSoundTimer();
                        }

                        // Wait until enough time has passed to begin next cycle
                        finalTime = System.nanoTime();
                        timeElapsed = finalTime - initialTime;
                        while ((nanoPeriodCPU - timeElapsed) > 0){
                            timeElapsed = System.nanoTime() - initialTime;
                            try{
                                Thread.sleep(0);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return "";
            }
        };
        swingWorker.execute();
    }
}

