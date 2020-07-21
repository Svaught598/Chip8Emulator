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
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }


    public void draw(Graphics g){
        System.out.println("\t[INFO] Draw method called");
        for(int i = 0; i < NUMBER_OF_PIXELS; i++){
            int posX = i % DISPLAY_WIDTH;
            int posY = (int) Math.floor(i/DISPLAY_WIDTH);

            if (pixelArray[i] != 1) {
                g.setColor(Color.BLACK);
            }
            else {
                g.setColor(Color.WHITE);
            }
            g.fillRect(posX*PIXEL_WIDTH, posY*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }

    }

    public void actionPerformed(ActionEvent event){
        // if(test > 2048){
        //     test = 1;
        // }
        // else{
        //     test += 1;
        // }
        // repaint();
    }

    public void drawSprite(int posX, int posY, byte[] spriteArray){
        // if any pixels are flipped from set to unset, set pixelFlipped = true
        // else set pixelFlipped = false;
        int height = spriteArray.length;
        int index;
        byte spriteLine;
        pixelFlipped = false;

        // loop through ylines & get line for sprite
        for (int yline = 0; yline < height; yline++){
            spriteLine = spriteArray[yline];

            // loop through xlines & get pixelarray index
            for (int xline = 0; xline < 8; xline++){
                index = (posX + xline) + (posY + yline)*DISPLAY_WIDTH;

                // check if bit in spriteLine is unset
                if ((spriteLine & (0x80 >> xline)) != 0){
                    
                    // check if bit in pixelArray is set
                    if (pixelArray[index] == 1){
                        pixelFlipped = true;
                    }
                    pixelArray[index] ^= 1;
                }
            }
        }
        System.out.println("\t[INFO] Sprite Painted");
        repaint();
    }

    public void clearScreen(){
        // clears the screen!
        for (int i = 0; i < NUMBER_OF_PIXELS; i++){
            pixelArray[i] = 0;
        }
        System.out.println("\t[INFO] Screen Cleared");
        repaint();
    }
}