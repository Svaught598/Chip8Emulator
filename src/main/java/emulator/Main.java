package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class Main{

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                startGUI();
            }
        });
    }

    private static void startGUI(){
        Chip8 emulator = new Chip8();
        emulator.initChip8();
    }

}
