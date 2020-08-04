package emulator.core;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;

import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Dimension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.io.FileWriter;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import emulator.view.GraphicsPanel;
import emulator.view.MemoryPanel;
import emulator.view.StatusPanel;
import emulator.view.InstructionPanel;
import emulator.view.Window;



public class Chip8{
    

    // Objects to interact with
    CPU cpu;
    Keyboard keyboard;
    Window window;
    GraphicsPanel gPanel;
    MemoryPanel mPanel;
    StatusPanel sPanel;
    InstructionPanel iPanel;
    SwingWorker mainThread;


    public Chip8(){

    }


    public Chip8 getChip8(){
        return this;
    }


    public void initChip8(){
        //instantiate objects for emulation
        cpu = new CPU();

        // //set clock up
        // setClock(500);

        // Swing components need to be invoked later to be in the
        // event dispatching thread (otherwise, the CPU emulation cycle
        // ends up preventing menu navigation, paintComponent, etc... 
        // Things that depend on event handling)
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                window = new Window("CHIP-8 Emulator by Svaught598");
                gPanel = new GraphicsPanel(new BorderLayout());
                mPanel = new MemoryPanel(new BorderLayout());
                sPanel = new StatusPanel(new BorderLayout());
                iPanel = new InstructionPanel(new BorderLayout());
                keyboard = new Keyboard();

                window.getContentPane().setLayout(new GridBagLayout());

                // adding Graphics Panel to GUI
                gPanel.setBorder(BorderFactory.createLoweredBevelBorder());
                window.getContentPane().add(
                    gPanel, new GridBagConstraints(
                        0, 0, 1, 1, 0.75, 0.7, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(16, 16, 16, 16), 0, 0)
                );

                // adding Memory Panel to GUI
                mPanel.setBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                            new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.decode("#d66853"))), 
                        "CPU Registers", 
                        SwingConstants.CENTER, 
                        SwingConstants.CENTER, 
                        new Font("Bauhaus 93", Font.BOLD, 16), 
                        Color.decode("#d66853")));
                window.getContentPane().add(
                    mPanel, new GridBagConstraints(
                        1, 0, 1, 1, 0.25, 0.7, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0)
                );

                // adding Status Panel to GUI
                sPanel.setBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                            new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.decode("#d66853"))), 
                        "Emulator Status", 
                        SwingConstants.CENTER, 
                        SwingConstants.CENTER, 
                        new Font("Bauhaus 93", Font.BOLD, 16), 
                        Color.decode("#d66853")));
                window.getContentPane().add(
                    sPanel, new GridBagConstraints(
                        0, 1, 1, 1, 0.75, 0.3, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0)
                );

                // adding Instruction Panel to GUI
                iPanel.setBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                            new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.decode("#d66853"))), 
                        "Last Instruction", 
                        SwingConstants.CENTER, 
                        SwingConstants.CENTER, 
                        new Font("Bauhaus 93", Font.BOLD, 16), 
                        Color.decode("#d66853")));
                window.getContentPane().add(
                    iPanel, new GridBagConstraints(
                        1, 1, 1, 1, 0.25, 0.3, GridBagConstraints.WEST, 
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0)
                );

                //window.setPreferredSize(new Dimension(920, 700));
                window.setSize(new Dimension(1000, 700));
                //window.pack();

                window.chip8 = getChip8();
                cpu.keyboard = keyboard;
                cpu.gPanel = gPanel;
                cpu.chip8 = getChip8();
                sPanel.cpu = cpu;

                window.initWindow();
            }
        });
    }


    public void mainLoop(){

        mainThread = new SwingWorker(){

            @Override
            protected String doInBackground() throws Exception{

                // Initialize working variables for loop
                long initialTime;
                long finalTime;
                long timeElapsed;
                int steps = 0;
                long loopPeriod;
                int numberOfSteps;

                while (cpu.running){

                    loopPeriod = getNewLoopPeriod();
                    numberOfSteps = getNewNumberOfSteps();

                    // begin CPU cycles when rom is Loaded
                    if (cpu.romLoaded == true){
                        initialTime = System.nanoTime();
                        cpu.step();

                        // Do these actions 60 times a second, 
                        // stepsBeforeRefresh is calculated from ratio of 
                        // CPU clock speed and 60 Hz (ratio of 'cycles')
                        if (steps % numberOfSteps == 0){
                            steps = 0;
                            cpu.decrementDelayTimer();
                            cpu.decrementSoundTimer();

                            // update panels!
                            mPanel.updateMemory(cpu);
                            iPanel.updateInstruction(cpu);
                        }

                        // Wait until enough time has passed to begin next cycle
                        finalTime = System.nanoTime();
                        timeElapsed = finalTime - initialTime;
                        while ((loopPeriod - timeElapsed) > 0){
                            timeElapsed = System.nanoTime() - initialTime;
                            //System.out.println(timeElapsed);
                            try{
                                Thread.sleep(0);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return "";
            }
        

            protected long getNewLoopPeriod(){
                float clockSpeed = getChip8().cpu.clockSpeed;
                return (long) (1000000000/clockSpeed);
            }


            protected int getNewNumberOfSteps(){
                float clockSpeed = getChip8().cpu.clockSpeed;
                return Math.round(clockSpeed/60);
            }
        };

        mainThread.execute();
    }


    public void loadRom(){
        //Creating FileChooser
        final JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showDialog(window, "Load Rom");

        //If Rom was Chosen, Load to CPU memory, and start emulation
        if (returnValue == JFileChooser.APPROVE_OPTION){
            File rom = fileChooser.getSelectedFile();
            cpu.loadRom(rom);
            sPanel.updateRomLoaded(rom.getName());
            mainLoop();
        }
    }


    public void saveState(int state){
        // save memory as a object
        JSONObject memory = new JSONObject();
        for (int i = 0; i < cpu.AMOUNT_OF_MEMORY; i++){
            memory.put(
                String.format("memory%d", i), 
                String.format("%d", cpu.memory[i]));
        }

        // Saving register values
        JSONObject register = new JSONObject();
        for (int i = 0; i < 0x10; i++){
            register.put(
                String.format("register%d", i),
                String.format("%d", cpu.V[i])
            );
        }

        // saving stack values
        JSONObject stack = new JSONObject();
        for (int i = 0; i < 0x10; i++){
            stack.put(
                String.format("stack%d", i),
                String.format("%d", cpu.stack[i])
            );
        }

        // saving gPanel pixelarray
        JSONObject pixelArray = new JSONObject();
        for (int i = 0; i < gPanel.NUMBER_OF_PIXELS; i++){
            pixelArray.put(
                String.format("pixel%d", i),
                String.format("%d", gPanel.pixelArray[i])
            );
        }

        // saving other values
        JSONObject index = new JSONObject();
        index.put("I", cpu.I);

        JSONObject delayTimer = new JSONObject();
        delayTimer.put("delayTimer", cpu.delayTimer);

        JSONObject soundTimer = new JSONObject();
        soundTimer.put("soundTimer", cpu.soundTimer);

        JSONObject programCounter = new JSONObject();
        programCounter.put("programCounter", cpu.programCounter);

        JSONObject stackPointer = new JSONObject();
        stackPointer.put("stackPointer", cpu.stackPointer);

        JSONObject opcode = new JSONObject();
        opcode.put("opcode", cpu.opcode);

        // parent array for allobjects to go in
        JSONArray cpuState = new JSONArray();

        // Add the objects to the state array
        cpuState.add(memory);
        cpuState.add(register);
        cpuState.add(stack);
        cpuState.add(pixelArray);
        cpuState.add(index);
        cpuState.add(delayTimer);
        cpuState.add(soundTimer);
        cpuState.add(programCounter);
        cpuState.add(stackPointer);
        cpuState.add(opcode);

        // Writing file to JSON file
        try (FileWriter file = new FileWriter(String.format("./saves/state_%d.json", state))){
            file.write(cpuState.toJSONString());
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    public void loadState(int state){
        
        // reset cpu state and create parser for state data
        cpu.reset();
        JSONParser parser = new JSONParser();

        // read the cpu state file in question, parse each field
        try (FileReader file = new FileReader(String.format("./saves/state_%d.json", state))){
            Object obj = parser.parse(file);
            JSONArray cpuState = (JSONArray) obj;
            for (Object field : cpuState){
                parseField((JSONObject) field);
            }

        // catch some exceptions
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e){
            e.printStackTrace();
        }

        // Start loop
        cpu.romLoaded = true;
        cpu.resumeRunning();
        System.out.println(cpu.running);
    }


    public void parseField(JSONObject field){
        // load memory values
        if (field.containsKey("memory1")){
            for (int i = 0; i < cpu.AMOUNT_OF_MEMORY; i++){
                cpu.memory[i] = Short.valueOf(
                    String.valueOf(
                        field.get(
                            String.format("memory%d", i)
                        )
                    )
                );
            }
        }

        // loading register values
        else if (field.containsKey("register1")){
            for (int i = 0; i < 0x10; i++){
                cpu.V[i] = Short.valueOf(
                    String.valueOf(
                        field.get(
                            String.format("register%d", i)
                        )
                    )
                );
            }
        }

        // loading stack values
        else if (field.containsKey("stack1")){
            for (int i = 0; i < 0x10; i++){
                cpu.stack[i] = Short.valueOf(
                    String.valueOf(
                        field.get(
                            String.format("stack%d", i)
                        )
                    )
                );
            }
        }

        // loading pixelarray
        else if (field.containsKey("pixel1")){
            for (int i = 0; i < gPanel.NUMBER_OF_PIXELS; i++){
                gPanel.pixelArray[i] = Byte.valueOf(
                    String.valueOf(
                        field.get(
                            String.format("pixel%d", i)
                        )
                    )
                );
            }
        }

        // loading the reset of the values
        else if (field.containsKey("I")){
            cpu.I = Short.valueOf(
                String.valueOf(field.get("I"))
            );
        }
        else if (field.containsKey("delayTimer")){
            cpu.delayTimer = Short.valueOf(
                String.valueOf(field.get("delayTimer"))
            );
        }
        else if (field.containsKey("soundTimer")){
            cpu.soundTimer = Short.valueOf(
                String.valueOf(field.get("soundTimer"))
            );
        }
        else if (field.containsKey("stackPointer")){
            cpu.stackPointer = Short.valueOf(
                String.valueOf(field.get("stackPointer"))
            );
        }
        else if (field.containsKey("programCounter")){
            cpu.programCounter = Short.valueOf(
                String.valueOf(field.get("programCounter"))
            );
        }
        else if (field.containsKey("opcode")){
            cpu.opcode =  Integer.valueOf(
                String.valueOf(field.get("opcode"))
            ); 
        }
    }
}