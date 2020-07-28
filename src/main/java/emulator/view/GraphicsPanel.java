package emulator.view;

import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Graphics;



public class GraphicsPanel extends JPanel{
    
    
    public int PIXEL_WIDTH = 10;
    public int PIXEL_HEIGHT = 10;
    public int DISPLAY_WIDTH = 64;
    public int DISPLAY_HEIGHT = 32;
    public int DELAY = 20;
    public int NUMBER_OF_PIXELS = 2048;


    int test = 1;
    public boolean pixelFlipped = false;
    byte pixelArray[] = new byte[2048];


    public GraphicsPanel(LayoutManager layout){
        super(layout);
        setBackground(Color.decode("#272324"));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }


    public void draw(Graphics g){
        for(int i = 0; i < NUMBER_OF_PIXELS; i++){
            int posX = i % DISPLAY_WIDTH;
            int posY = (int) Math.floor(i/DISPLAY_WIDTH);

            if (pixelArray[i] != 1) {
                g.setColor(Color.decode("#272324"));
            }
            else {
                g.setColor(Color.decode("#E2CD6D"));
            }
            g.fillRect(posX*PIXEL_WIDTH, posY*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }

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
                index = (int) ((posX + xline) + (posY + yline)*DISPLAY_WIDTH)%2048;

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
        repaint();
    }


    public void clearScreen(){
        // clears the screen!
        for (int i = 0; i < NUMBER_OF_PIXELS; i++){
            pixelArray[i] = 0;
        }
        repaint();
    }
}