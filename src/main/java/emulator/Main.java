package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class Main{

    public static void main(String[] args){
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startGUI();
            }
        });
    }

    private static void startGUI(){
        Chip8 emulator = new Chip8("CHIP-8 Interpreter");
        emulator.initChip8();
        emulator.mainLoop();
    }

}
