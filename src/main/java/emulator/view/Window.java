package emulator.view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import java.io.File;
import java.io.FileNotFoundException;

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

    String FIFTEEN_PUZZLE = "./roms/15PUZZLE";
    String BLINKY = "./roms/BLINKY";
    String BRIX = "./roms/BRIX";
    String INVADERS = "./roms/INVADERS";
    String PONG = "./roms/PONG";
    String PUZZLE = "./roms/PUZZLE";
    String TETRIS = "./roms/TETRIS";


    public Chip8 chip8;


    public Window(String windowName){
        super(windowName);
    }


    public void initWindow(){
        prepareMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#11151c"));
        setVisible(true);
    }


    public void prepareMenu(){
        // Setup menubar for window
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Loading roms =+++++++++++++++++++++++++++++++++++++++++
        JMenu loadMenu = new JMenu("Load ROM");

        JMenuItem fifteenPuzzle = new JMenuItem("15PUZZLE");
        fifteenPuzzle.addActionListener(this);
        fifteenPuzzle.setActionCommand(FIFTEEN_PUZZLE);

        JMenuItem blinky = new JMenuItem("BLINKY");
        blinky.addActionListener(this);
        blinky.setActionCommand(BLINKY);

        JMenuItem brix = new JMenuItem("BRIX");
        brix.addActionListener(this);
        brix.setActionCommand(BRIX);

        JMenuItem invaders = new JMenuItem("INVADERS");
        invaders.addActionListener(this);
        invaders.setActionCommand(INVADERS);

        JMenuItem puzzle = new JMenuItem("PUZZLE");
        puzzle.addActionListener(this);
        puzzle.setActionCommand(PUZZLE);

        JMenuItem pong = new JMenuItem("PONG");
        pong.addActionListener(this);
        pong.setActionCommand(PONG);

        JMenuItem tetris = new JMenuItem("TETRIS");
        tetris.addActionListener(this);
        tetris.setActionCommand(TETRIS);

        JMenuItem loadRomItem = new JMenuItem("Load from File");
        loadRomItem.addActionListener(this);
        loadRomItem.setActionCommand(LOAD_ROM);

        loadMenu.add(loadRomItem);
        loadMenu.add(fifteenPuzzle);
        loadMenu.add(blinky);
        loadMenu.add(brix);
        loadMenu.add(invaders);
        loadMenu.add(puzzle);
        loadMenu.add(pong);
        loadMenu.add(tetris);

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
        fileMenu.add(loadMenu);
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

        // or from roms folder
        else if (e.getActionCommand() == FIFTEEN_PUZZLE){
            chip8.loadRomByPath(FIFTEEN_PUZZLE);
        }
        else if (e.getActionCommand() == BLINKY){
            chip8.loadRomByPath(BLINKY);
        }
        else if (e.getActionCommand() == BRIX){
            chip8.loadRomByPath(BRIX);
        }
        else if (e.getActionCommand() == INVADERS){
            chip8.loadRomByPath(INVADERS);
        }
        else if (e.getActionCommand() == PONG){
            chip8.loadRomByPath(PONG);
        }
        else if (e.getActionCommand() == PUZZLE){
            chip8.loadRomByPath(PUZZLE);
        }
        else if (e.getActionCommand() == TETRIS){
            chip8.loadRomByPath(TETRIS);
        }

        // Save States
        else if (e.getActionCommand() == SAVE_STATE_1){
            chip8.saveState(1);
        }
        else if (e.getActionCommand() == SAVE_STATE_2){
            chip8.saveState(2);
        }
        else if (e.getActionCommand() == SAVE_STATE_3){
            chip8.saveState(3);
        }
        else if (e.getActionCommand() == SAVE_STATE_4){
            chip8.saveState(4);
        }
        else if (e.getActionCommand() == SAVE_STATE_5){
            chip8.saveState(5);
        }

        // Load States
        else if (e.getActionCommand() == LOAD_STATE_1){
            chip8.loadState(1);
        }
        else if (e.getActionCommand() == LOAD_STATE_2){
            chip8.loadState(2);
        }
        else if (e.getActionCommand() == LOAD_STATE_3){
            chip8.loadState(3);
        }
        else if (e.getActionCommand() == LOAD_STATE_4){
            chip8.loadState(4);
        }
        else if (e.getActionCommand() == LOAD_STATE_5){
            chip8.loadState(5);
        }
    }
}