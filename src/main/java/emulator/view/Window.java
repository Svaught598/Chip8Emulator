package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Window extends JFrame implements ActionListener{


    Chip8 chip8;


    public Window(String windowName){
        super(windowName);
    }


    public void initWindow(){
        prepareMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#C2B28F"));
        setVisible(true);
    }


    public void prepareMenu(){
        //Setup menubar for window
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        //Adding Menu Items to menuBar 
        JMenuItem loadRomItem = new JMenuItem("Load Rom");
        loadRomItem.addActionListener(this);
        loadRomItem.setActionCommand("Load Rom");

        //Putting it all together
        fileMenu.add(loadRomItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }


    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "Load Rom"){
            chip8.loadRom();
        }
    }
}