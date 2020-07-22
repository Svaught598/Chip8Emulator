package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Window extends JFrame implements ActionListener{

    CPU cpu;
    Chip8 chip8;

    public static JMenuItem loadRomItem = new JMenuItem("Load Rom");

    public Window(String windowName){
        super(windowName);
    }

    public void initWindow(){
        prepareMenu();
        prepareForResize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setSize(
        //     cpu.panel.DISPLAY_WIDTH * cpu.panel.PIXEL_WIDTH,
        //     cpu.panel.DISPLAY_HEIGHT * cpu.panel.PIXEL_HEIGHT + this.getInsets().top
        // );
        setVisible(true);
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
            cpu.loadRom(rom);
        }
        chip8.mainLoop();
    }

}