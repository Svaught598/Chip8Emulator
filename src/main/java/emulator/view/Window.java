package emulator.view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import java.io.File;

import emulator.core.Chip8;



public class Window extends JFrame implements ActionListener{

    // java swing setActionCommand takes string as argument
    // so I'm declaring string constants instead of an enum
    //....
    // I should have though more before using Swing >_<
    String LOAD_ROM = "load rom";

    String SAVE_STATE_1 = "save 1";
    String SAVE_STATE_2 = "save 2";
    String SAVE_STATE_3 = "save 3";
    String SAVE_STATE_4 = "save 4";
    String SAVE_STATE_5 = "save 5";

    String LOAD_STATE_1 = "load 1";
    String LOAD_STATE_2 = "load 2";
    String LOAD_STATE_3 = "load 3";
    String LOAD_STATE_4 = "load 4";
    String LOAD_STATE_5 = "load 5";


    public Chip8 chip8;


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
        // Setup menubar for window
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Loading roms =+++++++++++++++++++++++++++++++++++++++++
        JMenuItem loadRomItem = new JMenuItem("Load Rom");
        loadRomItem.addActionListener(this);
        loadRomItem.setActionCommand(LOAD_ROM);

        // loading states ++++++++++++++++++++++++++++++++++++++++
        JMenu loadState = new JMenu("Load State");

        JMenuItem load1 = new JMenuItem("Load State 1");
        load1.addActionListener(this);
        load1.setActionCommand(LOAD_STATE_1);

        JMenuItem load2 = new JMenuItem("Load State 2");
        load2.addActionListener(this);
        load2.setActionCommand(LOAD_STATE_2);

        JMenuItem load3 = new JMenuItem("Load State 3");
        load3.addActionListener(this);
        load3.setActionCommand(LOAD_STATE_3);

        JMenuItem load4 = new JMenuItem("Load State 4");
        load4.addActionListener(this);
        load4.setActionCommand(LOAD_STATE_4);

        JMenuItem load5 = new JMenuItem("Load State 5");
        load5.addActionListener(this);
        load5.setActionCommand(LOAD_STATE_5);

        loadState.add(load1);
        loadState.add(load2);
        loadState.add(load3);
        loadState.add(load4);
        loadState.add(load5);

        // saving states +++++++++++++++++++++++++++++++++++++++
        JMenu saveState = new JMenu("Save State");

        JMenuItem save1 = new JMenuItem("Save State 1");
        save1.addActionListener(this);
        save1.setActionCommand(SAVE_STATE_1);

        JMenuItem save2 = new JMenuItem("Save State 2");
        save2.addActionListener(this);
        save2.setActionCommand(SAVE_STATE_2);

        JMenuItem save3 = new JMenuItem("Save State 3");
        save3.addActionListener(this);
        save3.setActionCommand(SAVE_STATE_3);

        JMenuItem save4 = new JMenuItem("Save State 4");
        save4.addActionListener(this);
        save4.setActionCommand(SAVE_STATE_4);

        JMenuItem save5 = new JMenuItem("Save State 5");
        save5.addActionListener(this);
        save5.setActionCommand(SAVE_STATE_5);

        saveState.add(save1);
        saveState.add(save2);
        saveState.add(save3);
        saveState.add(save4);
        saveState.add(save5);

        // Putting it all together +++++++++++++++++++++++++
        fileMenu.add(loadRomItem);
        fileMenu.add(saveState);
        fileMenu.add(loadState);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }


    public void actionPerformed(ActionEvent e){
        // Load Rom From File
        if (e.getActionCommand() == LOAD_ROM){
            chip8.loadRom();
        }

        // Save States
        else if (e.getActionCommand() == SAVE_STATE_1){

        }
        else if (e.getActionCommand() == SAVE_STATE_2){

        }
        else if (e.getActionCommand() == SAVE_STATE_3){

        }
        else if (e.getActionCommand() == SAVE_STATE_4){

        }
        else if (e.getActionCommand() == SAVE_STATE_5){

        }

        // Load States
        else if (e.getActionCommand() == LOAD_STATE_1){

        }
        else if (e.getActionCommand() == LOAD_STATE_2){

        }
        else if (e.getActionCommand() == LOAD_STATE_3){

        }
        else if (e.getActionCommand() == LOAD_STATE_4){

        }
        else if (e.getActionCommand() == LOAD_STATE_5){

        }
    }
}