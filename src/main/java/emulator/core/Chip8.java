package emulator.core;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;

import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;

import java.io.File;

import emulator.view.GraphicsPanel;
import emulator.view.MemoryPanel;
import emulator.view.StatusPanel;
import emulator.view.InstructionPanel;
import emulator.view.Window;



public class Chip8{
    

    // Objects to interact with
    CPU cpu;
    Keyboard keyboard;
    Window window;
    GraphicsPanel gPanel;
    MemoryPanel mPanel;
    StatusPanel sPanel;
    InstructionPanel iPanel;

    // Clock speed & steps before timer countdown
    public float clockSpeed;
    public int stepsBeforeRefresh;
    public float nanoPeriodCPU;       // nanoseconds per instruction

 
    public Chip8(){

    }


    public Chip8 getChip8(){
        return this;
    }


    public void setClock(float clock){
        System.out.println("Setting new clock speed");
        clockSpeed = clock;
        stepsBeforeRefresh = Math.round(clockSpeed/60);
        nanoPeriodCPU = 1000000000/clockSpeed;
    }


    public void initChip8(){
        //instantiate objects for emulation
        cpu = new CPU();

        //set clock up
        setClock(500);

        // Swing components need to be invoked later to be in the
        // event dispatching thread (otherwise, the CPU emulation cycle
        // ends up preventing menu navigation, paintComponent, etc... 
        // Things that depend on event handling)
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                window = new Window("CHIP-8 Emulator by Svaught598");
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
                        0, 0, 1, 1, 0.75, 0.325, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0)
                );

                // adding Memory Panel to GUI
                mPanel.setBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                            new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.decode("#E86F68"))), 
                        "CPU Registers", 
                        SwingConstants.CENTER, 
                        SwingConstants.CENTER, 
                        new Font("Bauhaus 93", Font.BOLD, 16), 
                        Color.decode("#E86F68")));
                window.getContentPane().add(
                    mPanel, new GridBagConstraints(
                        1, 0, 1, 1, 0.25, 0.325, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0)
                );

                // adding Status Panel to GUI
                sPanel.setBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                            new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.decode("#E86F68"))), 
                        "Emulator Status", 
                        SwingConstants.CENTER, 
                        SwingConstants.CENTER, 
                        new Font("Bauhaus 93", Font.BOLD, 16), 
                        Color.decode("#E86F68")));
                window.getContentPane().add(
                    sPanel, new GridBagConstraints(
                        0, 1, 1, 1, 0.75, 0.675, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0)
                );

                // adding Instruction Panel to GUI
                iPanel.setBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                            new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.decode("#E86F68"))), 
                        "Last Instruction", 
                        SwingConstants.CENTER, 
                        SwingConstants.CENTER, 
                        new Font("Bauhaus 93", Font.BOLD, 16), 
                        Color.decode("#E86F68")));
                window.getContentPane().add(
                    iPanel, new GridBagConstraints(
                        1, 1, 1, 1, 0.25, 0.675, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0)
                );

                //window.setPreferredSize(new Dimension(920, 700));
                window.setSize(new Dimension(1000, 700));
                //window.pack();

                window.chip8 = getChip8();
                cpu.keyboard = keyboard;
                cpu.gPanel = gPanel;
                cpu.chip8 = getChip8();
                sPanel.cpu = cpu;

                window.initWindow();
            }
        });
    }


    public void mainLoop(){

        SwingWorker swingWorker = new SwingWorker(){
            @Override
            protected String doInBackground() throws Exception{

                // Initialize working variables for loop
                long initialTime;
                long finalTime;
                long timeElapsed;
                int steps = 0;

                while (cpu.running){

                    // begin CPU cycles when rom is Loaded
                    if (cpu.romLoaded == true){
                        initialTime = System.nanoTime();
                        cpu.step();
                        iPanel.updateInstruction(cpu);

                        // Do these actions 60 times a second, 
                        // stepsBeforeRefresh is calculated from ratio of 
                        // CPU clock speed and 60 Hz (ratio of 'cycles')
                        if (steps % stepsBeforeRefresh == 0){
                            steps = 0;
                            cpu.decrementDelayTimer();
                            cpu.decrementSoundTimer();

                            // update panels!
                            mPanel.updateMemory(cpu);
                        }

                        // Wait until enough time has passed to begin next cycle
                        finalTime = System.nanoTime();
                        timeElapsed = finalTime - initialTime;
                        while ((nanoPeriodCPU - timeElapsed) > 0){
                            timeElapsed = System.nanoTime() - initialTime;
                            //System.out.println(timeElapsed);
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


    public void loadRom(){
        //Creating FileChooser
        final JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showDialog(window, "Load Rom");

        //If Rom was Chosen, Load to CPU memory, and start emulation
        if (returnValue == JFileChooser.APPROVE_OPTION){
            File rom = fileChooser.getSelectedFile();
            cpu.loadRom(rom);
            mainLoop();
        }

    }
}