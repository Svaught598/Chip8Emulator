package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/*
 * Hello world!
 *
 */
public class Chip8Emulator extends JFrame{
    /*
    Entry point! main & createAndShowGUI are static methods to create objects
    and begin program. All other methods are instance methods for manipulating 
    data flow between objects.
    */

    public Chip8Emulator(String windowName){
        super(windowName);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        Chip8Emulator emulator = new Chip8Emulator("CHIP-8 Emulator - Steven Vaught");
        Panel panel = new Panel();
        emulator.add(panel);
        emulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        emulator.createEventListeners();

        //Display the window.
        emulator.setSize(
            panel.DISPLAY_WIDTH * panel.PIXEL_WIDTH,
            panel.DISPLAY_HEIGHT * panel.PIXEL_HEIGHT
        );
        emulator.setVisible(true);
    }

    // *************************
    //  Instance Methods *******
    // *************************

    private void createEventListeners() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                //TODO: dynamically resize panel graphics when emulator is resized.
                System.out.println("resized!");
            }
        });
    }

}
