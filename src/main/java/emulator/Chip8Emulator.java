package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class Chip8Emulator extends JFrame implements ActionListener{

    // ****************************************
    // Other objects that get interacted with *
    // ****************************************
    CPU cpu;

    /*
    Entry point! main & createAndShowGUI are static methods to create objects
    and begin program. All other methods are instance methods for manipulating 
    data flow between objects.
    */

    public Chip8Emulator(String windowName){
        super(windowName);
    }

    public static void main(String[] args){
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI(){
        //Create and set up the window.
        Chip8Emulator emulator = new Chip8Emulator("CHIP-8 Emulator - Steven Vaught");
        Panel panel = new Panel();
        emulator.cpu = new CPU();
        emulator.add(panel);
        emulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        emulator.prepareForResize();
        emulator.prepareMenu();

        //Display the window.
        emulator.setSize(
            panel.DISPLAY_WIDTH * panel.PIXEL_WIDTH,
            panel.DISPLAY_HEIGHT * panel.PIXEL_HEIGHT + emulator.getInsets().top
        );
        emulator.setVisible(true);
    }

    // *************************
    //  Instance Methods *******
    // *************************

    private void prepareForResize(){
        //TODO: dynamically resize panel graphics when emulator is resized.
        // Commented implementation below interferes with Panel draw() method

        this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent componentEvent){
                // resize logic and stuff goes here
                System.out.println("resized!");
            }
        });
    }

    private void prepareMenu(){
        //Setup menubar for window
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadRomItem = new JMenuItem("Load Rom");
        loadRomItem.addActionListener(this);

        fileMenu.add(loadRomItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e){
        //Creating FileChooser
        final JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showDialog(this, "Load Rom");

        //If Rom was Chosen, Load to CPU memory
        if (returnValue == JFileChooser.APPROVE_OPTION){
            File rom = fileChooser.getSelectedFile();
            this.cpu.loadRom(rom);
        }
    }
}
