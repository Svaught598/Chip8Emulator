package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Chip8{
    

    CPU cpu;
    Panel panel;
    Keyboard keyboard;
    Window window;


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
                panel = new Panel();
                keyboard = new Keyboard();

                window.add(panel);
                window.cpu = cpu;
                window.chip8 = getChip8();
                cpu.keyboard = keyboard;
                cpu.panel = panel;

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
                        Thread.sleep(100);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        swingWorker.execute();
    }
}

