package emulator;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

import emulator.core.Chip8;



public class Main{


    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                Chip8 emulator = new Chip8();
                emulator.initChip8();
                setLookAndFeel();
            }
        });
    }


    private static void setLookAndFeel(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
