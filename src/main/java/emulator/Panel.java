package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Panel extends JPanel implements ActionListener{
    
    public int PIXEL_WIDTH = 10;
    public int PIXEL_HEIGHT = 10;
    public int DISPLAY_WIDTH = 64;
    public int DISPLAY_HEIGHT = 32;
    public int DELAY = 20;
    public int NUMBER_OF_PIXELS = 2048;

    int test = 1;
    boolean pixelFlipped = false;
    byte pixelArray[] = new byte[2048];

    public Panel(){
        setBackground(Color.BLACK);
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g){
        for(int i = 0; i < test; i++){
            int posX = i % DISPLAY_WIDTH;
            int posY = (int) Math.floor(i/DISPLAY_WIDTH);
            // if (pixelArray[i] == 1) {
            //     g.setColor(Color.WHITE);
            // }
            // else {
            //     g.setColor(Color.BLACK);
            // }
            g.setColor(Color.WHITE);
            g.fillRect(posX*PIXEL_WIDTH, posY*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }
    }

    public void actionPerformed(ActionEvent event){
        if(test > 2048){
            test = 1;
        }
        else{
            test += 1;
        }
        repaint();
    }

    public void drawSprite(int posX, int posY, int height){
        // if any pixels are flipped from set to unset, set pixelFlipped = true
        // else set pixelFlipped = false;
    }

    public void clearScreen(){
        // This method should clear the screen!
    }
}