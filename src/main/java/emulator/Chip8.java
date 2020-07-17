package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Chip8 extends JFrame implements ActionListener{
    
    CPU cpu;

    public boolean romLoaded = false;
    public static JMenuItem loadRomItem = new JMenuItem("Load Rom");

    public Chip8(String windowName){
        //Constructor
        super(windowName);
    }

    public void initChip8(){
        //instantiate objects & prepare GUI
        this.cpu = new CPU();
        this.cpu.panel = new Panel();
        this.cpu.keyboard = new Keyboard();
        this.add(this.cpu.panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.prepareForResize();
        this.prepareMenu();

        //Display the window.
        this.setSize(
            this.cpu.panel.DISPLAY_WIDTH * this.cpu.panel.PIXEL_WIDTH,
            this.cpu.panel.DISPLAY_HEIGHT * this.cpu.panel.PIXEL_HEIGHT + this.getInsets().top
        );
        this.setVisible(true);
    }

    public void prepareForResize(){
        //prepare for emulator resize
        this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent componentEvent){
                //TODO: resize logic and stuff goes here
                System.out.println("resized!");
            }
        });
    }

    public void prepareMenu(){
        //Setup menubar for window
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        loadRomItem.addActionListener(this);

        fileMenu.add(loadRomItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == loadRomItem){
            loadRom();
        }
    }

    public void loadRom(){
        //Creating FileChooser
        final JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showDialog(this, "Load Rom");

        //If Rom was Chosen, Load to CPU memory
        if (returnValue == JFileChooser.APPROVE_OPTION){
            File rom = fileChooser.getSelectedFile();
            this.romLoaded = this.cpu.loadRom(rom);
        }
    }

    public void mainLoop(){
        //TODO: mainloop
    }
}

